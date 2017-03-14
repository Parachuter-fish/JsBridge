package com.example.caoyujie.jsbridge.jsbridge.component;

import android.webkit.WebView;
import android.widget.Toast;

import com.example.caoyujie.jsbridge.jsbridge.IBridge;
import com.example.caoyujie.jsbridge.jsbridge.JsCallback;

import org.json.JSONObject;

/**
 * Created by caoyujie on 17/3/13.
 * 窗口类js接口
 */

public class WindowJSBridge implements IBridge {

    public void toast(WebView webView , JSONObject parmObject , JsCallback callback){
        Toast.makeText( webView.getContext() , parmObject.optString("msg"), Toast.LENGTH_SHORT).show();
    }
}
