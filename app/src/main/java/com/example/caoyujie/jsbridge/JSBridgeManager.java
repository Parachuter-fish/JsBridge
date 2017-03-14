package com.example.caoyujie.jsbridge;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebView;

import com.example.caoyujie.jsbridge.jsbridge.component.AppInfoJSBridge;
import com.example.caoyujie.jsbridge.jsbridge.IBridge;
import com.example.caoyujie.jsbridge.jsbridge.JSBridgeRequest;
import com.example.caoyujie.jsbridge.jsbridge.JsCallback;
import com.example.caoyujie.jsbridge.jsbridge.component.WindowJSBridge;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by caoyujie on 17/3/13.
 * jsBridge处理类,采用单例
 */

public class JSBridgeManager {
    private static JSBridgeManager INSTANCE;
    private static String BRIDGE_PREFIX = "JSBridge";       //约定前缀
    private static HashMap<String,HashMap<String,Method>> exposedMethods = new HashMap<>();

    public static JSBridgeManager getInstance() {
        if (INSTANCE == null) {
            synchronized (JSBridgeManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JSBridgeManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 执行js所调用的原生代码
     * @param webview
     * @param uriString     前端传来的信息,包括了js类名、方法名、参数、callback等
     */
    public void excuteJS(WebView webview , String uriString) {
        JSBridgeRequest jsBridgeRequest = parseJSBridge(uriString);
        if (jsBridgeRequest == null)
            return;
        Class registJSBridge = null;
        switch (jsBridgeRequest.getMethodName()) {
            case "toast":
                registJSBridge = WindowJSBridge.class;
                break;

            case "getPackageName":
                registJSBridge = AppInfoJSBridge.class;
                break;
        }
        if(registJSBridge != null){
            regitstJSBridge(registJSBridge);
                callJava(webview , registJSBridge , jsBridgeRequest);
        }
    }

    /**
     * 将前端传过来的信息转换成 js请求model
     */
    private JSBridgeRequest parseJSBridge(String uriString) {
        JSBridgeRequest request = null;
        try {
            if (!TextUtils.isEmpty(uriString) && uriString.startsWith(BRIDGE_PREFIX)) {
                request = new JSBridgeRequest();
                Uri jsUri = Uri.parse(uriString);
                request.setClassName(jsUri.getHost());
                request.setPort(String.valueOf(jsUri.getPort()));
                request.setParm(new JSONObject(jsUri.getQuery()));
                String path = jsUri.getPath();
                if (!TextUtils.isEmpty(path)) {
                    request.setMethodName(path.replace("/", ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }

    /**
     * 根据前端指定的jsbridge类名找到该类,并获得其提供的方法
     * @param clazz
     */
    private void regitstJSBridge(Class<IBridge> clazz){
        String exposedName = clazz.getSimpleName();
        if(!exposedMethods.containsKey(exposedName)){
            try {
                exposedMethods.put(exposedName, getAllMethod(clazz));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得某个jsbridge类中所有公开方法
     * @param clazz   具体的jsbridge类
     */
    private HashMap<String,Method> getAllMethod(Class<IBridge> clazz){
        HashMap<String,Method> methods = new HashMap<>();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            String name;
            if(method.getModifiers() != Modifier.PUBLIC || (name = method.getName()) == null){
                continue;
            }
            Class[] parameters = method.getParameterTypes();
            if(parameters != null && parameters.length == 3){
                if(parameters[0] == WebView.class && parameters[1] == JSONObject.class && parameters[2] == JsCallback.class){
                    methods.put(name , method);
                }
            }
        }
        return methods;
    }

    /**
     * 通过反射得到该类的实例,并执行前端需要的js方法
     * @param webview
     * @param classImpl    jsbridge类的class对象
     * @param request      封装的js请求model
     */
    private void callJava(WebView webview , Class<IBridge> classImpl , JSBridgeRequest request){
        if(request == null)
            return;
        String className = request.getClassName();
        if (exposedMethods.containsKey(request.getClassName())) {
            HashMap<String, Method> methods = exposedMethods.get(className);
            String methodName = request.getMethodName();
            if (methods != null && methods.size() > 0 && methods.containsKey(methodName)) {
                Method method = methods.get(methodName);
                try {
                    method.invoke(classImpl.newInstance(), webview, request.getParm(), new JsCallback(webview, request.getPort()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 释放资源
     * 游览器页面关闭时调用
     */
    public void release(){
        exposedMethods.clear();
    }
}
