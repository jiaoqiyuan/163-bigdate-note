package com.bigdata.etl.mr;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LogBeanWritable implements Writable {

    private String activeName;
    private String sessionID;
    private long timeTag;
    private String ip;
    private String deviceID;
    private String reqUrl;
    private String userID;
    private String productID;
    private String orderID;

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public long getTimeTag() {
        return timeTag;
    }

    public void setTimeTag(long timeTag) {
        this.timeTag = timeTag;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void write(DataOutput out) throws IOException {
        WritableUtils.writeString(out, activeName);
        WritableUtils.writeString(out, sessionID);
        out.writeLong(timeTag);
        WritableUtils.writeString(out, ip);
        WritableUtils.writeString(out, deviceID);
        WritableUtils.writeString(out, reqUrl);
        WritableUtils.writeString(out, userID);
        WritableUtils.writeString(out, productID);
        WritableUtils.writeString(out, orderID);
    }

    public void readFields(DataInput in) throws IOException {
        activeName = WritableUtils.readString(in);
        sessionID = WritableUtils.readString(in);
        timeTag = in.readLong();
        ip = WritableUtils.readString(in);
        deviceID = WritableUtils.readString(in);
        reqUrl = WritableUtils.readString(in);
        userID = WritableUtils.readString(in);
        productID = WritableUtils.readString(in);
        orderID = WritableUtils.readString(in);
    }

    public String asJsonString() {
        JSONObject json = new JSONObject();
        json.put("active_name", activeName);
        json.put("session_id", sessionID);
        json.put("time_tag", timeTag);
        json.put("ip", ip);
        json.put("device_id", deviceID);
        json.put("req_url", reqUrl);
        json.put("user_id", userID);
        json.put("product_id", productID);
        json.put("order_id", orderID);

        return json.toJSONString();
    }
}
