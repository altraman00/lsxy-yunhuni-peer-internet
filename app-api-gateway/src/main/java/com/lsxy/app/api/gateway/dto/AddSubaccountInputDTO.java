package com.lsxy.app.api.gateway.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 号码绑定子账号
 * Created by liuws on 2016/8/24.
 */
public class AddSubaccountInputDTO extends CommonDTO{

    @NotNull
    private String callbackUrl;

    @Size(max = 128)
    private String remark;


}
