package com.grouppage.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RedirectController {

    @GetMapping("/")
    public ModelAndView getIndex(){
        return new ModelAndView("index");
    }
//
//    @GetMapping(value = "/**/{path:[^\\.]*}")
//    public String getRoot(){
//        return "forward:/";
//    }
}
