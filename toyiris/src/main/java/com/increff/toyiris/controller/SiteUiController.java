package com.increff.toyiris.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SiteUiController {
    @Value("${app.baseUrl}")
    private String baseUrl;

    @RequestMapping(value = "/home")
    public ModelAndView home() {
        return getMav("home.html");
    }


    @RequestMapping(value = "/input")
    public ModelAndView input() {
        return getMav("input.html");
    }

    @RequestMapping(value = "/report")
    public ModelAndView report() {
        return getMav("report.html");
    }

    private ModelAndView getMav(String page) {
        ModelAndView mav = new ModelAndView(page);
        mav.addObject("baseUrl", baseUrl);
        return mav;
    }
}
