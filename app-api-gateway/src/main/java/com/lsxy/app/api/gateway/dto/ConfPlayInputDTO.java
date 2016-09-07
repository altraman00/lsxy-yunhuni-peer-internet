package com.lsxy.app.api.gateway.dto;

import java.util.List;

import java.io.Serializable;

/**
 * Created by liuws on 2016/8/26.
 */
public class ConfPlayInputDTO implements Serializable {

    private List<String> files;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
