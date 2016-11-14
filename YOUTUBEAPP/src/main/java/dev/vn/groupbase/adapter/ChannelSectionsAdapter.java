package dev.vn.groupbase.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.database.ChannelSectionsTable;


/**
 * Created by acnovn on 10/26/16.
 */

public class ChannelSectionsAdapter extends RecyclerView.Adapter<ChannelSectionsAdapter.ChannelSectionsViewHolder> {
    private Context mContext;
    private List<ChannelSectionsTable> list;

    public ChannelSectionsAdapter(List<ChannelSectionsTable> lst, Context context) {
        list = lst;
        mContext = context;
    }

    @Override
    public ChannelSectionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_channelsections, parent, false);

        return new ChannelSectionsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChannelSectionsViewHolder holder, int position) {
        final ChannelSectionsTable obj = list.get(position);
        holder.title.setText(obj.name);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString(PlayListModel.LIST_PLAY_LIST_KEY,obj.playList);
//                bundle.putString(ChannelSectionsModel.CHENNEL_SECTIONS_TITLE,obj.name);
//                Intent intent = new Intent(mContext, PlayListActivity.class);
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChannelSectionsViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ChannelSectionsViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
