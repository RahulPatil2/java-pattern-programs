package com.java.pattern.program.adapters;

import android.app.Activity;
import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.java.pattern.program.R;
import com.java.pattern.program.listeners.ListItemClickListener;
import com.java.pattern.program.models.content.Contents;

import java.util.ArrayList;
import java.util.Random;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

    private Context mContext;
    private Activity mActivity;

    private ArrayList<Contents> mContentList;
    private ListItemClickListener mItemClickListener;

    public ContentAdapter(Context mContext, Activity mActivity, ArrayList<Contents> mContentList) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mContentList = mContentList;
    }

    public void setItemClickListener(ListItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content_recycler, parent, false);
        return new ViewHolder(view, viewType, mItemClickListener);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgPost;
        private TextView tvTitle;
        private LinearLayout lytContainer;
        private ListItemClickListener itemClickListener;


        public ViewHolder(View itemView, int viewType, ListItemClickListener itemClickListener) {
            super(itemView);

            this.itemClickListener = itemClickListener;
            // Find all views ids
            imgPost = (ImageView) itemView.findViewById(R.id.post_img);
            tvTitle = (TextView) itemView.findViewById(R.id.title_text);
            lytContainer = (LinearLayout) itemView.findViewById(R.id.lyt_container);

            lytContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition(), view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != mContentList ? mContentList.size() : 0);

    }

    @Override
    public void onBindViewHolder(ContentAdapter.ViewHolder mainHolder, int position) {
        final Contents model = mContentList.get(position);

        mainHolder.tvTitle.setText(model.getTitle());

        Random rand = new Random();
        int i = rand.nextInt(5) + 1;


        switch (i) {
            case 1:
                mainHolder.imgPost.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_question1));
                break;
            case 2:
                mainHolder.imgPost.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_question2));
                break;

            case 3:
                mainHolder.imgPost.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_question3));
                break;

            case 4:
                mainHolder.imgPost.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_question4));
                break;
            case 5:
                mainHolder.imgPost.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_question5));
                break;
            default:
                break;
        }

    }
}