package com.lsxy.app.api.gateway.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by liuws on 2016/8/26.
 */
public class ConfPlayInputDTO extends CommonDTO{

    @NotNull
    private List<String> files;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
