package com.lluviadeideas.jwt.controllers;

import java.security.Principal;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
        Model model, Principal principal, Locale locale) {

        if(principal != null) {
            return "redirect:/";
        }

        if(error != null){
            model.addAttribute("error", "Ha habido un error, comprueba tu usuario y contraseña o contacta con un administrador.");
        }
        
        model.addAttribute("titulo", messageSource.getMessage("text.login.titulo", null, locale));
        return "login";
    }

}