package com.luxStock.demo.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexController {

    @GetMapping("/")
    public String viewIndexPage(){
        return "index";
    }
    @GetMapping("/login")
    public String viewLogigPage() {
        return "login";
    }

    @GetMapping("/403")
    public String viewAccessDeniedPage() {
        return "403";
    }
}
