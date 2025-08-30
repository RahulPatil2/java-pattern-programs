package com.java.pattern.program.adapters;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.text.Html;

import com.java.pattern.program.data.constant.AppConstant;
import com.java.pattern.program.fragment.PostListFragment;
import com.java.pattern.program.models.categories.Categories;

import java.util.ArrayList;

public class CategoryPagerAdapter extends FragmentStatePagerAdapter {

    private Activity mActivity;

    private ArrayList<Categories> categoryList;

    public CategoryPagerAdapter(Activity mActivity, FragmentManager fm, ArrayList<Categories> categoryList) {
        super(fm);
        this.mActivity = mActivity;
        this.categoryList = categoryList;
    }

    @Override
    public Fragment getItem(int position) {

        String categoryId = categoryList.get(position).getCategoryId();

        Fragment postListFragment = new PostListFragment();
        Bundle args = new Bundle();
        args.putString(AppConstant.BUNDLE_KEY_CATEGORY_ID, categoryId);
        postListFragment.setArguments(args);
        return postListFragment;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        title = categoryList.get(position).getCategoryName();
        return Html.fromHtml(title);
    }
}


