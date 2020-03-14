package com.ddanilyuk.userDemo1.controllers;

//import com.ddanilyuk.userDemo1.extensions.UserExtension;
import com.ddanilyuk.userDemo1.extensions.UserExtension;
import com.ddanilyuk.userDemo1.model.Project;
import com.ddanilyuk.userDemo1.model.User;
import com.ddanilyuk.userDemo1.model.Views;
import com.ddanilyuk.userDemo1.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
//@RequestMapping("/main")
public class UserController {

    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Test request
    @GetMapping("/all")
    @JsonView(Views.usersView.class)
    public List<User> index() {
        return userRepository.findAll();
    }


    @PostMapping("/registration")
//    @JsonView(Views.userWithoutProjectsAndPassword.class)
    @JsonView(Views.usersView.class)
    public User newUser(@RequestBody Map<String, String> body) {
        String userFirstName = body.get("user_first_name");
        String userSecondName = body.get("user_second_name");
        String username = body.get("username");
        String password = body.get("password");

//        Optional<User> isExistUser = userRepository.findByUsername(username).orElseThrow(() -> new UserExtension(username));
        Optional<User> isExistUser = userRepository.findByUsername(username);

        if (!isExistUser.isPresent()) {
            User user = new User(userFirstName, userSecondName, username, password);

            user.setPassword(passwordEncoder.encode(user.getPassword()));

//            userRepository.save(user);
//            return String.valueOf(user.getUuid());
            return userRepository.save(user);
        } else {
//            return "User is already exist";
            throw new UserExtension("User is already exist");
//            return null;

        }
    }

    @PostMapping("/login")
    @JsonView(Views.usersView.class)
    public String loginUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return String.valueOf(user.getUuid());
            } else {
//                return "Password is wrong";

                throw new UserExtension("Password is wrong");

//                return null;

            }
        } else {
//            return "User is not exist";
            throw new UserExtension("User is not exist");
//            return null;

        }
    }

    @GetMapping("{uuid}/details")
    @JsonView(Views.usersView.class)
    public User index(@PathVariable String uuid) {
        return userRepository.findUserByUuid(UUID.fromString(uuid));
    }


}