package com.ddanilyuk.userDemo1.controllers;

import com.ddanilyuk.userDemo1.model.Deadline;
import com.ddanilyuk.userDemo1.model.Project;
import com.ddanilyuk.userDemo1.model.User;
import com.ddanilyuk.userDemo1.repositories.ProjectRepository;
import com.ddanilyuk.userDemo1.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("{uuid}/allProjects")
    public List<Project> index(@PathVariable String uuid) {
        User user = userRepository.findUserByUuid(UUID.fromString(uuid));
        return user.getProjects();
    }


    @PostMapping("{uuid}/addProject")
    public Project newUser(@PathVariable String uuid, @RequestBody Map<String, String> body) {

        UUID stringUUID = UUID.fromString(uuid);

        System.out.println(userRepository.findAll());

        User user = userRepository.findUserByUuid(stringUUID);


        String project_name = body.get("project_name");
        String project_description = body.get("project_description");

        Project project = new Project(project_name, project_description, user);

        return projectRepository.save(project);
    }

    @PostMapping("{uuid}/{projectID}/addDeadline")
    public Project newDeadline(@PathVariable String uuid, @PathVariable String projectID, @RequestBody Map<String, String> body) {

        UUID stringUUID = UUID.fromString(uuid);
        User user = userRepository.findUserByUuid(stringUUID);

        List<Project> projects = user.getProjects();

        Project projectToAdd = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (projects.contains(projectToAdd)) {
            String deadline_name = body.get("deadline_name");
            String deadline_description = body.get("deadline_description");
            Deadline deadline = new Deadline(deadline_name, deadline_description);
            deadline.setProject(projectToAdd);
            projectToAdd.getDeadlines().add(deadline);
            return projectRepository.save(projectToAdd);
        }
        return null;

    }


}
