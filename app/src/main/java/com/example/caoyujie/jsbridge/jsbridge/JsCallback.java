package com.example.caoyujie.jsbridge.jsbridge;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by caoyujie on 17/3/13.
 * 返回结果给前端的回调类
 */
public class JsCallback {

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private static final String CALLBACK_FORMAT = "javascript:JSBridge.onFinish(%s,%s);";

    private String mPort;
    private WeakReference<WebView> webview;

    public JsCallback(WebView view, String port) {
        mPort = port;
        webview = new WeakReference<WebView>(view);
    }

    /**
     * 添加回调
     * @param callbackEntity   回调给前端结果实体类
     */
    public void apply(JsCallback.Entity callbackEntity) {
        JSONObject jsonObject = callbackEntity.toJSON();
        final String execJs = String.format(CALLBACK_FORMAT, mPort, String.valueOf(jsonObject));
        if (webview != null && webview.get() != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    webview.get().loadUrl(execJs);
                }
            });
        }
    }

    /**
     * 回调给前端结果实体类
     */
    public static class Entity {
        /**
         * 成功码
         */
        private int code;
        /**
         * 响应信息
         */
        private String message;
        /**
         * 回调数据
         */
        private String data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public JSONObject toJSON() {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject();
                jsonObject.put("code", code);
                jsonObject.put("message", message);
                jsonObject.put("data", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
    }
}
