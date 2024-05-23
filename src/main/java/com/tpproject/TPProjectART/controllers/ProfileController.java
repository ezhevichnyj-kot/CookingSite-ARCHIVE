package com.tpproject.TPProjectART.controllers;

import com.tpproject.TPProjectART.entities.*;
import com.tpproject.TPProjectART.services.*;
import com.tpproject.TPProjectART.services.ImageService.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.io.*;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Value("${photo.path}")
    private String photo_path;

    @PostMapping("/profile")
    public String setPhoto(@RequestParam(name="load_photo", required = false) MultipartFile photo, Model model) throws IOException {
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

    @GetMapping("/profile")
    public String profile(Model model) {

        EUser user = userService.getAuthenticatedUser();

        if (user != null) {
            model.addAttribute("user", user);
            return "profile";
        }
        else
        {
            return "redirect:/login";
        }
    }

    @CrossOrigin
    @GetMapping("/admit_password")
    @ResponseBody
    public String admitPassword(@RequestParam(name="check", required = true) String predicted_pass, @RequestParam(name="newp",required = false) String new_pass) {
        if (new_pass == null)
            return "false";

        if (userService.authenticatedIsNotAnonymous())
            if (userService.getAuthenticatedUser().getPassword().equals(predicted_pass)) {
                EUser current = userService.getAuthenticatedUser();
                current.setPassword(new_pass);
                userService.updateUser(current);
                return "true";
            }
            else
            {
                return "true";
            }

        return "false";
    }
}
