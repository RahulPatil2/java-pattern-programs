package com.java.pattern.program.adapters;

import android.app.Activity;
import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.java.pattern.program.R;
import com.java.pattern.program.data.constant.AppConstant;
import com.java.pattern.program.listeners.WebListener;
import com.java.pattern.program.models.content.Contents;
import com.java.pattern.program.utility.CodeMakerSp;
import com.java.pattern.program.webengine.PostWebEngine;

import java.util.ArrayList;
import java.util.Random;

public class DetailPagerAdapter extends PagerAdapter {

    private Context mContext;

    private ArrayList<Contents> mItemList;
    private LayoutInflater inflater;
    private LinearLayout mLoadingView, mNoDataView;
    private ImageView mPostImage;
    private TextView mPostTitle;
    private WebView mWebView;
    private PostWebEngine mWebEngine;

    CodeMakerSp codeMakerSp = new CodeMakerSp();

    public DetailPagerAdapter(Context mContext, ArrayList<Contents> mItemList) {
        this.mContext = mContext;
        this.mItemList = mItemList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }


    @Override
    public Object instantiateItem(final ViewGroup view, final int position) {

        View rootView = inflater.inflate(R.layout.item_details_view_pager, view, false);

        mLoadingView = (LinearLayout) rootView.findViewById(R.id.loadingView);
        mNoDataView = (LinearLayout) rootView.findViewById(R.id.noDataView);

        mPostImage = (ImageView) rootView.findViewById(R.id.post_img);
        mPostTitle = (TextView) rootView.findViewById(R.id.title_text);


        mWebView = (WebView) rootView.findViewById(R.id.web_view);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebEngine = new PostWebEngine(mWebView, (Activity) mContext);
        mWebEngine.initWebView();

        mWebEngine.initListeners(new WebListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onLoaded() {
            }

            @Override
            public void onProgress(int progress) {
            }

            @Override
            public void onNetworkError() {
            }

            @Override
            public void onPageTitle(String title) {
            }
        });

        showLoader();

        Random rand = new Random();
        int i = rand.nextInt(5) + 1;
        switch (i) {
            case 1:
                mPostImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_question1));
                break;
            case 2:
                mPostImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_question2));
                break;

            case 3:
                mPostImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_question3));
                break;

            case 4:
                mPostImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_question4));
                break;
            case 5:
                mPostImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_question5));
                break;
            default:
                break;
        }

        mPostTitle.setText(mItemList.get(position).getTitle());

        String syntaxhighlighter = codeMakerSp.Syntaxhighlighter(mItemList.get(position).getDetails().toString());

        mWebEngine.loadHtml(syntaxhighlighter + AppConstant.CSS);
        hideLoader();


        view.addView(rootView);

        return rootView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


    public void showLoader() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }

        if (mNoDataView != null) {
            mNoDataView.setVisibility(View.GONE);
        }
    }

    public void hideLoader() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mNoDataView != null) {
            mNoDataView.setVisibility(View.GONE);
        }
    }

}
