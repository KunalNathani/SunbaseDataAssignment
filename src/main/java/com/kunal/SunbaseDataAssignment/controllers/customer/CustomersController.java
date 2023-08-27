package com.kunal.SunbaseDataAssignment.controllers.customer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class CustomersController {
    private String message = "Kunal!";
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("message", this.message);
        return "index";
    }
}
