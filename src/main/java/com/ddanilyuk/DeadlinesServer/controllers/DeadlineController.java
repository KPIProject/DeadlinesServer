package com.ddanilyuk.DeadlinesServer.controllers;

import com.ddanilyuk.DeadlinesServer.extensions.ServiceException;
import com.ddanilyuk.DeadlinesServer.extensions.SuccessException;
import com.ddanilyuk.DeadlinesServer.model.*;
import com.ddanilyuk.DeadlinesServer.repositories.DeadlineRepository;
import com.ddanilyuk.DeadlinesServer.repositories.ProjectRepository;
import com.ddanilyuk.DeadlinesServer.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping
public class DeadlineController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final DeadlineRepository deadlineRepository;


    public DeadlineController(ProjectRepository projectRepository, UserRepository userRepository, DeadlineRepository deadlineRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.deadlineRepository = deadlineRepository;
    }


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
                    Optional<User> userToAddOptional = userRepository.findByUsername(userToAdd);
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


    @PostMapping("{uuidOwner}/{projectID}/{deadlineId}/addExecutor/{usernameToAdd}")
    @JsonView({Views.deadlinesDetailView.class})
    public Deadline addExecutorToDeadline(@PathVariable String uuidOwner,
                                          @PathVariable String projectID,
                                          @PathVariable String deadlineId,
                                          @PathVariable String usernameToAdd) {

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
                    throw new ServiceException("Deadline is not in this project");
                }
            } else {
                throw new ServiceException("Invalid project owner");
            }
        }
    }

}
