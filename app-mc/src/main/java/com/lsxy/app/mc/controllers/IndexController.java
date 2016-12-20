package com.lsxy.app.mc.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by tandy on 16/11/19.
 */
@Controller
@RequestMapping("/admin")
public class IndexController {


    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "greeting";
    }
}
