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

import app.thn.groupbase.phimkinhdi.R;
import dev.vn.groupbase.api.entity.PlayListItemEntity;
import dev.vn.groupbase.database.HistoryTable;
import dev.vn.groupbase.database.YouTubeAppManager;
import dev.vn.groupbase.listener.LoadMoreListener;
import dev.vn.groupbase.listener.OnItemClickListener;

/**
 * Created by acnovn on 10/26/16.
 */

public class PlayListItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PlayListItemEntity> list;
    private Context mContext;
    int dpi;
    private OnItemClickListener listener;
    private boolean isSelect = false;
    private int indexSelect = 0;
    private List<HistoryTable> historyTablesList;
    private final int TYPE_DATA = 0;
    private final int TYPE_LOAD = 1;
    private LoadMoreListener listenerLoadMore;

    public void setLoadMoreListener(LoadMoreListener listener) {
        this.listenerLoadMore = listener;
    }

    public PlayListItemsAdapter(List<PlayListItemEntity> lst, Context context, OnItemClickListener itemClickListener) {
        list = lst;
        mContext = context;
        listener = itemClickListener;
        historyTablesList = YouTubeAppManager.getHistory();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) != null){
            return TYPE_DATA;
        } else {
            return TYPE_LOAD;
        }
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_DATA){
            return new PlayListItemsViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_playlist_items, parent, false));
        }else{
            return new LoadHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_load_more,parent,false));
        }
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public void setIndexSelect(int index) {
        indexSelect = index;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_DATA){
            ((PlayListItemsViewHolder)holder).bindData(position);
        } else {
            if (listenerLoadMore!=null){
                listenerLoadMore.onLoadMore();
            }
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
        public void bindData(int position ){
            PlayListItemEntity obj = list.get(position);
            tv_title.setText(obj.snippet.title);
            if (checkWatch(obj.contentDetails.videoId)) {
                watched.setVisibility(View.VISIBLE);
            } else {
                watched.setVisibility(View.INVISIBLE);
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
                            iv_play.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            iv_play.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(iv_Thumbnails);
            if (isSelect && position == indexSelect) {
                ln_item.setSelected(true);
            } else {
                ln_item.setSelected(false);
            }
        }
    }
}
