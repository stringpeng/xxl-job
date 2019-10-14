package com.xxl.job.executor.service.dto;

import java.io.Serializable;

/**
 * @author StringPeng
 * Created on 2019/6/22
 */
public class GatewayJobDTO implements Serializable {
    static final long serialVersionUID = 1L;

    String url;
    String funcId;
    String action;
    String begin;
    String end;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}