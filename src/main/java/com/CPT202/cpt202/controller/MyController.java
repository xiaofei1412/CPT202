package com.CPT202.cpt202.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        System.out.println("hello springboot!");
        return "hello springboot!";
    }
}
