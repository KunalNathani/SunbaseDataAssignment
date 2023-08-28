package com.kunal.SunbaseDataAssignment.controllers.customer;

import com.kunal.SunbaseDataAssignment.models.Customer;
import com.kunal.SunbaseDataAssignment.services.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
public class CustomersController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private HttpSession httpSession;

    @GetMapping("/login")
    public String login() {
        if (httpSession.getAttribute("token") != null) {
            return "redirect:/customers/all";
        }
        return "login";
    }

    @PostMapping("/login")
    public String verifyCredentials(@RequestParam String login_id, @RequestParam String password) {
        String token = customerService.login(login_id, password);
        if (! token.equals(null)) {
            httpSession.setAttribute("token", token);
            return "redirect:/customers/all";
        }
        return "redirect:/login";
    }
    @GetMapping("/customers/all")
    public String index(Model model) {
        model.addAttribute("customers", customerService.fetchAllRecords(this.getToken()));
        return "all-customers";
    }


    @PostMapping("/customers/store")
    public String store(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes) {
        boolean status = customerService.addCustomer(customer, this.getToken());
        redirectAttributes.addFlashAttribute("status", status ? "success" : "danger");
        redirectAttributes.addFlashAttribute("message", status ? "Created new Customer!" : "Error while Creating Customer");
        return "redirect:/customers/all";
    }

    @GetMapping("/customers/create")
    public String create() {
        return "create-customer";
    }

    @GetMapping("/customers/{uuid}/edit")
    public String edit(@PathVariable String uuid, Model model) {
        Customer c = customerService.getCustomerByUUID(uuid, this.getToken());
        if(c == null) return "redirect:/customers/all";
        model.addAttribute("customer", c);
        return "update-customer";
    }

    @PostMapping("customers/{uuid}/update")
    public String update(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes) {
        boolean status = customerService.updateCustomer(customer, this.getToken());
        redirectAttributes.addFlashAttribute("status", status ? "success" : "danger");
        redirectAttributes.addFlashAttribute("message", status ? "Updated!" : "Error while Updating Customer");
        return "redirect:/customers/all";
    }

    @PostMapping("/customers/{uuid}/delete")
    public String destroy(@PathVariable String uuid, RedirectAttributes redirectAttributes) {
        boolean status = customerService.deleteCustomer(uuid, this.getToken());
        redirectAttributes.addFlashAttribute("status", status ? "success" : "danger");
        redirectAttributes.addFlashAttribute("message", status ? "Deleted!" : "Error while Deleting Customer");
        return "redirect:/customers/all";
    }

    @ModelAttribute("token")
    public String getToken() {
        return (String) httpSession.getAttribute("token");
    }
}
