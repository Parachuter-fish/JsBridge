package com.example.caoyujie.jsbridge.jsbridge.component;

import android.webkit.WebView;

import com.example.caoyujie.jsbridge.jsbridge.IBridge;
import com.example.caoyujie.jsbridge.jsbridge.JsCallback;

import org.json.JSONObject;

/**
 * Created by caoyujie on 17/3/13.
 * 获取应用信息类js接口
 */

public class AppInfoJSBridge implements IBridge {

    public void getPackageName(WebView webView , JSONObject parmObject , JsCallback callback){
        JsCallback.Entity entity = new JsCallback.Entity();
        entity.setCode(200);
        entity.setMessage("成功");
        entity.setData(webView.getContext().getPackageName());
        callback.apply(entity);
    }
}
