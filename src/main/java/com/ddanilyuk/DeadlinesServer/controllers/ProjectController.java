package com.ddanilyuk.DeadlinesServer.controllers;

import com.ddanilyuk.DeadlinesServer.extensions.RestMessage;
import com.ddanilyuk.DeadlinesServer.model.*;
import com.ddanilyuk.DeadlinesServer.repositories.DeadlineRepository;
import com.ddanilyuk.DeadlinesServer.repositories.ProjectRepository;
import com.ddanilyuk.DeadlinesServer.repositories.UserRepository;
import com.ddanilyuk.DeadlinesServer.extensions.ServiceException;
import com.ddanilyuk.DeadlinesServer.extensions.SuccessException;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Контролер проектів
 */

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

    /**
     * Створення проекту з запрошенням юзера
     * @param uuid унікальний id юзера
     * @param complaintProject проект що створюємо
     * @return project - створенний проект
     */

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
            project.setCompleteMark(Boolean.FALSE);

            projectRepository.save(project);

            List<String> usersToAdd = complaintProject.usersToAdd;

            for (String userToAdd : usersToAdd) {
                if (!userRepository.findByUsername(userToAdd).isPresent()) {
                    projectRepository.delete(project);
                    throw new ServiceException("User to add not found");
                } else if (userToAdd.equals(user.getUsername())) {
                    projectRepository.delete(project);
                    throw new ServiceException("User owner cant be invited to project");
                }
                addUserToProject(uuid, userToAdd, String.valueOf(project.getProjectId()));
            }

            return project;

        }
    }

    /**
     * Додавання юзера у проект
     * @param uuidOwner uuid власника проекту
     * @param usernameToAdd username користувача якого додаємо
     * @param projectID унікальний id проекта
     * @return projectRepository.save(project) - збереження проекту у репозиторій
     */
    @PostMapping("{uuidOwner}/{projectID}/addUserToProject/{usernameToAdd}")
    @JsonView({Views.projectView.class})
    public Project addUserToProject(@PathVariable String uuidOwner, @PathVariable String usernameToAdd, @PathVariable String projectID) {

        Optional<User> userToAddOptional = userRepository.findByUsername(usernameToAdd);
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
                    project.getProjectUsersInvited().add(userToAdd);
                }
                return projectRepository.save(project);
            } else {
                throw new ServiceException("Invalid project owner");
            }
        }
    }


    /** DEBUG VERSIONS WITHOUT INVITE */
    /**
     * Створення проекту без запрошення юзера
     * @param uuid  унікальний id юзера
     * @param complaintProject виконаний проект
     * @return project - проект який створили
     */
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
            project.setCompleteMark(Boolean.FALSE);

            projectRepository.save(project);

            List<String> usersToAdd = complaintProject.usersToAdd;
            for (String userToAdd : usersToAdd) {
                if (!userRepository.findByUsername(userToAdd).isPresent()) {
                    projectRepository.delete(project);
                    throw new ServiceException("User to add not found");
                } else if (userToAdd.equals(user.getUsername())) {
                    projectRepository.delete(project);
                    throw new ServiceException("User owner cant be invited to project");
                }
                addUserToProjectDebug(uuid, userToAdd, String.valueOf(project.getProjectId()));
            }

            return project;
        }
    }

    /**
     * Додавання юзера до проекту
     * @param uuidOwner   uuid власника проекту
     * @param usernameToAdd  username користувача якого додаємо
     * @param projectID  унікальний id проекта
     * @return projectRepository.save(project) - збереження проекту у репозиторій
     */

    @SuppressWarnings("DuplicatedCode")
    @PostMapping("{uuidOwner}/{projectID}/addUserToProjectDebug/{usernameToAdd}")
    @JsonView({Views.projectView.class})
    public Project addUserToProjectDebug(@PathVariable String uuidOwner, @PathVariable String usernameToAdd, @PathVariable String projectID) {

        Optional<User> userToAddOptional = userRepository.findByUsername(usernameToAdd);
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
                return projectRepository.save(project);
            } else {
                throw new ServiceException("Invalid project owner");
            }
        }
    }

    /**
     * Виконання проекту
     * @param uuid унікальний id юзера
     * @param projectID унікальний id проекта
     * @return projectRepository.save(project) - збереження проекту у репозиторій
     */

    @Modifying
    @PostMapping("{uuid}/{projectID}/setProjectComplete")
    @JsonView({Views.projectView.class})
    public Project setProjectComplete(@PathVariable String uuid, @PathVariable String projectID) {

        Optional<User> userOwnerOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (!userOwnerOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else {
            User userOwner = userOwnerOptional.get();
            Project project = projectOptional.get();

            if (!project.getProjectOwner().equals(userOwner)) {
                throw new ServiceException("Invalid project owner");
            }

            project.setCompleteMark(Boolean.TRUE);

            for (Deadline deadline: project.getDeadlines()) {
                deadline.setCompleteMark(Boolean.TRUE);
                deadline.setCompletedBy(userOwner.getUsername());
            }

            return projectRepository.save(project);
        }
    }

    /**
     * Невиконання проекту
     * @param uuid унікальний id юзера
     * @param projectID унікальний id проекта
     * @return projectRepository.save(project) - збереження проекту у репозиторій
     */

    @Modifying
    @PostMapping("{uuid}/{projectID}/setProjectUnComplete")
    @JsonView({Views.projectView.class})
    public Project setProjectUnComplete(@PathVariable String uuid, @PathVariable String projectID) {

        Optional<User> userOwnerOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));

        if (!userOwnerOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else {
            User userOwner = userOwnerOptional.get();
            Project project = projectOptional.get();

            if (!project.getProjectOwner().equals(userOwner)) {
                throw new ServiceException("Invalid project owner");
            }

            project.setCompleteMark(Boolean.FALSE);
            return projectRepository.save(project);
        }
    }


    /**
     * Редагування проекту
     * @param uuid унікальний id юзера
     * @param projectID унікальний id проекта
     * @param projectEdited проект який редагуємо
     * @return projectRepository.save(project) - збереження проекту у репозиторій
     */

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

    /**
     * Видалення проекту
     * @param uuid унікальний id юзера
     * @param projectID унікальний id проекта
     * @return RestMessage("Success", 200, "Deleted") - повідомлення про успішне видалення проекту
     */
    @DeleteMapping("{uuid}/{projectID}/deleteProject")
    public RestMessage deleteProject(@PathVariable String uuid, @PathVariable String projectID) {
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
                return new RestMessage("Success", 200, "Deleted");
            } else {
                throw new ServiceException("Invalid project owner");
            }
        }
    }

    /**
     * Видалення виконавця з дедлайну
     * @param uuid унікальний id юзера
     * @param projectID унікальний id проекта
     * @param deadlineID id дедлайну
     * @param usernameToDelete username корисувача якого видаляємо
     * @return RestMessage("Success", 200, "Deleted") - повідомлення про успішне видалення юзера
     */

    @DeleteMapping("{uuid}/{projectID}/{deadlineID}/deleteExecutorFromDeadline/{usernameToDelete}")
    public RestMessage deleteExecutorFromDeadline(@PathVariable String uuid, @PathVariable String projectID, @PathVariable String deadlineID, @PathVariable String usernameToDelete) {
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<User> userToDeleteOptional = userRepository.findByUsername(usernameToDelete);
        Optional<Project> projectOptional = projectRepository.findById(Integer.parseInt(projectID));
        Optional<Deadline> deadlineOptional = deadlineRepository.findById(Integer.parseInt(deadlineID));

        if (!userOptional.isPresent()) {
            throw new ServiceException("User not found");
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
                    return new RestMessage("Success", 200, "Deleted");
                } else {
                    throw new ServiceException("Deadline is not in this project");
                }
            } else {
                throw new ServiceException("Invalid project owner");
            }
        }
    }

    /**
     * Видалення дедлайну
     * @param uuid унікальний id юзера
     * @param projectID унікальний id проекта
     * @param deadlineID id дедлайну
     * @return RestMessage("Success", 200, "Deleted") - повідомлення про успішне видалення дедлайну
     */

    @DeleteMapping("{uuid}/{projectID}/{deadlineID}/deleteDeadline")
    public RestMessage deleteDeadline(@PathVariable String uuid, @PathVariable String projectID, @PathVariable String deadlineID) {
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
//                    deadlineRepository.deleteById(deadline.getDeadlineId());
                    project.getDeadlines().remove(deadline);
                    deadlineRepository.delete(deadline);
                    return new RestMessage("Success", 200, "Deleted");
                } else {
                    throw new ServiceException("Deadline is not in this project");
                }

            } else {
                throw new ServiceException("Invalid project owner");
            }
        }
    }

    /**
     * Видалення юзера з проекту
     * @param uuid унікальний id юзера
     * @param projectID унікальний id проекта
     * @param usernameToDelete username корисувача якого видаляємо
     * @return RestMessage("Success", 200, "Deleted") - повідомлення про успішне видалення юзера
     */

    @DeleteMapping("{uuid}/{projectID}/deleteUserFromProject/{usernameToDelete}")
    public RestMessage deleteUserFromProject(@PathVariable String uuid, @PathVariable String projectID, @PathVariable String usernameToDelete) {
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<User> userToDeleteOptional = userRepository.findByUsername(usernameToDelete);
        Optional<Project> projectOptional = projectRepository.findById(Integer.parseInt(projectID));

        if (!userOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!userToDeleteOptional.isPresent()) {
            throw new ServiceException("User to delete not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else {
            User userOwner = userOptional.get();
            User userToDelete = userToDeleteOptional.get();
            Project project = projectOptional.get();

            if (project.getProjectOwner().equals(userOwner)) {

                for (Deadline deadline : project.getDeadlines()) {
                    deleteExecutorFromDeadline(uuid, projectID, String.valueOf(deadline.getDeadlineId()), usernameToDelete);
                }

                project.getProjectUsers().remove(userToDelete);

                projectRepository.save(project);
                return new RestMessage("Success", 200, "Deleted");

            } else {
                throw new ServiceException("Invalid project owner");
            }
        }
    }

}
