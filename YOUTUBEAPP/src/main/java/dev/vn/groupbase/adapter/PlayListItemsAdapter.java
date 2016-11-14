package dev.vn.groupbase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.api.entity.PlayListItemEntity;
import dev.vn.groupbase.database.HistoryTable;
import dev.vn.groupbase.database.YouTubeAppManager;
import dev.vn.groupbase.listener.OnItemClickListener;

/**
 * Created by acnovn on 10/26/16.
 */

public class PlayListItemsAdapter extends RecyclerView.Adapter<PlayListItemsAdapter.PlayListItemsViewHolder> {
    private List<PlayListItemEntity> list;
    private Context mContext;
    int dpi;
    private OnItemClickListener listener;
    private boolean isSelect = false;
    private int indexSelect = 0;
    List<HistoryTable> historyTablesList;

    public PlayListItemsAdapter(List<PlayListItemEntity> lst, Context context, OnItemClickListener itemClickListener) {
        list = lst;
        mContext = context;
        listener = itemClickListener;
        historyTablesList = YouTubeAppManager.getHistory();
    }

    private boolean checkWatch(String videoId) {
        for (HistoryTable item : historyTablesList) {
            if (item.videoId.equals(videoId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public PlayListItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist_items, parent, false);

        return new PlayListItemsViewHolder(itemView);
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setIndexSelect(int index) {
        indexSelect = index;
    }

    @Override
    public void onBindViewHolder(final PlayListItemsViewHolder holder, int position) {
        PlayListItemEntity obj = list.get(position);
        holder.tv_title.setText(obj.snippet.title);
        if (checkWatch(obj.contentDetails.videoId)) {
            holder.watched.setVisibility(View.VISIBLE);
        } else {
            holder.watched.setVisibility(View.INVISIBLE);
        }
        String url = "";
        try {
            if (!TextUtils.isEmpty(obj.snippet.thumbnails.maxres.url)) {
                url = obj.snippet.thumbnails.maxres.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.standard.url)) {
                url = obj.snippet.thumbnails.standard.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.high.url)) {
                url = obj.snippet.thumbnails.high.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.medium.url)) {
                url = obj.snippet.thumbnails.medium.url;
            } else if (!TextUtils.isEmpty(obj.snippet.thumbnails.default_url.url)) {
                url = obj.snippet.thumbnails.default_url.url;
            }
        } catch (Exception e) {
            url = "";
        }

        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error)
                .skipMemoryCache(false)
                .dontAnimate()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.iv_play.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.iv_play.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.iv_Thumbnails);
        if (isSelect && position == indexSelect) {
            holder.ln_item.setSelected(true);
        } else {
            holder.ln_item.setSelected(false);
        }
    }

    public String getVideoId(int position) {
        return list.get(position).contentDetails.videoId;
    }

    public PlayListItemEntity getItemObject(int position) {
        return this.list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PlayListItemsViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_Thumbnails;
        public TextView tv_title;
        public ImageView iv_play;
        public View ln_item;
        public TextView watched;

        public PlayListItemsViewHolder(final View itemView) {
            super(itemView);
            iv_Thumbnails = (ImageView) itemView.findViewById(R.id.iv_thumbnails);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_play = (ImageView) itemView.findViewById(R.id.iv_play);
            ln_item = itemView.findViewById(R.id.ln_item);
            watched = (TextView) itemView.findViewById(R.id.watched);
            watched = (TextView) itemView.findViewById(R.id.watched);
            ln_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });
        }
    }
}
