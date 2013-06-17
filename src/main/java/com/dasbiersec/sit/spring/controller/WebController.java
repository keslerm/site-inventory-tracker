package com.dasbiersec.sit.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController
{
    // place holder right now
    @RequestMapping(value = "/**")
    public String index(ModelMap map)
    {
        map.addAttribute("message", "Welcome!");
        return "hello";
    }
}
