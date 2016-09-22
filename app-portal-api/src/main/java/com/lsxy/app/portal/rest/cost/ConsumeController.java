package com.lsxy.app.portal.rest.cost;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.consume.enums.ConsumeCode;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消费记录
 * Created by zhangxb on 2016/7/8.
 */
@RequestMapping("/rest/consume")
@RestController
public class ConsumeController extends AbstractRestController {
    @Autowired
    ConsumeService consumeService;
    /**
     * 获取分页数据
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/page")
    public RestResponse pageList(Integer pageNo , Integer pageSize,String startTime,String endTime){
        String userName = getCurrentAccountUserName();
        Page<Consume> page =  consumeService.pageList(userName,pageNo,pageSize,startTime,endTime);
        changeTypeToChinese(page.getResult());
        return RestResponse.success(page);
    }

    /**
     * 将消费类型转换为中文，运用枚举
     * @param consumes
     */
    private void changeTypeToChinese(List<Consume> consumes){
        for (Consume consume:consumes){
            String type = consume.getType();
            try{
                ConsumeCode consumeCode = ConsumeCode.valueOf(type);
                consume.setType(consumeCode.getName());
            }catch(Exception e){
//                e.printStackTrace();
                consume.setType("未知项目");
            }
        }
    }

}
