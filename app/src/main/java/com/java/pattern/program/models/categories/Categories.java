package com.java.pattern.program.models.categories;

import android.os.Parcel;
import android.os.Parcelable;

public class Categories implements Parcelable {
    String categoryId;
    String categoryName;

    public Categories(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryId);
        dest.writeString(categoryName);
    }

    protected Categories(Parcel in) {
        categoryId = in.readString();
        categoryName = in.readString();
        categoryId = in.readString();
    }

    public static Creator<Categories> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Categories> CREATOR = new Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel source) {
            return new Categories(source);
        }

        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };
}