package org.ahmedukamel.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SpringBootApplication
@EnableScheduling
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @GetMapping
    public ModelAndView homePage() {
        return new ModelAndView("home");
    }

    @GetMapping(value = "privacy-policy")
    public ModelAndView privacyPolicyPage() {
        return new ModelAndView("privacy-policy");
    }
}
