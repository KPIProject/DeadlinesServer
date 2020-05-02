package com.ddanilyuk.userDemo1.controllers;

import com.ddanilyuk.userDemo1.extensions.ServiceException;
import com.ddanilyuk.userDemo1.extensions.SuccessException;
import com.ddanilyuk.userDemo1.model.*;
import com.ddanilyuk.userDemo1.repositories.DeadlineRepository;
import com.ddanilyuk.userDemo1.repositories.ProjectRepository;
import com.ddanilyuk.userDemo1.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            throw new ServiceException("User is not found");
        }
    }


    @PostMapping("{uuid}/createProject")
    @JsonView(Views.projectView.class)
    public Project createProject(@PathVariable String uuid, @Valid @RequestBody ComplaintProject complaintProject) {

        Project project = complaintProject.project;
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));

        if (!userOptional.isPresent()) {
            throw new ServiceException("User is not found");
        } else if (project.getProjectDescription() == null) {
            throw new ServiceException("Invalid projectDescription");
        } else if (project.getProjectName() == null || project.getProjectName().equals("")) {
            throw new ServiceException("Invalid projectName");
        } else {
            if (project.getProjectCreationTime() == 0) {
                Date dateNow = new Date();
                project.setProjectCreationTime(dateNow.getTime());
            }
            User user = userOptional.get();
            project.setProjectOwner(user);

            projectRepository.save(project);

            List<String> usersToAdd = complaintProject.usersToAdd;
            for (String userToAdd : usersToAdd) {
                if (!userRepository.findUserByUuid(UUID.fromString(userToAdd)).isPresent()) {
                    projectRepository.delete(project);
                    throw new ServiceException("User to add is not found");
                } else if (userToAdd.equals(user.getUuid().toString())) {
                    projectRepository.delete(project);
                    throw new ServiceException("User owner cant be invited to project");
                }
                addUserToProject(uuid, userToAdd, String.valueOf(project.getProjectId()));
            }

            return project;

        }
    }


    @PostMapping("{uuidOwner}/{projectID}/addUserToProject/{uuidUserToAdd}")
    @JsonView({Views.projectView.class})
    public Project addUserToProject(@PathVariable String uuidOwner, @PathVariable String uuidUserToAdd, @PathVariable String projectID) {

        Optional<User> userToAddOptional = userRepository.findUserByUuid(UUID.fromString(uuidUserToAdd));
        Optional<User> userOwnerOptional = userRepository.findUserByUuid(UUID.fromString(uuidOwner));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (!userToAddOptional.isPresent()) {
            throw new ServiceException("User to add not found");
        } else if (!userOwnerOptional.isPresent()) {
            throw new ServiceException("User owner not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else {
            User userToAdd = userToAddOptional.get();
            User userOwner = userOwnerOptional.get();
            Project project = projectOptional.get();

            if (userToAdd.equals(userOwner)) {
                throw new ServiceException("User owner cant be invited to project");
            }

            if (project.getProjectOwner().getUuid().equals(userOwner.getUuid())) {

                if (project.getProjectUsers().contains(userToAdd)) {
                    throw new ServiceException("User is already in this project");
                } else {
//                    project.getProjectUsersUuid().add(userToAdd.getUuid());
//                    project.getProjectUsers().add(userToAdd);
                    project.getProjectUsersInvited().add(userToAdd);
//                    project.getProjectInvitedUsers().add(userToAdd);
                }

//                Set<Project> projects = userToAdd.getProjectsAppended();
//                projects.add(project);
//                userToAdd.setProjectsAppended(projects);

//                List<Project> projectsInvitations = userToAdd.getProjectsInvitations();
//                projectsInvitations.add(project);
//                userToAdd.setProjectsInvitations(projectsInvitations);

                userRepository.save(userToAdd);

                return projectRepository.save(project);
//                return project;
            } else {
                throw new ServiceException("Invalid project owner");
            }
        }
    }


    /** DEBUG VERSIONS WITHOUT INVITE */

    @SuppressWarnings("DuplicatedCode")
    @PostMapping("{uuid}/createProjectDebug")
    @JsonView(Views.projectView.class)
    public Project createProjectDebug(@PathVariable String uuid, @Valid @RequestBody ComplaintProject complaintProject) {

        Project project = complaintProject.project;
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));

        if (!userOptional.isPresent()) {
            throw new ServiceException("User is not found");
        } else if (project.getProjectDescription() == null) {
            throw new ServiceException("Invalid projectDescription");
        } else if (project.getProjectName() == null || project.getProjectName().equals("")) {
            throw new ServiceException("Invalid projectName");
        } else {
            if (project.getProjectCreationTime() == 0) {
                Date dateNow = new Date();
                project.setProjectCreationTime(dateNow.getTime());
            }
            User user = userOptional.get();
            project.setProjectOwner(user);

            projectRepository.save(project);

            List<String> usersToAdd = complaintProject.usersToAdd;
            for (String userToAdd : usersToAdd) {
                if (!userRepository.findUserByUuid(UUID.fromString(userToAdd)).isPresent()) {
                    projectRepository.delete(project);
                    throw new ServiceException("User to add is not found");
                } else if (userToAdd.equals(user.getUuid().toString())) {
                    projectRepository.delete(project);
                    throw new ServiceException("User owner cant be invited to project");
                }
                addUserToProjectDebug(uuid, userToAdd, String.valueOf(project.getProjectId()));
            }

            return project;
        }
    }


    @SuppressWarnings("DuplicatedCode")
    @PostMapping("{uuidOwner}/{projectID}/addUserToProjectDebug/{uuidUserToAdd}")
    @JsonView({Views.projectView.class})
    public Project addUserToProjectDebug(@PathVariable String uuidOwner, @PathVariable String uuidUserToAdd, @PathVariable String projectID) {

        Optional<User> userToAddOptional = userRepository.findUserByUuid(UUID.fromString(uuidUserToAdd));
        Optional<User> userOwnerOptional = userRepository.findUserByUuid(UUID.fromString(uuidOwner));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (!userToAddOptional.isPresent()) {
            throw new ServiceException("User to add not found");
        } else if (!userOwnerOptional.isPresent()) {
            throw new ServiceException("User owner not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else {
            User userToAdd = userToAddOptional.get();
            User userOwner = userOwnerOptional.get();
            Project project = projectOptional.get();

            if (userToAdd.equals(userOwner)) {
                throw new ServiceException("User owner cant be invited to project");
            }

            if (project.getProjectOwner().getUuid().equals(userOwner.getUuid())) {

                if (project.getProjectUsers().contains(userToAdd)) {
                    throw new ServiceException("User is already in this project");
                } else {
                    project.getProjectUsers().add(userToAdd);
                }
                userRepository.save(userToAdd);
                return projectRepository.save(project);
            } else {
                throw new ServiceException("Invalid project owner");
            }
        }
    }

    /** */


    @PostMapping("{uuid}/{projectID}/addDeadline")
    @JsonView({Views.deadlinesDetailView.class})
    public Deadline addDeadlineToProject(@PathVariable String uuid, @PathVariable String projectID, @RequestBody ComplaintDeadline complaintDeadline) {

        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<Project> projectToAddOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));

        Deadline deadline = complaintDeadline.deadline;

        if (!userOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectToAddOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else if (deadline.getDeadlineName() == null || deadline.getDeadlineName().equals("")) {
            throw new ServiceException("Invalid deadlineName");
        } else if (deadline.getDeadlineDescription() == null) {
            throw new ServiceException("Invalid deadlineDescription");
        } else {

            User user = userOptional.get();
            Project projectToAdd = projectToAddOptional.get();

            if (projectToAdd.getProjectOwner().getUuid().equals(user.getUuid())) {

                if (deadline.getDeadlineCreatedTime() == 0) {
                    Date dateNow = new Date();
                    deadline.setDeadlineCreatedTime(dateNow.getTime());
                }

                deadline.setProject(projectToAdd);
                deadline.setDeadlineProjectId(projectToAdd.getProjectId());
                projectToAdd.getDeadlines().add(deadline);


                deadlineRepository.save(deadline);

                projectRepository.save(projectToAdd);

                List<String> usersUUIDToAdd = complaintDeadline.usersToAdd;

                for (String userToAdd : usersUUIDToAdd) {
                    Optional<User> userToAddOptional = userRepository.findUserByUuid(UUID.fromString(userToAdd));
                    if (!userToAddOptional.isPresent()) {
                        deadlineRepository.delete(deadline);
                        throw new ServiceException("User to add not found");
                    }
                    addExecutorToDeadline(uuid, projectID, String.valueOf(deadline.getDeadlineId()), userToAdd);
                }

                deadline.setDeadlineExecutors(deadline.getDeadlineExecutors());
                return deadline;
            } else {
                throw new ServiceException("Invalid project owner");
            }
        }

    }


    @PostMapping("{uuidOwner}/{projectID}/{deadlineId}/addExecutor/{uuidUserToAdd}")
    @JsonView({Views.deadlinesDetailView.class})
    public Deadline addExecutorToDeadline(@PathVariable String uuidOwner,
                                         @PathVariable String projectID,
                                         @PathVariable String deadlineId,
                                         @PathVariable String uuidUserToAdd) {

        Optional<User> userToAddOptional = userRepository.findUserByUuid(UUID.fromString(uuidUserToAdd));
        Optional<User> userOwnerOptional = userRepository.findUserByUuid(UUID.fromString(uuidOwner));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (!userToAddOptional.isPresent()) {
            throw new ServiceException("User to add not found");
        } else if (!userOwnerOptional.isPresent()) {
            throw new ServiceException("User owner not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else {
            User userToAdd = userToAddOptional.get();
            User userOwner = userOwnerOptional.get();
            Project project = projectOptional.get();

            if (project.getProjectOwner().getUuid().equals(userOwner.getUuid())) {

                if (project.getProjectUsers().contains(userToAdd) || (project.getProjectOwner().equals(userToAdd))) {
                    List<Deadline> projectDeadlines = project.getDeadlines();
                    for (Deadline deadline : projectDeadlines) {
                        if (deadline.getDeadlineId() == Integer.parseInt(deadlineId)) {
                            deadline.getDeadlineExecutors().add(userToAdd);

                            projectRepository.save(project);

                            deadline.setDeadlineExecutors(deadline.getDeadlineExecutors());

                            return deadline;
                        }
                    }
                    throw new ServiceException("Deadline not found");
                } else {
                    throw new ServiceException("User to add is not in this project");
                }
            } else {
                throw new ServiceException("Invalid project owner");
            }
        }
    }


    /*********EDITING*********/

    @Modifying
    @PostMapping("{uuid}/{projectID}/editProject")
    @JsonView(Views.projectView.class)
    public Project editProject(@PathVariable String uuid, @PathVariable String projectID, @RequestBody Project projectEdited) {

        Optional<User> userToEditOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (!userToEditOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else {
            User user = userToEditOptional.get();
            Project project = projectOptional.get();

            if (!project.getProjectOwner().equals(user)) {
                throw new ServiceException("Invalid project owner");
            }

            if (projectEdited.getProjectName() != null && !projectEdited.getProjectName().equals("")) {
                project.setProjectName(projectEdited.getProjectName());
            }
            if (projectEdited.getProjectDescription() != null && !projectEdited.getProjectDescription().equals("")) {
                project.setProjectDescription(projectEdited.getProjectDescription());
            }
            if (!(projectEdited.getProjectExecutionTime() == 0)) {
                project.setProjectExecutionTime(projectEdited.getProjectExecutionTime());
            }

            return projectRepository.save(project);
        }
    }


    @Modifying
    @PostMapping("{uuid}/{projectID}/{deadlineID}/editDeadline")
    @JsonView(Views.deadlinesDetailView.class)
    public Deadline editDeadline(@PathVariable String uuid, @PathVariable String projectID, @PathVariable String deadlineID, @RequestBody Deadline deadlineEdited) {

        Optional<User> userToEditOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));
        Optional<Deadline> deadlineOptional = deadlineRepository.findById(Integer.parseInt(deadlineID));

        if (!userToEditOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else if (!deadlineOptional.isPresent()) {
            throw new ServiceException("Deadline not found");
        } else {
            User user = userToEditOptional.get();
            Project project = projectOptional.get();
            Deadline deadline = deadlineOptional.get();

            if (!project.getProjectOwner().equals(user)) {
                throw new ServiceException("Invalid project owner");
            }

            if (deadline.getDeadlineName() != null && !deadlineEdited.getDeadlineName().equals("")) {
                deadline.setDeadlineName(deadlineEdited.getDeadlineName());
            }
            if (deadlineEdited.getDeadlineDescription() != null && !deadlineEdited.getDeadlineDescription().equals("")) {
                deadline.setDeadlineDescription(deadlineEdited.getDeadlineDescription());
            }
            if (!(deadlineEdited.getDeadlineExecutionTime() == 0)) {
                deadline.setDeadlineExecutionTime(deadlineEdited.getDeadlineExecutionTime());
            }

            return deadlineRepository.save(deadline);
        }
    }


    /*********DELETING*********/

    @DeleteMapping("{uuid}/{projectID}/deleteProject")
    public String deleteProject(@PathVariable String uuid, @PathVariable String projectID) {
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<Project> projectOptional = projectRepository.findById(Integer.parseInt(projectID));

        if (!userOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else {
            User userOwner = userOptional.get();
            Project project = projectOptional.get();
            if (project.getProjectOwner().equals(userOwner)) {
                projectRepository.delete(project);
                throw new SuccessException("Deleted");
            } else {
                throw new ServiceException("Invalid project owner");
            }
        }
    }


    @DeleteMapping("{uuid}/{projectID}/{deadlineID}/deleteDeadline")
    public String deleteDeadline(@PathVariable String uuid, @PathVariable String projectID, @PathVariable String deadlineID) {
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<Project> projectOptional = projectRepository.findById(Integer.parseInt(projectID));
        Optional<Deadline> deadlineOptional = deadlineRepository.findById(Integer.parseInt(deadlineID));

        if (!userOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else if (!deadlineOptional.isPresent()) {
            throw new ServiceException("Deadline not found");
        } else {
            User userOwner = userOptional.get();
            Project project = projectOptional.get();
            Deadline deadline = deadlineOptional.get();

            if (project.getProjectOwner().equals(userOwner)) {
                if (project.getDeadlines().contains(deadline)) {
                    deadlineRepository.delete(deadline);
                    throw new SuccessException("Deleted");
                } else {
                    throw new ServiceException("Deadline is not this project");
                }
            } else {
                throw new ServiceException("User is not owner of this project");
            }
        }
    }


    @DeleteMapping("{uuid}/{projectID}/{deadlineID}/deleteExecutorFromDeadline/{userToDeleteUUID}")
    public String deleteExecutorFromDeadline(@PathVariable String uuid, @PathVariable String projectID, @PathVariable String deadlineID, @PathVariable String userToDeleteUUID) {
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<User> userToDeleteOptional = userRepository.findUserByUuid(UUID.fromString(userToDeleteUUID));
        Optional<Project> projectOptional = projectRepository.findById(Integer.parseInt(projectID));
        Optional<Deadline> deadlineOptional = deadlineRepository.findById(Integer.parseInt(deadlineID));

        if (!userOptional.isPresent()) {
            throw new ServiceException("User owner not found");
        } else if (!userToDeleteOptional.isPresent()) {
            throw new ServiceException("User to delete not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else if (!deadlineOptional.isPresent()) {
            throw new ServiceException("Deadline not found");
        } else {
            User userOwner = userOptional.get();
            User userToDelete = userToDeleteOptional.get();
            Project project = projectOptional.get();
            Deadline deadline = deadlineOptional.get();

            if (project.getProjectOwner().equals(userOwner)) {
                if (project.getDeadlines().contains(deadline)) {
                    if (!project.getProjectUsers().contains(userToDelete)) {
                        throw new ServiceException("User to delete is not this project");
                    }
                    deadline.getDeadlineExecutors().remove(userToDelete);
                    deadlineRepository.save(deadline);
                    throw new SuccessException("Deleted");
                } else {
                    throw new ServiceException("Deadline is not this project");
                }
            } else {
                throw new ServiceException("User is not owner of this project");
            }
        }
    }


    @DeleteMapping("{uuid}/{projectID}/deleteUserFromProject/{userToDeleteUUID}")
    public String deleteUserFromProject(@PathVariable String uuid, @PathVariable String projectID, @PathVariable String userToDeleteUUID) {
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<User> userToDeleteOptional = userRepository.findUserByUuid(UUID.fromString(userToDeleteUUID));
        Optional<Project> projectOptional = projectRepository.findById(Integer.parseInt(projectID));

        if (!userOptional.isPresent()) {
            throw new ServiceException("User owner not found");
        } else if (!userToDeleteOptional.isPresent()) {
            throw new ServiceException("User to delete not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else {
            User userOwner = userOptional.get();
            User userToDelete = userToDeleteOptional.get();
            Project project = projectOptional.get();

            if (project.getProjectOwner().equals(userOwner)) {

                project.getProjectUsers().remove(userToDelete);

                for (Deadline deadline : project.getDeadlines()) {
                    deleteExecutorFromDeadline(uuid, projectID, String.valueOf(deadline.getDeadlineId()), userToDeleteUUID);
                }

                projectRepository.save(project);
                throw new SuccessException("Deleted");

            } else {
                throw new ServiceException("User is not owner of this project");
            }
        }
    }

}
