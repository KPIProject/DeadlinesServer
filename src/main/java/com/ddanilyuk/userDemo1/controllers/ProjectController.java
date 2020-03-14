package com.ddanilyuk.userDemo1.controllers;

import com.ddanilyuk.userDemo1.extensions.UserExtension;
import com.ddanilyuk.userDemo1.model.Deadline;
import com.ddanilyuk.userDemo1.model.Project;
import com.ddanilyuk.userDemo1.model.User;
import com.ddanilyuk.userDemo1.repositories.DeadlineRepository;
import com.ddanilyuk.userDemo1.repositories.ProjectRepository;
import com.ddanilyuk.userDemo1.repositories.UserRepository;
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


    @PostMapping("{uuid}/createProject")
    public Project createProject(@PathVariable String uuid, @RequestBody Map<String, String> body) {

        UUID stringUUID = UUID.fromString(uuid);

        User user = userRepository.findUserByUuid(stringUUID);


        String project_name = body.get("project_name");
        String project_description = body.get("project_description");

        Project project = new Project(project_name, project_description, user);

        return projectRepository.save(project);
    }

    @PostMapping("{uuidOwner}/{projectID}/addUserToProject/{uuidUserToAdd}")
    public Project newUserToProject(@PathVariable String uuidOwner, @PathVariable String uuidUserToAdd, @PathVariable String projectID, @RequestBody Map<String, String> body) {

        User userToAdd = userRepository.findUserByUuid(UUID.fromString(uuidUserToAdd));
        User userOwner = userRepository.findUserByUuid(UUID.fromString(uuidOwner));

//        List<Project> userOwnerProjects = userOwner.getProjects();

        Project project = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (project.getProjectCreatorUuid().equals(userOwner.getUuid())) {

            project.getProjectActiveUsersId().add(userToAdd.getUuid());
            List<Project> projects = userToAdd.getProjects();
            projects.add(project);
            userToAdd.setProjects(projects);
            userRepository.save(userToAdd);

            return projectRepository.save(project);
        }

        throw new UserExtension("User owner not found");
//        return null;


    }

    @PostMapping("{uuid}/{projectID}/addDeadline")
    public Project newDeadlineToProject(@PathVariable String uuid, @PathVariable String projectID, @RequestBody Map<String, String> body) {

        UUID stringUUID = UUID.fromString(uuid);
        User user = userRepository.findUserByUuid(stringUUID);

        Project projectToAdd = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (projectToAdd.getProjectCreatorUuid().equals(user.getUuid())) {
            String deadline_name = body.get("deadline_name");
            String deadline_description = body.get("deadline_description");
            Deadline deadline = new Deadline(deadline_name, deadline_description);
            deadline.setProject(projectToAdd);
            deadline.setDeadlineProjectId(projectToAdd.getProjectId());
            projectToAdd.getDeadlines().add(deadline);
            return projectRepository.save(projectToAdd);
        }


        throw new UserExtension("Deadline not found");

//        return null;


    }

    @PostMapping("{uuidOwner}/{projectID}/{deadlineId}/addExecutor/{uuidUserToAdd}")
    public Project addExecutorToProject(@PathVariable String uuidOwner,
                                        @PathVariable String projectID,
                                        @PathVariable String deadlineId,
                                        @PathVariable String uuidUserToAdd) {

        User userToAdd = userRepository.findUserByUuid(UUID.fromString(uuidUserToAdd));
        User userOwner = userRepository.findUserByUuid(UUID.fromString(uuidOwner));

        Project project = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (project.getProjectCreatorUuid().equals(userOwner.getUuid())) {

            List<Deadline> projectDeadlines = project.getDeadlines();
            for (Deadline deadline : projectDeadlines) {
                if (deadline.getDeadlineId() == Integer.parseInt(deadlineId)) {
                    deadline.getDeadlineExecutorsUuid().add(userToAdd.getUuid());
                    return projectRepository.save(project);
                }
            }
            throw new UserExtension("Deadline not found");
//            return null;

        }
        throw new UserExtension("User owner not found");
//        return null;


    }


}
