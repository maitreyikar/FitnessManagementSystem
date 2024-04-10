
package com.fitwise.fitnessmanagementsystem.Dietician;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.*;


@Controller
@RequestMapping(path = "/dietician")

public class DieticianController {
    @Autowired
    private DieticianRepository dieticianRepository;

    public DieticianController(DieticianRepository dieticianRepository){
        this.dieticianRepository = dieticianRepository;
    }

    @GetMapping(path = "/login")
    public String getLoginPage(Model model) {
        model.addAttribute("message", "");
        Dietician dietician1 = new Dietician(1, "Nikita", "nikita@mabel.com", "password123", 1234567890L, "Oncology");
        dieticianRepository.save(dietician1);
        return "dietician_login";
    }
    
    @PostMapping(path = "/login")
    public String validateDietician(Model model, @RequestParam("email") String enteredEmail, @RequestParam("password") String enteredPassword, HttpServletRequest request){
        
        List<Dietician> existingDieticians = dieticianRepository.findByDieticianEmail(enteredEmail);

        if(existingDieticians.size() == 1 && (existingDieticians.get(0)).getDieticianPassword().equals(enteredPassword)){
            HttpSession session = request.getSession();
            session.setAttribute("loggedInDietician", existingDieticians.get(0));
            return "redirect:/dietician/home";
        }
        model.addAttribute("message", "Invalid email address and/or password");
        return "dietician_login";
    }

}
