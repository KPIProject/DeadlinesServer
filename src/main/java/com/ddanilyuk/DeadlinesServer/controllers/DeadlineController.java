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

/**
 Контролер дедлайнів
 */

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


    /**
     * Додавання дедлайна у проект
     * @param uuid  унікальний id юзера
     * @param projectID  унікальний id проекта
     * @param complaintDeadline  дедлайн який ми хочемо додати
     * @return  - дедлайн
     */
    @PostMapping("{uuid}/{projectID}/addDeadline")
    @JsonView({Views.deadlinesDetailView.class})
    public Deadline addDeadlineToProject(@PathVariable String uuid, @PathVariable String projectID, @RequestBody ComplaintDeadline complaintDeadline) {
        /**
        В списку з бази даних по uuid знаходимо юзера
        */
        Optional<User> userOptional = userRepository.findUserByUuid(UUID.fromString(uuid));
        Optional<Project> projectToAddOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));
        /**
        Отримуємо дедлайн який хочемо додати
        */
        Deadline deadline = complaintDeadline.deadline;
        /**
        Блок помилок
        */
        if (!userOptional.isPresent()) {
            throw new ServiceException("User not found");
        } else if (!projectToAddOptional.isPresent()) {
            throw new ServiceException("Project not found");
        } else if (deadline.getDeadlineName() == null || deadline.getDeadlineName().equals("")) {
            throw new ServiceException("Invalid deadlineName");
        } else if (deadline.getDeadlineDescription() == null) {
            throw new ServiceException("Invalid deadlineDescription");
        } else {
            /**
            Отримуємо неопціонального юзера
            */
            User user = userOptional.get();
            Project projectToAdd = projectToAddOptional.get();
            /**
            Якщо в проекті куди ми додаємо дедлайн , юзер і власник проекту мають однакові uuid то додаємо дедлайн
            */
            if (projectToAdd.getProjectOwner().getUuid().equals(user.getUuid())) {
                /**
                Якщо в дедлайні, який додає юзер, немає поля date, воно додається з поточною датою сервера
                */
                if (deadline.getDeadlineCreatedTime() == 0) {
                    Date dateNow = new Date();
                    deadline.setDeadlineCreatedTime(dateNow.getTime());
                }
                /**
                При створенні дедлайну
                completeMark Boolean - відмітка про виконання == false
                */
                deadline.setCompleteMark(Boolean.FALSE);
                deadline.setCompletedBy("");
                /**
                Проект у якому знаходиться дедлайн
                */
                deadline.setProject(projectToAdd);
                /**
                У дедлайна є projectID в полі якого він знаходиться
                */
                deadline.setDeadlineProjectId(projectToAdd.getProjectId());
                /**
                Всі дедлайни додаємо у проект (взаємний зв'язок)
                */
                projectToAdd.getDeadlines().add(deadline);

                deadlineRepository.save(deadline);
                projectRepository.save(projectToAdd);

                List<String> usersUUIDToAdd = complaintDeadline.usersToAdd;
                /**
                Додавання юзера до дедлайна
                */
                for (String userToAdd : usersUUIDToAdd) {
                    Optional<User> userToAddOptional = userRepository.findByUsername(userToAdd);
                    if (!userToAddOptional.isPresent()) {
                        deadlineRepository.delete(deadline);
                        /**
                        ServiceException при якому юзера не існує
                         */
                        throw new ServiceException("User to add not found");
                    }
                    /**
                    Якщо юзер існує додається виконавець у дедлайн
                     */
                    addExecutorToDeadline(uuid, projectID, String.valueOf(deadline.getDeadlineId()), userToAdd);
                }

                deadline.setDeadlineExecutors(deadline.getDeadlineExecutors());
                /**
                 Повертає дедлайн
                 */
                return deadline;
            } else {
                /**
                 ServiceException при якому не власник проекту хоче додати дедлайн
                 */
                throw new ServiceException("Invalid project owner");
            }
        }
    }

    /**
     * Додавання виконавця дедлайна
     * @param uuidOwner  uuid власника проекту
     * @param projectID  унікальний id проекта
     * @param deadlineId  унікальний id дедлайна
     * @param usernameToAdd - користувач якого додаємо
     * @return deadline - дедлайн
     */

    @PostMapping("{uuidOwner}/{projectID}/{deadlineId}/addExecutor/{usernameToAdd}")
    @JsonView({Views.deadlinesDetailView.class})
    public Deadline addExecutorToDeadline(@PathVariable String uuidOwner,
                                          @PathVariable String projectID,
                                          @PathVariable String deadlineId,
                                          @PathVariable String usernameToAdd) {

        Optional<User> userToAddOptional = userRepository.findByUsername(usernameToAdd);
        Optional<User> userOwnerOptional = userRepository.findUserByUuid(UUID.fromString(uuidOwner));
        Optional<Project> projectOptional = projectRepository.findByProjectId(Integer.parseInt(projectID));
        /**
         Перевірки на помилки
         */
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

    /**
     * Відмітка про виконання дедлайну
     * @param uuid унікальний id юзера
     * @param projectID унікальний id проекта
     * @param deadlineID унікальний id дедлайна
     * @return deadlineRepository.save(deadline) - виконаний дедлайн
     */

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

    /**
     * Відмітка про невиконання дедлайну
     * @param uuid унікальний id юзера
     * @param projectID унікальний id проекта
     * @param deadlineID унікальний id дедлайна
     * @return deadlineRepository.save(deadline) - невиконаний дедлайн
     */

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

    /**
     * Редагування дедлайну
     * @param uuid унікальний id юзера
     * @param projectID унікальний id проекта
     * @param deadlineID унікальний id дедлайна
     * @param deadlineEdited дедлайн який ми редагуємо
     * @return deadlineRepository.save(deadline) збереження дедлайну який ми редагували
     */
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
