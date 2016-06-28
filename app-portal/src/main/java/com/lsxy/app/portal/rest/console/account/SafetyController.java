package com.lsxy.app.portal.rest.console.account;

import com.lsxy.app.portal.rest.comm.MobileCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static javafx.scene.input.KeyCode.R;
import static javax.swing.text.html.CSS.getAttribute;

/**
 * Created by zhangxb on 2016/6/24.
 * 安全设置
 */
@Controller
@RequestMapping("/console/account/safety")
public class SafetyController {
    private static final Logger logger = LoggerFactory.getLogger(SafetyController.class);

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
    @RequestMapping(value="/index_psw" )
    public ModelAndView index_psw(HttpServletRequest request ){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/console/account/safety/edit_psw");
        return mav;
    }


    @RequestMapping(value="/edit_psw" ,method = RequestMethod.POST)
    public ModelAndView edit_psw(HttpServletRequest request ){
        ModelAndView mav = new ModelAndView();
        //todo 修改数据库数据
        int status = 0;
        //todo 0修改成功 -1表示失败
        if(status==0) {
            mav.addObject("msg", "修改成功！");
        }else{
            mav.addObject("msg", "修改失败！");
        }
        mav.setViewName("/console/account/safety/edit_psw");
        return mav;
    }


    @RequestMapping(value="/validation_psw" ,method = RequestMethod.POST)
    @ResponseBody
    public Map validation_psw(String oldPws ){
       HashMap map = new HashMap();
        //todo  获取当前用户密码
        String pws = "A123456";
        //todo 0修改成功 -1表示失败
        if(pws.equalsIgnoreCase(oldPws)) {
            map.put("sucess", "2");
            map.put("msg", "密码验证通过！");
        }else{
            map.put("sucess", "-2！");
            map.put("msg", "密码验证失败！");
        }
        return map;
    }

    @RequestMapping(value="/edit_mobile" ,method = RequestMethod.POST)
    @ResponseBody
    public Map edit_mobile(String mobile ,HttpServletRequest request ){
        HashMap map = new HashMap();
        //todo  对数据修改用户密码，
        int status = 0;//-1表示数据库修改失败
        // 2修改成功 -2表示失败
        if(status!=-1) {
            SafetyVo safetyVo = (SafetyVo)request.getSession().getAttribute("safetyVo");
            safetyVo.setMobile(mobile);
            safetyVo.setIsMobile("1");
            request.getSession().setAttribute("safetyVo",safetyVo);
            map.put("sucess", "2");
            map.put("msg", "新手机绑定成功！");
        }else{
            map.put("sucess", "-2！");
            map.put("msg", "新手机绑定失败！");
        }
        //将手机验证码删除
        MobileCodeUtils.removeMobileCodeChecker(request);
        return map;
    }
}
