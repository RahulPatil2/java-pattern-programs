package com.java.pattern.program.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.java.pattern.program.R;
import com.java.pattern.program.activity.DetailsActivity;
import com.java.pattern.program.adapters.ContentAdapter;
import com.java.pattern.program.data.constant.AppConstant;
import com.java.pattern.program.listeners.ListItemClickListener;
import com.java.pattern.program.models.content.Contents;
import com.java.pattern.program.utility.ActivityUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PostListFragment extends Fragment {

    private LinearLayout mLoadingView, mNoDataView;

    private ArrayList<Contents> mContentList;
    private String mCategoryId;
    private ContentAdapter mAdapter = null;
    private RecyclerView mRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_content_list, container, false);


        initVar();
        initView(rootView);
        initFunctionality(rootView);
        initListener();

        return rootView;
    }


    public void initVar() {

        mContentList = new ArrayList<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mCategoryId = getArguments().getString(AppConstant.BUNDLE_KEY_CATEGORY_ID);
        }
    }

    public void initView(View rootView) {

        mLoadingView = (LinearLayout) rootView.findViewById(R.id.loadingView);
        mNoDataView = (LinearLayout) rootView.findViewById(R.id.noDataView);

        initLoader(rootView);

        mRecycler = (RecyclerView) rootView.findViewById(R.id.rvContent);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new ContentAdapter(getContext(), getActivity(), mContentList);
        mRecycler.setAdapter(mAdapter);

    }

    public void initLoader(View rootView) {
        mLoadingView = (LinearLayout) rootView.findViewById(R.id.loadingView);
        mNoDataView = (LinearLayout) rootView.findViewById(R.id.noDataView);
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

    public void showEmptyView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mNoDataView != null) {
            mNoDataView.setVisibility(View.VISIBLE);
        }
    }


    public void initFunctionality(View rootView) {

        showLoader();

        loadContents();
    }

    public void initListener() {

        mAdapter.setItemClickListener(new ListItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                int id = view.getId();

                if (id == R.id.lyt_container) {
                    ActivityUtilities.getInstance().invokeDetailsActiviy(
                            getActivity(),
                            DetailsActivity.class,
                            mContentList,
                            position,
                            false
                    );
                }
            }
        });
    }

    private void loadContents() {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getActivity().getAssets().open(AppConstant.CONTENT_FILE)));
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
        parseContent(sb.toString());
    }

    private void parseContent(String jsonData) {
        try {

            JSONObject jsonObjMain = new JSONObject(jsonData);
            JSONArray jsonArray1 = jsonObjMain.getJSONArray(AppConstant.JSON_KEY_ITEMS);

            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObj = jsonArray1.getJSONObject(i);

                String title = jsonObj.getString(AppConstant.JSON_KEY_TITLE);
                String category = jsonObj.getString(AppConstant.JSON_KEY_CATEGORY);
                String details = jsonObj.getString(AppConstant.JSON_KEY_DETAILS);


                if (mCategoryId.equals(category)) {
                    mContentList.add(new Contents(title, category, details, false));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        hideLoader();
        mAdapter.notifyDataSetChanged();
    }

}