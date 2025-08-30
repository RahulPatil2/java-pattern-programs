package com.java.pattern.program.utility;

import android.app.Activity;
import android.content.Intent;

import com.java.pattern.program.data.constant.AppConstant;
import com.java.pattern.program.models.content.Contents;

import java.util.ArrayList;

public class ActivityUtilities {

    private static ActivityUtilities sActivityUtilities = null;

    public static ActivityUtilities getInstance() {
        if (sActivityUtilities == null) {
            sActivityUtilities = new ActivityUtilities();
        }
        return sActivityUtilities;
    }

    public void invokeNewActivity(Activity activity, Class<?> tClass, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeCustomUrlActivity(Activity activity, Class<?> tClass, String pageTitle, String pageUrl, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putExtra(AppConstant.BUNDLE_KEY_TITLE, pageTitle);
        intent.putExtra(AppConstant.BUNDLE_KEY_URL, pageUrl);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

    public void invokeDetailsActiviy(Activity activity, Class<?> tClass, ArrayList<Contents> contentList, int position, boolean shouldFinish) {
        Intent intent = new Intent(activity, tClass);
        intent.putParcelableArrayListExtra(AppConstant.BUNDLE_KEY_ITEM, contentList);
        intent.putExtra(AppConstant.BUNDLE_KEY_INDEX, position);
        activity.startActivity(intent);
        if (shouldFinish) {
            activity.finish();
        }
    }

}
