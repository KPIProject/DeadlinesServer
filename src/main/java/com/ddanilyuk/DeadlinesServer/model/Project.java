package com.ddanilyuk.DeadlinesServer.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * Клас проекту з усіма полями і описом
 */
@SuppressWarnings({"unused"})
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.defaultView.class)
    private int projectId;

    @JsonView(Views.defaultView.class)
    private String projectName;

    /**
     * Максимальна кількісь символів для опису проекту
     */
    @Size(max = 8192)
    @JsonView(Views.defaultView.class)
    private String projectDescription;


    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Views.defaultView.class)
    private List<Deadline> deadlines = new ArrayList<>();


    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(Views.projectView.class)
    private User projectOwner;

    @Column
    @JsonView(Views.projectView.class)
    @ManyToMany
    @JoinTable(
            name = "projects_appended",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> projectUsers = new ArrayList<>();


    @Column
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "projects_invited",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonView(Views.projectView.class)
    private List<User> projectUsersInvited = new ArrayList<>();


    @Column
    @JsonView(Views.defaultView.class)
    private long projectCreationTime;


    @Column
    @JsonView(Views.defaultView.class)
    private long projectExecutionTime;

    /**
     * Коли ти виконуєш проект, дедлайни автоматично стають виконані
     */
    @Column
    @JsonView(Views.defaultView.class)
    private Boolean completeMark;


    public Project() {
    }

    /**
     *
     * @param projectName - назва проекту
     * @param projectDescription - опис проекту
     */
    public Project(String projectName, String projectDescription) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        /**
         * Задавання часу створення проекту відповідно до часу самого сєрвака
         */
        Date dateNow = new Date();
        projectCreationTime = dateNow.getTime();
    }

    /**
     *
     * @param projectName - назва проекту
     * @param projectDescription - опис проекту
     * @param userOwner - ім'я користувача проекта
     */
    public Project(String projectName, String projectDescription, User userOwner) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectOwner = userOwner;
        /**
         * Задавання часу створення проекту
         */
        Date dateNow = new Date();
        projectCreationTime = dateNow.getTime();
    }

    public List<User> getProjectUsersInvited() {
        return projectUsersInvited;
    }

    public void setProjectUsersInvited(List<User> projectUsersInvited) {
        this.projectUsersInvited = projectUsersInvited;
    }

    public Boolean getCompleteMark() {
        return completeMark;
    }

    public void setCompleteMark(Boolean completeMark) {
        this.completeMark = completeMark;
    }

    public long getProjectCreationTime() {
        return projectCreationTime;
    }

    public void setProjectCreationTime(long projectCreatedTime) {
        this.projectCreationTime = projectCreatedTime;
    }

    public long getProjectExecutionTime() {
        return projectExecutionTime;
    }

    public void setProjectExecutionTime(long projectExecutionTime) {
        this.projectExecutionTime = projectExecutionTime;
    }

    public List<User> getProjectUsers() {
        return projectUsers;
    }

    public void setProjectUsers(List<User> projectUsers) {
        this.projectUsers = projectUsers;
    }


    public User getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(User projectOwner) {
        this.projectOwner = projectOwner;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }


    public List<Deadline> getDeadlines() {
        return deadlines;
    }

    public void setDeadlines(List<Deadline> deadlines) {
        this.deadlines = deadlines;
    }

}
