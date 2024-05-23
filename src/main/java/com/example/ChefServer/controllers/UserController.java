package com.example.ChefServer.controllers;

import com.example.ChefServer.entities.*;
import com.example.ChefServer.services.*;
import com.example.ChefServer.services.ImageService.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.io.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${photo.path}")
    private String photo_path;

    @GetMapping("/login")
    public String userLogin(Model model) {
        return "login"; 
    }

    @GetMapping("/register")
    public String userGetRegister(){
        return "register";
    }

    @PostMapping("/register")
    public String userRegister(EUser user, Model model){
        
        if (userService.registerNewUser(user)) {
            model.addAttribute("message","User created!");
            model.addAttribute("status_code","201");
            
            return "redirect:/user/login";
        }
        
        model.addAttribute("message","This name already exists!");
        return "register";
    }

    @PostMapping("/change_photo")
    public String changePhoto(
        @RequestParam(name="load_photo", required = false) MultipartFile photo,
        Model model) 
    throws IOException {
        
        if (userService.authenticatedIsNotAnonymous())
        {
            EUser current = userService.getAuthenticatedUser();
            if (photo != null)
            {
                String fileName = Long.toString(current.getId()) + ".jpg";

                File uploadDir = new File(photo_path);
                if (!uploadDir.exists())
                    uploadDir.mkdir();

                FileUploadUtil.saveFile(photo_path, fileName, photo);

                current.setPhoto_path(photo_path + "/" + fileName);
            }
            else
            {
                current.setPhoto_path(null);
            }
            userService.updateUser(current);
            return "redirect:/profile";
        }
        return "redirect:/error?code=406";
    }

    @GetMapping("/")
    public String profile(Model model) {

        EUser user = userService.getAuthenticatedUser();

        if (user != null) {
            model.addAttribute("user", user);
            return "profile";
        }
        else
        {
            return "redirect:/user/login";
        }
    }

    @PutMapping("/change_password")
    @ResponseBody
    public String admitPassword(
            @RequestParam(name="check", required = true) String predicted_pass,
            @RequestParam(name="newp",required = true) String new_pass)
    {
        if (userService.authenticatedIsNotAnonymous())

            if (passwordEncoder.matches(predicted_pass, userService.getAuthenticatedUser().getPassword())) {
                EUser current_user = userService.getAuthenticatedUser();
                current_user.setPassword(passwordEncoder.encode(new_pass));
                userService.updateUser(current_user);
                return "true";
            }

        return "false";
    }
}
