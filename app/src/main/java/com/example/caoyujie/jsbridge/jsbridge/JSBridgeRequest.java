package com.example.caoyujie.jsbridge.jsbridge;

import org.json.JSONObject;

/**
 * Created by caoyujie on 17/3/13.
 * 根据前端请求信息转换成的请求实体类
 */

public class JSBridgeRequest {
    /**
     * native方法名
     */
    private String methodName;
    /**
     * jsbridge的类名
     */
    private String className;
    /**
     * 请求参数
     */
    private JSONObject parm;
    /**
     * 区分回调的标志
     */
    private String port;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public JSONObject getParm() {
        return parm;
    }

    public void setParm(JSONObject parm) {
        this.parm = parm;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
