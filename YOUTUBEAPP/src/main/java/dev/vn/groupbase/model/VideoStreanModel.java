package dev.vn.groupbase.model;

import android.os.Handler;

import com.android.volley.VolleyError;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import dev.vn.groupbase.api.VideoStreamApi;
import dev.vn.groupbase.common.DebugLog;
import dev.vn.groupbase.common.ModelCommon;
import dev.vn.groupbase.common.ProgressLoading;
import dev.vn.groupbase.listener.VideoStreamListener;
import gmo.hcm.net.lib.ApiListener;

/**
 * Created by acnovn on 11/4/16.
 */

public class VideoStreanModel extends ModelCommon {
    public VideoStreanModel(VideoStreamListener listener) {
        super(listener);
    }
    public boolean connected = true;
    Handler handler = new Handler();
    @Override
    public void onStart() {
        super.onStart();
    }

    public void requestStream(final String videoid) {
        VideoStreamApi api = new VideoStreamApi(new ApiListener() {
            @Override
            public void onError(VolleyError statusCode) {
                ((VideoStreamListener) mCallBack).onError();
                DebugLog.log_e("url","error");
            }

            @Override
            public void onFinish(Object result, boolean endRequest) {
                String result_str = result.toString();
                String data_decode;
                try {
                    data_decode = URLDecoder.decode(URLDecoder.decode(result_str, "UTF-8"), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    data_decode = "";
                }

                int index_url_encoded_fmt_stream_map = data_decode.indexOf("url_encoded_fmt_stream_map");
                String data = data_decode.substring(index_url_encoded_fmt_stream_map, data_decode.length());
                String[] url_list = data.split("https://");
                final ArrayList<String> url_stream = new ArrayList<>();
                for (int i = 0; i < url_list.length; i++) {
                    if (url_list[i].contains("ratebypass=yes")) {
                        StringBuilder fix_url = new StringBuilder();
                        fix_url.append("https://");
                        fix_url.append(url_list[i].substring(0, url_list[i].indexOf(";")));
                        url_stream.add(fix_url.toString());
                    }
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
                                DebugLog.log_e("Play", "connect");
                                connected = true;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((VideoStreamListener) mCallBack).onLoadData(url_stream);
                                    }
                                });

                            } else {
                                connected = false;
                                DebugLog.log_e("Play", "no connect");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        DebugLog.log_e("Play", "re call api");
                                        requestStream(videoid);
                                    }
                                },100);
                            }

                        } catch (IOException e) {
                            con.disconnect();
                            DebugLog.log_e("Play", e.getMessage());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((VideoStreamListener) mCallBack).onError();
                                }
                            },100);

                        }
                    }
                }).start();

            }
        });
        api.setVideoId(videoid);
        api.execute();
        DebugLog.log_e("url",api.getUrl());
    }

}
