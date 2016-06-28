package com.lsxy.app.portal.rest.console.account;

import com.lsxy.app.portal.rest.comm.MobileCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2016/6/24.
 * 安全设置
 */
@Controller
@RequestMapping("/console/account/safety")
public class SafetyController {
    private static final Logger logger = LoggerFactory.getLogger(SafetyController.class);
    private static final String RESULT_SUCESS = "2";//处理结果-成功
    private static final String RESULT_FIAL = "-2";//处理结果-失败
    private static final Integer EDIT_FIAL = -1;//数据库操作，-1表示失败

    /**
     * 安全设置首页
     * @param request
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        SafetyVo safetyVo = (SafetyVo) request.getSession().getAttribute("safetyVo");
        if(safetyVo == null) {

            safetyVo = new SafetyVo("-1","云呼你xx", "1", "userId", new Date(), "18826474526","1","-1","1");
        }
        request.getSession().setAttribute("safetyVo",safetyVo);
        mav.addObject("safetyVo",safetyVo);
        mav.setViewName("/console/account/safety/index");
        return mav;
    }

    /**
     * 修改密码首页
     * @return
     */
    @RequestMapping(value="/index_psw" )
    public ModelAndView indexPsw(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/console/account/safety/edit_psw");
        return mav;
    }

    /**
     *  修改密码方法
     * @return
     */
    @RequestMapping(value="/edit_psw" ,method = RequestMethod.POST)
    public ModelAndView editPsw( ){
        ModelAndView mav = new ModelAndView();
        //TODO 修改数据库数据返回处理结果
        int status = 0;
        //TODO 0修改成功 -1表示失败
        if(status != EDIT_FIAL) {
            mav.addObject("msg", "修改成功！");
        }else{
            mav.addObject("msg", "修改失败！");
        }
        mav.setViewName("/console/account/safety/edit_psw");
        return mav;
    }

    /**
     * 验证密码方法
     * @param oldPws 用户输入的密码
     * @return
     */
    @RequestMapping(value="/validation_psw" ,method = RequestMethod.POST)
    @ResponseBody
    public Map validationPsw(String oldPws ){
       HashMap map = new HashMap();
        //TODO  查询数据库获取当前用户密码
        String pws = "A123456";
        //验证密码
        if(pws.equalsIgnoreCase(oldPws)) {
            map.put("sucess", RESULT_SUCESS);
            map.put("msg", "密码验证通过！");
        }else{
            map.put("sucess", RESULT_FIAL);
            map.put("msg", "密码验证失败！");
        }
        return map;
    }


    /**
     *  绑定手机号码
     * @param mobile 新手机号码
     * @param request
     * @return
     */
    @RequestMapping(value="/edit_mobile" ,method = RequestMethod.POST)
    @ResponseBody
    public Map editMobile(String mobile ,HttpServletRequest request ){
        HashMap map = new HashMap();
        //TODO  对数据修改用户密码，
        int status = 0;//-1表示数据库修改失败
        // 2修改成功 -2表示失败
        if(status!=EDIT_FIAL) {
            SafetyVo safetyVo = (SafetyVo)request.getSession().getAttribute("safetyVo");
            safetyVo.setMobile(mobile);
            safetyVo.setIsMobile("1");
            request.getSession().setAttribute("safetyVo",safetyVo);
            map.put("sucess", RESULT_SUCESS);
            map.put("msg", "新手机绑定成功！");
        }else{
            map.put("sucess", RESULT_FIAL);
            map.put("msg", "新手机绑定失败！");
        }
        //将手机验证码删除
        MobileCodeUtils.removeMobileCodeChecker(request);
        return map;
    }
}
