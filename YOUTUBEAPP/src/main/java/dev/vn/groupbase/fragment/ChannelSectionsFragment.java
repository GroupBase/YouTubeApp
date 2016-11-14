package dev.vn.groupbase.fragment;

import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import app.thn.groupbase.youtubeapp.R;
import dev.vn.groupbase.adapter.ChannelSectionsAdapter;
import dev.vn.groupbase.common.FragmentCommon;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.database.ChannelSectionsTable;
import dev.vn.groupbase.model.callback.ModelCallBackChannelSections;
import dev.vn.groupbase.model.ChannelSectionsModel;

/**
 * Created by acnovn on 10/26/16.
 */

public class ChannelSectionsFragment extends FragmentCommon implements ModelCallBackChannelSections {
    private List<ChannelSectionsTable> lst = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChannelSectionsAdapter mAdapter;
    private ChannelSectionsModel mModel;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_channelsections;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (mAdapter!=null) {
            recyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    protected ModelCommon initModel() {
        if (mModel == null) {
            mModel = new ChannelSectionsModel(this);
        }
        return mModel;
    }

    @Override
    public void onLoadChannelSection(ArrayList<ChannelSectionsTable> list) {
        lst = list;
        mAdapter = new ChannelSectionsAdapter(lst, getContext());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(ModelCommon.ERROR_TYPE error_type) {

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView = null;
    }
}
