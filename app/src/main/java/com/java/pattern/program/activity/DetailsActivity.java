package com.java.pattern.program.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.java.pattern.program.R;
import com.java.pattern.program.adapters.DetailPagerAdapter;
import com.java.pattern.program.data.constant.AppConstant;
import com.java.pattern.program.data.sqlite.FavoriteDbController;
import com.java.pattern.program.models.content.Contents;
import com.java.pattern.program.models.favorite.FavoriteModel;
import com.java.pattern.program.utility.AdsUtilities;
import com.java.pattern.program.utility.CodeMakerSp;
import com.java.pattern.program.utility.DialogUtilities;
import com.java.pattern.program.utility.TtsEngine;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends BaseActivity {
    private Activity mActivity;
    private Context mContext;

    private ArrayList<Contents> mItemList;
    private int mCurrentIndex;
    private ViewPager mViewPager;
    private DetailPagerAdapter mPagerAdapter = null;
    private LinearLayout mLytBottomPanel;
    private ImageButton mBtnPrev, mBtnNext;
    private TextView mTxtCounter;
    private ImageButton mBtnFavorite, mBtnShare, mBtnCopy, mBtnRun;

    // Favourites view
    private List<FavoriteModel> mFavoriteList;
    private FavoriteDbController mFavoriteDbController;
    private boolean mIsFavorite = false;


    private TtsEngine mTtsEngine;
    private boolean mIsTtsPlaying = false;
    private String mTtsText;
    private MenuItem menuItemTTS;

    CodeMakerSp codeMakerSp = new CodeMakerSp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVar();
        initView();
        initFunctionality();
        initListener();
    }

    private void initVar() {
        mActivity = DetailsActivity.this;
        mContext = mActivity.getApplicationContext();

        mFavoriteList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            mCurrentIndex = intent.getIntExtra(AppConstant.BUNDLE_KEY_INDEX, 0);
            mItemList = intent.getParcelableArrayListExtra(AppConstant.BUNDLE_KEY_ITEM);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_details);

        mTxtCounter = (TextView) findViewById(R.id.menus_counter);
        mBtnFavorite = (ImageButton) findViewById(R.id.menus_fav);
        mBtnShare = (ImageButton) findViewById(R.id.menus_share);
        mBtnCopy = (ImageButton) findViewById(R.id.menus_copy);
        mBtnRun = (ImageButton) findViewById(R.id.menu_run);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mLytBottomPanel = (LinearLayout) findViewById(R.id.bottomPanel);
        mBtnPrev = (ImageButton) findViewById(R.id.btn_prev);
        mBtnNext = (ImageButton) findViewById(R.id.btn_next);

        initToolbar(false);
        setToolbarTitle(getString(R.string.post_details));
        enableUpButton();
    }

    private void initFunctionality() {

        mPagerAdapter = new DetailPagerAdapter(mActivity, mItemList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrentIndex);

        if(!(mItemList.get(mViewPager.getCurrentItem()).getTitle().contains("\n"))){
            mBtnRun.setVisibility(View.GONE);
        }

        mTtsText = Html.fromHtml(mItemList.get(mViewPager.getCurrentItem()).getTitle() + AppConstant.EMPTY_STRING + mItemList.get(mViewPager.getCurrentItem()).getDetails()).toString();

        mFavoriteDbController = new FavoriteDbController(mContext);
        mFavoriteList.addAll(mFavoriteDbController.getAllData());
        isFavorite();

        mTtsEngine = new TtsEngine(mActivity);

        updateCounter();

        if (mItemList.size() == AppConstant.BUNDLE_KEY_FIRST_INDEX) {
            mLytBottomPanel.setVisibility(View.GONE);
        }

        // load full screen ad
        AdsUtilities.getInstance(mContext).loadFullScreenAd(mActivity);
        // show banner ads
        AdsUtilities.getInstance(mContext).showBannerAd((AdView) findViewById(R.id.adsView));
    }

    public void initListener() {
        mBtnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contents model = mItemList.get(mViewPager.getCurrentItem());
                mIsFavorite = !mIsFavorite;
                if (mIsFavorite) {
                    mFavoriteDbController.insertData(model.getTitle(), model.getDetails());
                    mFavoriteList.add(new FavoriteModel(AppConstant.BUNDLE_KEY_ZERO_INDEX, model.getTitle(), model.getDetails()));
                    Toast.makeText(getApplicationContext(), getString(R.string.added_to_fav), Toast.LENGTH_SHORT).show();
                } else {
                    mFavoriteDbController.deleteEachFav(model.getTitle());
                    for (int i = 0; i < mFavoriteList.size(); i++) {
                        if (mFavoriteList.get(i).getDetails().equals(model.getDetails())) {
                            mFavoriteList.remove(i);
                            break;
                        }
                    }
                    Toast.makeText(getApplicationContext(), getString(R.string.removed_from_fav), Toast.LENGTH_SHORT).show();
                }
                setFavorite();
            }
        });

        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contents model = mItemList.get(mViewPager.getCurrentItem());
                final String appPackageName = mActivity.getPackageName();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, (codeMakerSp.CodeSyntaxMaker(model.getDetails())
                        + AppConstant.EMPTY_STRING
                        + mActivity.getResources().getString(R.string.share_text)
                        + "https://play.google.com/store/apps/details?id=" + appPackageName));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
            }
        });

        mBtnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Contents model = mItemList.get(mViewPager.getCurrentItem());

                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Text Label", codeMakerSp.CodeSyntaxMaker(model.getDetails()));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), getString(R.string.copy_to_clipboard), Toast.LENGTH_SHORT).show();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateCounter();
                mIsFavorite = false;
                isFavorite();

                // show full-screen ads
                AdsUtilities.getInstance(mContext).showFullScreenAd(mActivity);

                if (position % AppConstant.ADS_INTERVAL == 0) {
                    // load full screen ad
                    AdsUtilities.getInstance(mContext).loadFullScreenAd(mActivity);
                }

                mTtsText = Html.fromHtml(mItemList.get(mViewPager.getCurrentItem()).getTitle() + AppConstant.EMPTY_STRING + mItemList.get(mViewPager.getCurrentItem()).getDetails()).toString();
                if (mIsTtsPlaying) {
                    toggleTtsPlay(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - AppConstant.BUNDLE_KEY_FIRST_INDEX);
                updateCounter();
            }
        });
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + AppConstant.BUNDLE_KEY_FIRST_INDEX);
                updateCounter();
            }
        });

        mBtnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getSupportFragmentManager();
                DialogUtilities dialog = DialogUtilities.newInstanceRun(getString(R.string.dialogTitle), mItemList.get(mViewPager.getCurrentItem()).getTitle(), getString(R.string.close), "Hi");
                dialog.show(manager, AppConstant.BUNDLE_KEY_DIALOG_FRAGMENT);

            }
        });
    }

    public void isFavorite() {
        for (int i = 0; i < mFavoriteList.size(); i++) {
            if (mFavoriteList.get(i).getDetails().equals(mItemList.get(mViewPager.getCurrentItem()).getDetails())) {
                mIsFavorite = true;
                break;
            }
        }
        setFavorite();
    }

    public void setFavorite() {
        if (mIsFavorite) {
            mBtnFavorite.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.ic_fav));
        } else {
            mBtnFavorite.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.ic_un_fav));
        }
    }

    public void updateCounter() {
        String counter = String.format(getString(R.string.item_counter), mViewPager.getCurrentItem() + AppConstant.BUNDLE_KEY_FIRST_INDEX, mItemList.size());
        mTxtCounter.setText(counter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            // show full-screen ads
            AdsUtilities.getInstance(mContext).showFullScreenAd(mActivity);
            return true;
        } else if (id == R.id.menus_read_article) {
            if (mItemList != null) {
                toggleTtsPlay(false);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleTtsPlay(boolean isPageScrolledWhilePlaying) {
        if (mIsTtsPlaying & !isPageScrolledWhilePlaying) {
            mTtsEngine.releaseEngine();
            mIsTtsPlaying = false;
        } else if (mIsTtsPlaying & isPageScrolledWhilePlaying) {
            mTtsEngine.releaseEngine();
            mTtsEngine.startEngine(mTtsText);
            mIsTtsPlaying = true;
        } else {
            mTtsEngine.startEngine(mTtsText);
            mIsTtsPlaying = true;
        }
        toggleTtsView();
    }

    private void toggleTtsView() {
        if (mIsTtsPlaying) {
            menuItemTTS.setTitle(R.string.site_menu_stop_reading);
        } else {
            menuItemTTS.setTitle(R.string.read_post);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTtsEngine.releaseEngine();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTtsEngine.releaseEngine();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);

        menuItemTTS = menu.findItem(R.id.menus_read_article);

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // show full-screen ads
        AdsUtilities.getInstance(mContext).showFullScreenAd(mActivity);
    }

}