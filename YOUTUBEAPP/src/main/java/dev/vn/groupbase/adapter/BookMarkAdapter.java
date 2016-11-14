package dev.vn.groupbase.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.activity.VideoPlayActivity;
import dev.vn.groupbase.database.BookMarkTable;

/**
 * Created by nghiath on 11/10/16.
 */

public class BookMarkAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<String> mListHeader;
    private HashMap<String, List<BookMarkTable>> _listDataChild;

    public BookMarkAdapter(Context context, List<BookMarkTable> list) {
        mContext = context;
        mListHeader = new ArrayList<>();
        _listDataChild = new HashMap<>();
        HashMap<String, String> header = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            if (!header.containsKey(list.get(i).playListId)) {
                header.put(list.get(i).playListId, list.get(i).playListName);
                mListHeader.add(list.get(i).playListName);
            }
        }
        for (String key : header.keySet()) {
            String param = header.get(key);
            List<BookMarkTable> lst_obj= new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (key.equals(list.get(i).playListId)) {
                    lst_obj.add(list.get(i));
                }
            }
            _listDataChild.put(param,lst_obj);
        }

    }

    @Override
    public int getGroupCount() {
        return mListHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return _listDataChild.get(mListHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            LayoutInflater infalInflater = LayoutInflater.from(mContext);
            convertView = infalInflater.inflate(R.layout.item_book_mark_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.tv_group.setText(mListHeader.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        if (convertView == null) {
            LayoutInflater infalInflater = LayoutInflater.from(mContext);
            convertView = infalInflater.inflate(R.layout.item_book_mark, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        final BookMarkTable obj = _listDataChild.get(mListHeader.get(groupPosition)).get(childPosition);
        holder.tv_title.setText(obj.videoName);
        String url = obj.imgVideo;
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
        holder.ln_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String url;
                try {
                    if (!TextUtils.isEmpty(obj.imgVideo)) {
                        url = obj.imgVideo;
                    } else {
                        url = "";
                    }
                } catch (Exception e){
                    url = "";
                }
                bundle.putString("video_id",obj.videoId);
                bundle.putString("url_img",url);
                bundle.putString("title",obj.videoName);
                bundle.putSerializable("bookmark",obj);
                Intent intent = new Intent(mContext, VideoPlayActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        TextView tv_group;

        public GroupViewHolder(View v) {
            tv_group = (TextView) v.findViewById(R.id.tv_group);
        }
    }

    class ChildViewHolder {
        public ImageView iv_Thumbnails;
        public TextView tv_title;
        public ImageView iv_play;
        public View ln_item;

        public ChildViewHolder(View itemView) {
            iv_Thumbnails = (ImageView) itemView.findViewById(R.id.iv_thumbnails);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_play = (ImageView) itemView.findViewById(R.id.iv_play);
            ln_item = itemView.findViewById(R.id.ln_item);
        }
    }
}
