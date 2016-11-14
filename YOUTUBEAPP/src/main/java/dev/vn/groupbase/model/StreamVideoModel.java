package dev.vn.groupbase.model;

import android.os.Handler;

import com.android.volley.VolleyError;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import dev.vn.groupbase.api.VideoStreamApi;
import dev.vn.groupbase.common.DebugLog;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.model.callback.ModelCallBackStreamVideo;
import dev.vn.groupbase.util.Helper;
import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 11/9/16.
 */

public class StreamVideoModel extends ModelCommon {
    private static String TAG = "StreamVideoModel";
    private Handler handler = new Handler();

    public StreamVideoModel(ModelCallBackStreamVideo listener) {
        super(listener);
    }

    public void requestStream(final String videoId) {
        if (!Helper.isNetworkConnected(mContext)){
            ((ModelCallBackStreamVideo) mCallBack).onError(ERROR_TYPE.NETWORK);
            return;
        }
        VideoStreamApi api = new VideoStreamApi(new ApiListener() {
            @Override
            public void onError(VolleyError statusCode) {
                DebugLog.log_e(TAG, "error");
                ((ModelCallBackStreamVideo) mCallBack).onError(ERROR_TYPE.NETWORK);
            }

            @Override
            public void onFinish(Object result, boolean endRequest) {
                DebugLog.log_e(TAG, "onFinish");
                String result_str = result.toString();
                String data_decode;
                try {
                    data_decode = URLDecoder.decode(URLDecoder.decode(result_str, "UTF-8"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    data_decode = "";
                }
                final ArrayList<String> url_stream = new ArrayList<>();
                try {
                    int index_url_encoded_fmt_stream_map = data_decode.indexOf("url_encoded_fmt_stream_map");
                    String data = data_decode.substring(index_url_encoded_fmt_stream_map, data_decode.length());
                    String[] url_list = data.split("https://");

                    for (int i = 0; i < url_list.length; i++) {
                        if (url_list[i].contains("ratebypass=yes")) {
                            StringBuilder fix_url = new StringBuilder();
                            fix_url.append("https://");
                            fix_url.append(url_list[i].substring(0, url_list[i].indexOf(";")));
                            url_stream.add(fix_url.toString());
                        }
                    }
                }catch (Exception e){
                    DebugLog.log_e(TAG, "parse");
                    ((ModelCallBackStreamVideo) mCallBack).onError(ERROR_TYPE.NETWORK);
                    return;
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection con = null;
                        try {
                            con = null;
                            URL url = new URL(url_stream.get(0));
                            con = (HttpURLConnection) url.openConnection();
                            con.setConnectTimeout(200);
                            int response_code = con.getResponseCode();
                            con.disconnect();
                            if (response_code == 200) {
                                DebugLog.log_e(TAG, "connect_stream");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((ModelCallBackStreamVideo) mCallBack).onData(url_stream.get(0));
                                    }
                                });

                            } else {
                                DebugLog.log_e(TAG, "no connect_stream");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        DebugLog.log_e(TAG, "re call api");
                                        requestStream(videoId);
                                    }
                                }, 100);
                            }

                        } catch (IOException e) {
                            con.disconnect();
                            DebugLog.log_e(TAG, e.getMessage());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
//                                    ((ModelCallBackStreamVideo) mCallBack).onError();
                                    DebugLog.log_e(TAG, "re call api");
                                    requestStream(videoId);
                                }
                            }, 500);

                        }
                    }
                }).start();
            }
        });
        api.setVideoId(videoId);
        api.execute();
    }
}
