package com.example.caoyujie.jsbridge;

import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * @author caoyujie
 * WebChromeClient主要辅助WebView处理JavaScript的对话框、网站图标、网站title、加载进度等
 */
public class JSBridgeWebChromeClient extends WebChromeClient {
    /**
     * @param view
     * @param url
     * @param message       前端设置的内容
     * @param defaultValue
     * @param result        按确定时回调的结果, result.cancel()关闭回调,否则将卡住界面
     * @return              true:交给客户端自定义处理   false:游览器默认处理
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        JSBridgeManager.getInstance().excuteJS(view,message);
        result.cancel();
        return true;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }
}
