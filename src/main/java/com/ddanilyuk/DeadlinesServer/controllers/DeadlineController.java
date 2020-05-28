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

import java.util.*;

@RestController
@RequestMapping
/*
Контролер дедлайнів
*/
public class DeadlineController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final DeadlineRepository deadlineRepository;


    public DeadlineController(ProjectRepository projectRepository, UserRepository userRepository, DeadlineRepository deadlineRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.deadlineRepository = deadlineRepository;
    }

/*
Функція додавання дедлайна
*/
    @PostMapping("{uuid}/{projectID}/addDeadline")
    @JsonView({Views.deadlinesDetailView.class})
    public Deadline addDeadlineToProject(@PathVariable String uuid, @PathVariable String projectID, @RequestBody ComplaintDeadline complaintDeadline) {
// в списку з бази даних по ююid намагаємось знайти юзера
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        // намагаємось найти проект
        Optional<Project> projectToAddOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));
// отрим. дедлайн який хочемо додати
        Deadline deadline = complaintDeadline.deadline;
// блок для помилки
        if (!userOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectToAddOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else if (deadline.getDeadlineName() == null || deadline.getDeadlineName().equals("")) {
            throw new ServiceException("Invalid deadlineName");
        } else if (deadline.getDeadlineDescription() == null) {
            throw new ServiceException("Invalid deadlineDescription");
        } else {
// отрим не опціонального юзера
            User user = userOptional.get();
            Project projectToAdd = projectToAddOptional.get();
// якщо в проекті куда хочемо додати дедл, юзер і проект мають однакові ююайді(мож додати дедл якщо ти власник)
            if (projectToAdd.getProjectOwner().getUuid().equals(user.getUuid())) {
// якщо в тому дедлайні який юзер хоче додати немає поля дейт, сервер додає тікущу дату сервака
                if (deadline.getDeadlineCreatedTime() == 0) {
                    Date dateNow = new Date();
                    deadline.setDeadlineCreatedTime(dateNow.getTime());
                }
                //робимо так що дедл не виконаний
                deadline.setCompleteMark(Boolean.FALSE);
                deadline.setCompletedBy("");
                // у дедлайна є проект в якому він знаходиться
                deadline.setProject(projectToAdd);
                // у дедлайна є айдішнік проекта в полі якого він знаходиться
                deadline.setDeadlineProjectId(projectToAdd.getProjectId());
                //беремо всі дедлайни і додаємо у проект(взаємний зв'язок)
                projectToAdd.getDeadlines().add(deadline);

                deadlineRepository.save(deadline);
                projectRepository.save(projectToAdd);

                List<String> usersUUIDToAdd = complaintDeadline.usersToAdd;
//відповідає за додавання юзера до дедлайна
                for (String userToAdd : usersUUIDToAdd) {
                    Optional<User> userToAddOptional = userRepository.findByUsername(userToAdd);
                    if (!userToAddOptional.isPresent()) {
                        deadlineRepository.delete(deadline);
                        throw new ServiceException("User to add not found");
                    }
                    // якщо юзер існує, виконується ця функція, додати виконавця в дедлайн
                    addExecutorToDeadline(uuid, projectID, String.valueOf(deadline.getDeadlineId()), userToAdd);
                }

                deadline.setDeadlineExecutors(deadline.getDeadlineExecutors());
                return deadline; // повертає дедлайн
            } else {
                throw new ServiceException("Invalid project owner"); // ти не власник проекту але хоч додати дедл
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
// перевірка на помилки
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
    @PostMapping("{uuid}/{projectID}/{deadlineID}/setDeadlineComplete")
    @JsonView({Views.deadlinesDetailView.class})
    public Deadline setDeadlineComplete(@PathVariable String uuid, @PathVariable String projectID, @PathVariable String deadlineID) {

        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));
        Optional<Deadline> deadlineOptional = deadlineRepository.findById(Integer.parseInt(deadlineID));

        if (!userOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else if (!deadlineOptional.isPresent()) {
            throw new ServiceException("Deadline not found");
        } else {
            User user = userOptional.get();
            Project project = projectOptional.get();
            Deadline deadline = deadlineOptional.get();

            if (!deadline.getDeadlineExecutors().contains(user) && user != project.getProjectOwner()) {
                throw new ServiceException("User cant complete this deadline");
            }

            deadline.setCompleteMark(Boolean.TRUE);
            deadline.setCompletedBy(user.getUsername());


            return deadlineRepository.save(deadline);
        }
    }


    @Modifying
    @PostMapping("{uuid}/{projectID}/{deadlineID}/setDeadlineUnComplete")
    @JsonView({Views.deadlinesDetailView.class})
    public Deadline setDeadlineUnComplete(@PathVariable String uuid, @PathVariable String projectID, @PathVariable String deadlineID) {

        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));
        Optional<Deadline> deadlineOptional = deadlineRepository.findById(Integer.parseInt(deadlineID));

        if (!userOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else if (!deadlineOptional.isPresent()) {
            throw new ServiceException("Deadline not found");
        } else {
            User user = userOptional.get();
            Project project = projectOptional.get();
            Deadline deadline = deadlineOptional.get();

            if (!deadline.getDeadlineExecutors().contains(user) && user != project.getProjectOwner()) {
                throw new ServiceException("User cant complete this deadline");
            }

            deadline.setCompleteMark(Boolean.FALSE);
            deadline.setCompletedBy("");

            return deadlineRepository.save(deadline);
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




}
