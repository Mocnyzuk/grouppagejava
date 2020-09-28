package com.grouppage.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RedirectController {

    @GetMapping("/")
    public String getIndex(){
        return "index.html";
    }
//
//    @GetMapping(value = "/**/{path:[^\\.]*}")
//    public String getRoot(){
//        return "forward:/";
//    }
}
