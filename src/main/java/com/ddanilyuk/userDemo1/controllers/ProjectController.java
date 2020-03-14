package com.ddanilyuk.userDemo1.controllers;

import com.ddanilyuk.userDemo1.extensions.UserExtension;
import com.ddanilyuk.userDemo1.model.Deadline;
import com.ddanilyuk.userDemo1.model.Project;
import com.ddanilyuk.userDemo1.model.User;
import com.ddanilyuk.userDemo1.model.Views;
import com.ddanilyuk.userDemo1.repositories.DeadlineRepository;
import com.ddanilyuk.userDemo1.repositories.ProjectRepository;
import com.ddanilyuk.userDemo1.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

//import com.monitorjbl.json.JsonView;
import static com.monitorjbl.json.Match.match;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.monitorjbl.json.Match.match;

@RestController
@RequestMapping
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final DeadlineRepository deadlineRepository;


    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository, DeadlineRepository deadlineRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.deadlineRepository = deadlineRepository;
    }


    @GetMapping("{uuid}/allProjects")
    @JsonView(Views.projectView.class)
    public List<Project> allProjects(@PathVariable String uuid) {
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Project> projects = user.getProjectsCreated();
            projects.addAll(user.getProjectsAppended());
            return projects;
        } else {
            throw new UserExtension("User is not found");
        }
    }


    @PostMapping("{uuid}/createProject")
    @JsonView(Views.projectView.class)
    public Project createProject(@PathVariable String uuid, @RequestBody Map<String, String> body) {

        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            String project_name = body.get("project_name");
            String project_description = body.get("project_description");

            Project project = new Project(project_name, project_description, user);

            return projectRepository.save(project);
        } else {
            throw new UserExtension("User is not found");
        }

    }

    @PostMapping("{uuidOwner}/{projectID}/addUserToProject/{uuidUserToAdd}")
    @JsonView({Views.projectView.class})
    public Project newUserToProject(@PathVariable String uuidOwner, @PathVariable String uuidUserToAdd, @PathVariable String projectID, @RequestBody Map<String, String> body) {

        Optional<User> userToAddOptional = userRepository.findUserByUuid(UUID.fromString(uuidUserToAdd));
        Optional<User> userOwnerOptional = userRepository.findUserByUuid(UUID.fromString(uuidOwner));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (!userToAddOptional.isPresent()) {
            throw new UserExtension("User to add not found");
        } else if (!userOwnerOptional.isPresent()) {
            throw new UserExtension("User owner not found");
        } else if (!projectOptional.isPresent()) {
            throw new UserExtension("Project not found");
        } else {
            User userToAdd = userToAddOptional.get();
            User userOwner = userOwnerOptional.get();
            Project project = projectOptional.get();

            if (project.getProjectCreatorUuid().equals(userOwner.getUuid())) {

                if (project.getProjectUsers().contains(userToAdd)) {
                    throw new UserExtension("User is already in this project");
                } else {
                    project.getProjectActiveUsersUuid().add(userToAdd.getUuid());
                    project.getProjectUsers().add(userToAdd);
                }

                List<Project> projects = userToAdd.getProjectsAppended();
                projects.add(project);
                userToAdd.setProjectsAppended(projects);
                userRepository.save(userToAdd);

                return projectRepository.save(project);
            } else  {
                throw new UserExtension("Invalid project owner");
            }
        }


    }

    @PostMapping("{uuid}/{projectID}/addDeadline")
    @JsonView({Views.projectView.class})
    public Project newDeadlineToProject(@PathVariable String uuid, @PathVariable String projectID, @RequestBody Map<String, String> body) {

        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));

        Optional<Project> projectToAddOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (!userOptional.isPresent()) {
            throw new UserExtension("User not found");
        } else if (!projectToAddOptional.isPresent()) {
            throw new UserExtension("Project not found");
        } else {
            User user = userOptional.get();

            Project projectToAdd = projectToAddOptional.get();

            if (projectToAdd.getProjectCreatorUuid().equals(user.getUuid())) {
                String deadline_name = body.get("deadline_name");
                String deadline_description = body.get("deadline_description");
                Deadline deadline = new Deadline(deadline_name, deadline_description);
                deadline.setProject(projectToAdd);
                deadline.setDeadlineProjectId(projectToAdd.getProjectId());
                projectToAdd.getDeadlines().add(deadline);
                return projectRepository.save(projectToAdd);
            } else {
                throw new UserExtension("Invalid project owner");
            }
        }

