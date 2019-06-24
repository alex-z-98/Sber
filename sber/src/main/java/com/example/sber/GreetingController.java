package com.example.sber;

import com.example.sber.domain.User;
import com.example.sber.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.sql.Date;

@Controller
public class GreetingController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String main(Map<String, Object> model) {
        Iterable<User> users = userRepo.findAll();

        model.put("users", users);

        return "main";
    }

    @PostMapping
    public String add(@RequestParam MultipartFile photo,
                      @RequestParam MultipartFile info,
                      @RequestParam String login,
                      @RequestParam String name,
                      @RequestParam String surname,
                      @RequestParam String address,
                      @RequestParam String birth,
                      Map<String, Object> model) throws IOException {
        User user = new User(login, name, surname, address);

        try {
            user.setBirth(Date.valueOf(birth));
        }
        catch (IllegalArgumentException ex)
        {
        }

        if(!photo.isEmpty())
            user.setPhoto(photo.getBytes());


        byte[] userFileInfo = info.getBytes();
        if (userFileInfo != null)
            user.setInfo(new String(userFileInfo));


        userRepo.save(user);


        Iterable<User> users = userRepo.findAll();

        model.put("users", users);

        return "main";
    }

    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model) {
        Iterable<User> users;

        if (filter != null && !filter.isEmpty()) {
            users = userRepo.findByName(filter);
        } else {
            users = userRepo.findAll();
        }

        model.put("users", users);

        return "main";
    }

    @GetMapping("getInfo")
    public String getInfo(@RequestParam Integer id, Map<String, Object> model){
        User user;
        user=userRepo.findFirstById(id);

        model.put("user", user);

        return "user";
    }

    @PostMapping ("deleteUser")
    public String deleteUser(@RequestParam Integer id, Map<String, Object> model){
        userRepo.delete(userRepo.findFirstById(id));

        Iterable<User> users=userRepo.findAll();

        model.put("users", users);

        return "redirect:/";
    }

    @GetMapping("/getImage")
    public void getImage(@RequestParam(required = false, defaultValue = "") Integer id,
                         HttpServletResponse response) throws IOException
    {
        User user = userRepo.findFirstById(id);;

        if (user.getPhoto() == null)
        {
            byte[] array = Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"\\src\\main\\resources\\images\\defaultImage.jpg"));
            response.setContentType("image/*");
            response.getOutputStream().write(array);
            response.getOutputStream().close();
            return;
        }
        response.setContentType("image/*");
        response.getOutputStream().write(user.getPhoto());
        response.getOutputStream().close();
    }

    @GetMapping("editInfo")
    public String editInfo(@RequestParam Integer id, Map<String, Object> model){
        User user;
        user=userRepo.findFirstById(id);

        model.put("user", user);

        return "editInfo";
    }

    @PostMapping("postChanges")
    public String postChanges(@RequestParam MultipartFile photo,
                      @RequestParam MultipartFile info,
                      @RequestParam String login,
                      @RequestParam String name,
                      @RequestParam String surname,
                      @RequestParam String address,
                      @RequestParam String birth,
                      @RequestParam Integer id,
                      Map<String, Object> model) throws IOException {
        User user = userRepo.findFirstById(id);

        user.setLogin(login);
        user.setName(name);
        user.setSurname(surname);
        user.setAddress(address);

        try {
            user.setBirth(Date.valueOf(birth));
        }
        catch (IllegalArgumentException ex)
        {
        }

        if (!photo.isEmpty())
            user.setPhoto(photo.getBytes());

        if (!info.isEmpty())
            user.setInfo(new String(info.getBytes()));

        userRepo.save(user);

        Iterable<User> users = userRepo.findAll();

        model.put("users", userRepo.findAll());

        return "redirect:/";
    }
}
