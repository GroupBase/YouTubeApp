package dev.vn.groupbase.common;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;

import app.thn.groupbase.youtubeapp.R;


/**
 * Created by acnovn on 10/19/16.
 */

public class ProgressLoading {
    protected static ProgressDialog pd_loading;

    public static void show() {
        pd_loading = ProgressDialog.show(ViewManager.getInstance().getActivity(), null, null,
                true,
                false);
        pd_loading.setContentView(R.layout.base_loading);
        pd_loading.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pd_loading.setCanceledOnTouchOutside(false);
        pd_loading.getWindow().setGravity(Gravity.CENTER);
        pd_loading.setCancelable(false);
        pd_loading.show();
    }

    public static void dismiss() {
        pd_loading.dismiss();
    }
}
