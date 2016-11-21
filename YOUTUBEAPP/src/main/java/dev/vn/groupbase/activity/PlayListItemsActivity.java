package dev.vn.groupbase.activity;

import android.content.Intent;
import android.os.Bundle;
import dev.vn.groupbase.common.ViewManager;
import dev.vn.groupbase.fragment.PlayListItemsFragment;

/**
 * Created by acnovn on 10/27/16.
 */

public class PlayListItemsActivity extends BaseActivity {

    @Override
    protected void onCreateExecute(Bundle savedInstanceState) {
        if (savedInstanceState == null){
            Bundle bundle ;
            Intent intent = getIntent();
            bundle = intent.getExtras();
            ViewManager.getInstance().addFragment(PlayListItemsFragment.newInstance(bundle,PlayListItemsFragment.class),false);
        }
    }

}
