package com.lsxy.user.demo;

import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tandy on 16/8/8.
 */
@RestController
public class CallbackRestController {

    @RequestMapping("/callback")
    public RestResponse callback(){
        return RestResponse.success("ok");
    }
}