//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            Project projectToAdd = projectRepository.findByProjectId(Integer.parseInt(projectID));
//
//            if (projectToAdd.getProjectCreatorUuid().equals(user.getUuid())) {
//                String deadline_name = body.get("deadline_name");
//                String deadline_description = body.get("deadline_description");
//                Deadline deadline = new Deadline(deadline_name, deadline_description);
//                deadline.setProject(projectToAdd);
//                deadline.setDeadlineProjectId(projectToAdd.getProjectId());
//                projectToAdd.getDeadlines().add(deadline);
//                return projectRepository.save(projectToAdd);
//            }
//
//            throw new UserExtension("Project not found");
//
//        } else {
//            throw new UserExtension("User not found");
//        }


    }

    @PostMapping("{uuidOwner}/{projectID}/{deadlineId}/addExecutor/{uuidUserToAdd}")
    @JsonView({Views.projectView.class})
    public Project addExecutorToProject(@PathVariable String uuidOwner,
                                        @PathVariable String projectID,
                                        @PathVariable String deadlineId,
                                        @PathVariable String uuidUserToAdd) {

//        User userToAdd = userRepository.findUserByUuid(UUID.fromString(uuidUserToAdd));
//        User userOwner = userRepository.findUserByUuid(UUID.fromString(uuidOwner));
//
//        Project project = projectRepository.findByProjectId(Integer.parseInt(projectID));
//
//        if (project.getProjectCreatorUuid().equals(userOwner.getUuid())) {
//
//            List<Deadline> projectDeadlines = project.getDeadlines();
//            for (Deadline deadline : projectDeadlines) {
//                if (deadline.getDeadlineId() == Integer.parseInt(deadlineId)) {
//                    deadline.getDeadlineExecutorsUuid().add(userToAdd.getUuid());
//                    return projectRepository.save(project);
//                }
//            }
//            throw new UserExtension("Deadline not found");
////            return null;
//
//        }
//        throw new UserExtension("User owner not found");
//        return null;


        Optional<User> userToAddOptional = userRepository.findUserByUuid(UUID.fromString(uuidUserToAdd));
        Optional<User> userOwnerOptional = userRepository.findUserByUuid(UUID.fromString(uuidOwner));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (!userToAddOptional.isPresent()) {
            throw new UserExtension("User to add not found");
        } else if (!userOwnerOptional.isPresent()) {
            throw new UserExtension("User owner not found");
        } else if (!projectOptional.isPresent()) {
            throw new UserExtension("Project not found");
        } else {
            User userToAdd = userToAddOptional.get();
            User userOwner = userOwnerOptional.get();
            Project project = projectOptional.get();

            if (project.getProjectCreatorUuid().equals(userOwner.getUuid())) {

                if (project.getProjectUsers().contains(userToAdd)) {
                    List<Deadline> projectDeadlines = project.getDeadlines();
                    for (Deadline deadline : projectDeadlines) {
                        if (deadline.getDeadlineId() == Integer.parseInt(deadlineId)) {
                            deadline.getDeadlineExecutorsUuid().add(userToAdd.getUuid());

                            return projectRepository.save(project);
                        }
                    }
                    throw new UserExtension("Deadline not found");
                } else {
                    throw new UserExtension("User to add is not in this project");
                }
            } else {
                throw new UserExtension("Invalid project owner");
            }
        }


    }

    @GetMapping("deadlineDetail/{id}")
    @JsonView({Views.deadlinesDetailView.class})
    public Deadline findDeadline(@PathVariable String id) {
        Optional<Deadline> deadlineOptional = deadlineRepository.findById(Integer.parseInt(id));

        if (deadlineOptional.isPresent()) {

            return deadlineOptional.get();
        } else {
            throw new UserExtension("Deadline is not found");
        }
    }


}
