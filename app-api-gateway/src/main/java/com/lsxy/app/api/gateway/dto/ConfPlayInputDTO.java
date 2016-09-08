package com.lsxy.app.api.gateway.dto;

import java.util.List;

/**
 * Created by liuws on 2016/8/26.
 */
public class ConfPlayInputDTO extends CommonDTO{

    private List<String> files;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
