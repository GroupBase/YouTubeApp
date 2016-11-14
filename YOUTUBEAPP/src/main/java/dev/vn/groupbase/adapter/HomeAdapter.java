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

import java.util.ArrayList;
import java.util.List;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.api.entity.PlayListItemEntity;


/**
 * Created by acnovn on 10/26/16.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private List<PlayListItemEntity> list;
    private Context mContext;
    int dpi;

    public HomeAdapter(List<PlayListItemEntity> lst ,Context context) {
        list = lst;
        mContext = context;
//        dpi = mContext.getResources().getDisplayMetrics().densityDpi;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);

        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        final PlayListItemEntity obj = list.get(position);
        holder.tv_title.setText(obj.snippet.title);
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

        holder.iv_Thumbnails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
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
//                bundle.putString("video_id",obj.contentDetails.videoId);
//                bundle.putString("url_img",url);
//                Intent intent = new Intent(mContext, VideoPlayActivity.class);
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
            }
        });
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

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_Thumbnails;
        public TextView tv_title;

        public HomeViewHolder(View itemView) {
            super(itemView);
            iv_Thumbnails = (ImageView) itemView.findViewById(R.id.iv_thumbnails);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
    public static class ChannelSection {
        public String title;
        public ArrayList<String> id;
    }
}
