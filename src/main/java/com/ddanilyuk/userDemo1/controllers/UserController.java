package com.ddanilyuk.userDemo1.controllers;

import com.ddanilyuk.userDemo1.extensions.UserExtension;
import com.ddanilyuk.userDemo1.model.Project;
import com.ddanilyuk.userDemo1.model.User;
import com.ddanilyuk.userDemo1.model.Views;
import com.ddanilyuk.userDemo1.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
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


    // Test request (Delete with release)
    @GetMapping("/all")
    @JsonView(Views.usersView.class)
    public List<User> index() {
        return userRepository.findAll();
    }


    @PostMapping("/registration")
    @JsonView(Views.usersView.class)
    public User newUser(@RequestBody Map<String, String> body) {
        String userFirstName = body.get("userFirstName");
        String userSecondName = body.get("userSecondName");
        String username = body.get("username");
        String password = body.get("password");

        if (userFirstName == null || userFirstName.equals("")) {
            throw new UserExtension("Invalid userFirstName");
        } else if (userSecondName == null || userSecondName.equals("")) {
            throw new UserExtension("Invalid userSecondName");
        } else if (username == null || username.equals("")) {
            throw new UserExtension("Invalid username");
        } else if (password == null || password.equals("")) {
            throw new UserExtension("Invalid password");
        } else {
            Optional<User> isExistUser = userRepository.findByUsername(username);

            if (!isExistUser.isPresent()) {
                User user = new User(userFirstName, userSecondName, username, password);
                user.setPassword(passwordEncoder.encode(user.getPassword()));

                Date dateNow = new Date();
                user.setUserCreationTime(dateNow.getTime());

                return userRepository.save(user);
            } else {
                throw new UserExtension("User is already exist");
            }
        }
    }


    @PostMapping("/login")
    @JsonView(Views.usersView.class)
    public User loginUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.equals("")) {
            throw new UserExtension("Invalid username");
        } else if (password == null || password.equals("")) {
            throw new UserExtension("Invalid password");
        } else {
            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (passwordEncoder.matches(password, user.getPassword())) {
                    return user;
                } else {
                    throw new UserExtension("Password is wrong");
                }
            } else {
                throw new UserExtension("User not found");
            }
        }
    }


    @GetMapping("{uuid}/details")
    @JsonView(Views.usersView.class)
    public User index(@PathVariable String uuid) {
        Optional<User> optionalUser = userRepository.findUserByUuid(UUID.fromString(uuid));
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UserExtension("User not found");
        }
    }


    @GetMapping("findByUsername/{username}")
    @JsonView(Views.defaultView.class)
    public List<User> findByUsername(@PathVariable String username) {
        Optional<List<User>> optionalUsers = userRepository.findAllByUsername(username);
        if (optionalUsers.isPresent()) {
            return optionalUsers.get();
        } else {
            throw new UserExtension("Users not found");
        }
    }


    @GetMapping("/getAllUsers")
    @JsonView(Views.defaultView.class)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }





    @Modifying
    @PostMapping("{uuid}/editUser")
    @JsonView(Views.usersView.class)
    public User editUser(@PathVariable String uuid, @RequestBody Map<String, String> body) {
        UUID userToEditUUID = UUID.fromString(uuid);
        String userFirstName = body.get("userFirstName");
        String userSecondName = body.get("userSecondName");
        String username = body.get("username");
        String password = body.get("password");

        Optional<User> userToEditOptional = userRepository.findUserByUuid(userToEditUUID);

        if (!userToEditOptional.isPresent()) {
            throw new UserExtension("UserNotFound");
        } else {
            User user = userToEditOptional.get();
            user.setUserFirstName(userFirstName);
            user.setUserSecondName(userSecondName);
            user.setUsername(username);
//            passwordEncoder.upgradeEncoding(passwordEncoder.encode(password));
//            passwordEncoder.upgradeEncoding(password);
            user.setPassword(passwordEncoder.encode(password));
            return userRepository.save(user);
        }
    }


    @DeleteMapping("{uuid}/deleteUser")
    public String deleteUser(@PathVariable String uuid) {
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));

        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            throw new UserExtension("Deleted");
        } else {
            throw new UserExtension("User not found");
        }
    }

}