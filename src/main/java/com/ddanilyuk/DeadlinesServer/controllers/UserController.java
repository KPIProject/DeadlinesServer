package com.ddanilyuk.DeadlinesServer.controllers;

import com.ddanilyuk.DeadlinesServer.repositories.ProjectRepository;
import com.ddanilyuk.DeadlinesServer.repositories.UserRepository;
import com.ddanilyuk.DeadlinesServer.extensions.SuccessException;
import com.ddanilyuk.DeadlinesServer.extensions.ServiceException;
import com.ddanilyuk.DeadlinesServer.model.Project;
import com.ddanilyuk.DeadlinesServer.model.User;
import com.ddanilyuk.DeadlinesServer.model.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Контролер юзерів
 */
@RestController
public class UserController {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserController(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }


    /**
     * Версія для Debug
     * Вивід усіх юзерів
     * @return userRepository.findAll() - всіх юзерів з uuid
     */
    @GetMapping("/all")
    @JsonView(Views.usersViewDebugVersion.class)
    public List<User> all() {
        return userRepository.findAll();
    }

    /**
     * Реєєстрація
     * @param body тіло запиту з формою реєєстрації
     * @return userRepository.save(user) - збереження юзера
     */
    @PostMapping("/registration")
    @JsonView(Views.loginView.class)
    public User registration(@RequestBody Map<String, String> body) {
        String userFirstName = body.get("userFirstName");
        String userSecondName = body.get("userSecondName");
        String username = body.get("username");
        String password = body.get("password");

        if (userFirstName == null || userFirstName.equals("")) {
            throw new ServiceException("Invalid userFirstName");
        } else if (userSecondName == null || userSecondName.equals("")) {
            throw new ServiceException("Invalid userSecondName");
        } else if (username == null || username.equals("")) {
            throw new ServiceException("Invalid username");
        } else if (password == null || password.equals("")) {
            throw new ServiceException("Invalid password");
        } else {
            Optional<User> isExistUser = userRepository.findByUsername(username);

            if (!isExistUser.isPresent()) {
                User user = new User(userFirstName, userSecondName, username, password);
                user.setPassword(passwordEncoder.encode(user.getPassword()));

                Date dateNow = new Date();
                user.setUserCreationTime(dateNow.getTime());

                return userRepository.save(user);
            } else {
                throw new ServiceException("User is already exist");
            }
        }
    }

    /**
     * Форма для входу
     * @param body тіло запиту з формою для входу
     * @return user - юзер що увійшов
     */
    @PostMapping("/login")
    @JsonView(Views.loginView.class)
    public User login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.equals("")) {
            throw new ServiceException("Invalid username");
        } else if (password == null || password.equals("")) {
            throw new ServiceException("Invalid password");
        } else {
            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (passwordEncoder.matches(password, user.getPassword())) {
                    return user;
                } else {
                    throw new ServiceException("Password is wrong");
                }
            } else {
                throw new ServiceException("User not found");
            }
        }
    }

    /**
     * Отримання інформацію про юзера
     * @param uuid унікальний id юзера
     * @return optionalUser.get() - юзер про якого хочемо отримати інформацію
     */

    @GetMapping("{uuid}/details")
    @JsonView(Views.loginView.class)
    public User details(@PathVariable String uuid) {
        Optional<User> optionalUser = userRepository.findUserByUuid(UUID.fromString(uuid));
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new ServiceException("User not found");
        }
    }

    /**
     * Знаходження юзера по username
     * @param username username користувача якого шукаємо
     * @return optionalUsers.get() - юзер якого знайшли
     */

    @GetMapping("findByUsername/{username}")
    @JsonView(Views.defaultView.class)
    public List<User> findByUsername(@PathVariable String username) {
        Optional<List<User>> optionalUsers = userRepository.findAllByUsername(username);
        if (optionalUsers.isPresent()) {
            return optionalUsers.get();
        } else {
            throw new ServiceException("Users not found");
        }
    }


    /**
     * Переглянути всі проекти у які запросили юзера
     * @param uuid унікальний id юзера
     * @return user.getProjectsInvited() - проекти у які запросили юзера
     */
    @GetMapping("{uuid}/getInvitations")
    @JsonView(Views.projectView.class)
    public List<Project> getInvitations(@PathVariable String uuid) {
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getProjectsInvited();
        } else {
            throw new ServiceException("User not found");
        }
    }

    /**
     * Прийняти запрошення у проект
     * @param uuid унікальний id юзера
     * @param projectID унікальний id проекта
     * @return projectRepository.save(project) - проект до якого прийняли запрошення
     */
    @PostMapping("{uuid}/acceptInvite/{projectID}")
    @JsonView(Views.projectView.class)
    public Project acceptInvite(@PathVariable String uuid, @PathVariable String projectID) {

        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (!userOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else {
            User user = userOptional.get();
            Project project = projectOptional.get();
            List<Project> projects =  user.getProjectsInvited();
            if (projects.contains(project)) {
                project.getProjectUsers().add(user);
                project.getProjectUsersInvited().remove(user);
                return projectRepository.save(project);
            } else {
                throw new ServiceException("You are not invited to this project");
            }
        }
    }

    /**
     * Відхилити запрошення до проекту
     * @param uuid унікальний id юзера
     * @param projectID унікальний id проекта
     * @return SuccessException("Done") - повідомлення про відхилення запрошення
     */
    @PostMapping("{uuid}/rejectInvite/{projectID}")
    @JsonView(Views.projectView.class)
    public String rejectInvite(@PathVariable String uuid, @PathVariable String projectID) {

        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (!userOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else {
            User user = userOptional.get();
            Project project = projectOptional.get();
            List<Project> projects =  user.getProjectsInvited();
            if (projects.contains(project)) {
                project.getProjectUsersInvited().remove(user);
                projectRepository.save(project);
                throw new SuccessException("Done");
            } else {
                throw new ServiceException("You are not invited to this project");
            }
        }
    }

    /**
     * Редагування юзера
     * @param uuid унікальний id юзера
     * @param userEdited username корисувача якого редагуємо
     * @return userRepository.save(user) - збереження юзера
     */
    @Modifying
    @PostMapping("{uuid}/editUser")
    @JsonView(Views.usersView.class)
    public User editUser(@PathVariable String uuid, @RequestBody User userEdited) {

        Optional<User> userToEditOptional = userRepository.findUserByUuid(UUID.fromString(uuid));

        if (!userToEditOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else {
            User user = userToEditOptional.get();

            if (userEdited.getUsername() != null && !userEdited.getUsername().equals("")) {
                user.setUsername(userEdited.getUsername());
            }
            if (userEdited.getUserFirstName() != null && !userEdited.getUserFirstName().equals("")) {
                user.setUserFirstName(userEdited.getUserFirstName());
            }
            if (userEdited.getUserSecondName() != null && !userEdited.getUserSecondName().equals("")) {
                user.setUserSecondName(userEdited.getUserSecondName());
            }
            if (userEdited.getPassword() != null && !userEdited.getPassword().equals("")) {
                user.setPassword(passwordEncoder.encode(userEdited.getPassword()));
            }

            return userRepository.save(user);
        }
    }

    /**
     * Видалення юзера
     * @param uuid унікальний id юзера
     * @return видалення юзера
     */
    @DeleteMapping("{uuid}/deleteUser")
    public String deleteUser(@PathVariable String uuid)  {
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));

        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            throw new SuccessException("Deleted");
        } else {
            throw new ServiceException("User not found");
        }
    }

}