package com.java.pattern.program.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.google.android.material.tabs.TabLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.java.pattern.program.R;
import com.java.pattern.program.adapters.CategoryPagerAdapter;
import com.java.pattern.program.data.constant.AppConstant;
import com.java.pattern.program.data.sqlite.NotificationDbController;
import com.java.pattern.program.models.categories.Categories;
import com.java.pattern.program.models.notification.NotificationModel;
import com.java.pattern.program.utility.ActivityUtilities;
import com.java.pattern.program.utility.AdsUtilities;
import com.java.pattern.program.utility.AppUtilities;
import com.java.pattern.program.utility.RateItDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private Activity mActivity;
    private Context mContext;

    private RelativeLayout mNotificationView;
    private ImageButton mImgBtnSearch;

    private ArrayList<Categories> mCategoryList;
    private ViewPager mViewPager;
    private CategoryPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RateItDialogFragment.show(this, getSupportFragmentManager());

        initVar();
        initView();
        loadData();
        initListener();

    }

    @Override
    protected void onPause() {
        super.onPause();

        //unregister broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(newNotificationReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //register broadcast receiver
        IntentFilter intentFilter = new IntentFilter(AppConstant.NEW_NOTI);
        LocalBroadcastManager.getInstance(this).registerReceiver(newNotificationReceiver, intentFilter);

        initNotification();

        // load full screen ad
        AdsUtilities.getInstance(mContext).loadFullScreenAd(mActivity);
    }

    // received new broadcast
    private BroadcastReceiver newNotificationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            initNotification();
        }
    };


    @Override
    public void onBackPressed() {
        AppUtilities.tapPromptToExit(mActivity);
    }

    private void initVar() {
        mActivity = MainActivity.this;
        mContext = getApplicationContext();

        mCategoryList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mNotificationView = (RelativeLayout) findViewById(R.id.notificationView);
        mImgBtnSearch = (ImageButton) findViewById(R.id.imgBtnSearch);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mPagerAdapter = new CategoryPagerAdapter(mActivity, getSupportFragmentManager(), mCategoryList);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        initToolbar(false);
        initDrawer();
        initLoader();
    }

    private void initListener() {
        //notification view click listener
        mNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtilities.getInstance().invokeNewActivity(mActivity, NotificationListActivity.class, false);
            }
        });

        // Search button click listener
        mImgBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtilities.getInstance().invokeNewActivity(mActivity, SearchActivity.class, false);
            }
        });

    }

    private void loadData() {
        showLoader();

        loadCategories();

        // show banner ads
        AdsUtilities.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adsView));
    }

    private void loadCategories() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(AppConstant.CATEGORY_FILE)));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        parseCategory(sb.toString());
    }

    private void parseCategory(String jsonData) {
        try {

            JSONObject jsonObjMain = new JSONObject(jsonData);
            JSONArray jsonArray1 = jsonObjMain.getJSONArray(AppConstant.JSON_KEY_ITEMS);

            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObj = jsonArray1.getJSONObject(i);

                String categoryID = jsonObj.getString(AppConstant.JSON_KEY_CATEGORY_ID);
                String categoryName = jsonObj.getString(AppConstant.JSON_KEY_CATEGORY_TITLE);

                mCategoryList.add(new Categories(categoryID, categoryName));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        hideLoader();
        mPagerAdapter.notifyDataSetChanged();
    }

    public void initNotification() {
        NotificationDbController notificationDbController = new NotificationDbController(mContext);
        TextView notificationCount = (TextView) findViewById(R.id.notificationCount);
        notificationCount.setVisibility(View.INVISIBLE);

        ArrayList<NotificationModel> notiArrayList = notificationDbController.getUnreadData();

        if (notiArrayList != null && !notiArrayList.isEmpty()) {
            int totalUnread = notiArrayList.size();
            if (totalUnread > 0) {
                notificationCount.setVisibility(View.VISIBLE);
                notificationCount.setText(String.valueOf(totalUnread));
            } else {
                notificationCount.setVisibility(View.INVISIBLE);
            }
        }

    }

}
