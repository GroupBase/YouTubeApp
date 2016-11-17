package dev.vn.groupbase.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.thn.groupbase.gameshow.R;
import dev.vn.groupbase.api.entity.PlayListEntity;
import dev.vn.groupbase.listener.OnItemClickListener;

/**
 * Created by acnovn on 10/26/16.
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListViewHolder> {
    private List<PlayListEntity> list;
    private Context mContext;
    int dpi;
    private OnItemClickListener listener;
    public void setListener(OnItemClickListener itemClickListener){
        this.listener = itemClickListener;
    }
    public PlayListAdapter(List<PlayListEntity> lst, Context context) {
        list = lst;
        mContext = context;
//        dpi = mContext.getResources().getDisplayMetrics().densityDpi;
    }
    public PlayListEntity getObject(int  position) {
        return list.get(position);
    }
    @Override
    public PlayListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);

        return new PlayListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayListViewHolder holder, int position) {
        final PlayListEntity obj = list.get(position);
        holder.tv_title.setText(obj.snippet.title);
        holder.tv_video_count.setText(obj.contentDetails.itemCount+"");
        String url;
        try {
            if (!TextUtils.isEmpty(obj.snippet.thumbnails.maxres.url)) {
                url = obj.snippet.thumbnails.maxres.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.standard.url)) {
                url = obj.snippet.thumbnails.standard.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.high.url)) {
                url = obj.snippet.thumbnails.high.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.medium.url)) {
                url = obj.snippet.thumbnails.medium.url;
            } else {
                url = obj.snippet.thumbnails.default_url.url;
            }
        } catch (Exception e){
            url = "";
        }

        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error)
                .skipMemoryCache(false)
                .into(holder.iv_Thumbnails);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PlayListViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_Thumbnails;
        public TextView tv_title;
        public TextView tv_video_count;
        public PlayListViewHolder(View itemView) {
            super(itemView);
            iv_Thumbnails = (ImageView) itemView.findViewById(R.id.iv_thumbnails);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_video_count = (TextView) itemView.findViewById(R.id.tv_video_count);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null) {
                        listener.onItemClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }
}
