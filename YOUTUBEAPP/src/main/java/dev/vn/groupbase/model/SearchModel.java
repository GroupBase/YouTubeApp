package dev.vn.groupbase.model;

import com.android.volley.VolleyError;

import java.util.List;

import dev.vn.groupbase.api.SearchApi;
import dev.vn.groupbase.api.entity.SearchEnity;
import dev.vn.groupbase.api.parser.SearchParser;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.listener.SearchListener;
import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 11/3/16.
 */

public class SearchModel extends ModelCommon {
    public SearchModel(SearchListener listener) {
        super(listener);
    }

    public void searchrequest(String keyword) {
        SearchApi api = new SearchApi(new ApiListener() {
            @Override
            public void onError(VolleyError statusCode) {

            }

            @Override
            public void onFinish(Object result, boolean endRequest) {
                List<SearchEnity> list = SearchParser.parser(result.toString());
                ((SearchListener) mCallBack).onLoadData(list);
            }
        });
        api.setkeyWord(keyword);
        api.execute();
    }
}
