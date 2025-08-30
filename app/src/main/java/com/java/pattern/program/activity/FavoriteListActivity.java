package com.java.pattern.program.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdView;
import com.java.pattern.program.R;
import com.java.pattern.program.adapters.FavoriteAdapter;
import com.java.pattern.program.data.constant.AppConstant;
import com.java.pattern.program.data.sqlite.FavoriteDbController;
import com.java.pattern.program.listeners.ListItemClickListener;
import com.java.pattern.program.models.content.Contents;
import com.java.pattern.program.models.favorite.FavoriteModel;
import com.java.pattern.program.utility.ActivityUtilities;
import com.java.pattern.program.utility.AdsUtilities;
import com.java.pattern.program.utility.DialogUtilities;

import java.util.ArrayList;


public class FavoriteListActivity extends BaseActivity {

    private Activity mActivity;
    private Context mContext;

    private ArrayList<FavoriteModel> mFavoriteList;
    private ArrayList<Contents> mContentList;
    private FavoriteAdapter mFavoriteAdapter = null;
    private RecyclerView mRecycler;

    private FavoriteDbController mFavoriteDbController;
    private MenuItem mMenuItemDeleteAll;
    private int mAdapterPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initFunctionality();
        initListener();
    }

    private void initVar() {
        mActivity = FavoriteListActivity.this;
        mContext = mActivity.getApplicationContext();

        mFavoriteList = new ArrayList<>();
        mContentList = new ArrayList<>();
    }

    private void initView() {
        setContentView(R.layout.activity_common_recycler);

        mRecycler = (RecyclerView) findViewById(R.id.rvContent);
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mFavoriteAdapter = new FavoriteAdapter(mContext, mActivity, mFavoriteList);
        mRecycler.setAdapter(mFavoriteAdapter);

        initToolbar(true);
        setToolbarTitle(getString(R.string.site_menu_fav));
        enableUpButton();
        initLoader();
    }

    private void initFunctionality() {

        // show full-screen ads
        AdsUtilities.getInstance(mContext).showFullScreenAd(mActivity);
        // show banner ads
        AdsUtilities.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adsView));
    }

    public void updateUI() {
        showLoader();

        if (mFavoriteDbController == null) {
            mFavoriteDbController = new FavoriteDbController(mContext);
        }
        mFavoriteList.clear();
        mFavoriteList.addAll(mFavoriteDbController.getAllData());

        mContentList.clear();
        for (int i = 0; i < mFavoriteList.size(); i++) {
            Contents model = new Contents(mFavoriteList.get(i).getTitle(), AppConstant.EMPTY_STRING, mFavoriteList.get(i).getDetails(), true);
            mContentList.add(model);
        }

        mFavoriteAdapter.notifyDataSetChanged();

        hideLoader();

        if (mFavoriteList.size() == 0) {
            showEmptyView();
            if (mMenuItemDeleteAll != null) {
                mMenuItemDeleteAll.setVisible(false);
            }
        } else {
            if (mMenuItemDeleteAll != null) {
                mMenuItemDeleteAll.setVisible(true);
            }
        }
    }

    public void initListener() {
        // recycler list item click listener
        mFavoriteAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                mAdapterPosition = position;

                int id = view.getId();

                if (id == R.id.btn_delete) {
                    FragmentManager manager = getSupportFragmentManager();
                    DialogUtilities dialog = DialogUtilities.newInstance(
                            getString(R.string.site_menu_fav),
                            getString(R.string.delete_fav_item),
                            getString(R.string.yes),
                            getString(R.string.no),
                            AppConstant.BUNDLE_KEY_DELETE_EACH_FAV
                    );
                    dialog.show(manager, AppConstant.BUNDLE_KEY_DIALOG_FRAGMENT);

                } else if (id == R.id.lyt_container) {
                    ActivityUtilities.getInstance().invokeDetailsActiviy(
                            mActivity,
                            DetailsActivity.class,
                            mContentList,
                            position,
                            false
                    );
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            ActivityUtilities.getInstance().invokeNewActivity(
                    mActivity,
                    MainActivity.class,
                    true
            );
            return true;
        } else if (id == R.id.menus_delete_all) {
            FragmentManager manager = getSupportFragmentManager();
            DialogUtilities dialog = DialogUtilities.newInstance(
                    getString(R.string.site_menu_fav),
                    getString(R.string.delete_all_fav_item),
                    getString(R.string.yes),
                    getString(R.string.no),
                    AppConstant.BUNDLE_KEY_DELETE_ALL_FAV
            );
            dialog.show(manager, AppConstant.BUNDLE_KEY_DIALOG_FRAGMENT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityUtilities.getInstance().invokeNewActivity(mActivity, MainActivity.class, true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete_all, menu);
        mMenuItemDeleteAll = menu.findItem(R.id.menus_delete_all);

        updateUI();

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFavoriteAdapter != null) {
            updateUI();
        }
    }

    @Override
    public void onComplete(Boolean isOkPressed, String viewIdText) {
        if (isOkPressed) {
            if (viewIdText.equals(AppConstant.BUNDLE_KEY_DELETE_ALL_FAV)) {
                mFavoriteDbController.deleteAllFav();
                updateUI();
            } else if (viewIdText.equals(AppConstant.BUNDLE_KEY_DELETE_EACH_FAV)) {
                mFavoriteDbController.deleteEachFav(mFavoriteList.get(mAdapterPosition).getTitle());
                updateUI();
            }
        }
    }
}
