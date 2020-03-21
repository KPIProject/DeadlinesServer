package com.ddanilyuk.userDemo1.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


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

    @Size(max = 8192)
    @JsonView(Views.defaultView.class)
    private String projectDescription;


    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonView(Views.defaultView.class)
    private List<Deadline> deadlines = new ArrayList<>();


    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(Views.projectView.class)
    private User projectOwner;


    @NotNull
    @JsonView(Views.usersView.class)
    private UUID projectOwnerUuid;


    @Column
    @JsonView(Views.projectView.class)
//    @ElementCollection(targetClass = User.class)
    @ManyToMany(mappedBy = "projectsAppended")
    private List<User> projectUsers = new ArrayList<>();


    @Column
    @ElementCollection(targetClass = UUID.class)
    @JsonView(Views.usersView.class)
    private List<UUID> projectUsersUuid = new ArrayList<>();


    @Column
    @JsonView(Views.defaultView.class)
    private long projectCreationTime;


    @Column
    @JsonView(Views.defaultView.class)
    private long projectExecutionTime;

    public Project() {
    }

    public Project(String projectName, String projectDescription) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;

        Date dateNow = new Date();
        projectCreationTime = dateNow.getTime();
    }

    public Project(String projectName, String projectDescription, User userOwner) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectOwner = userOwner;
        this.projectOwnerUuid = projectOwner.getUuid();
//        this. projectActiveUserIds = Collections.singletonList(projectActiveUser.getUserId());

        Date dateNow = new Date();
        projectCreationTime = dateNow.getTime();
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

    public List<UUID> getProjectActiveUsersUuid() {
        return projectUsersUuid;
    }

    public void setProjectActiveUsersUuid(List<UUID> projectActiveUsersId) {
        this.projectUsersUuid = projectActiveUsersId;
    }

    public UUID getProjectOwnerUuid() {
        return projectOwnerUuid;
    }

    public void setProjectOwnerUuid(UUID projectOwnerUuid) {
        this.projectOwnerUuid = projectOwnerUuid;
    }

    public User getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(User projectOwner) {
        this.projectOwnerUuid = projectOwner.getUuid();
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

//    @Override
//    public String toString() {
//        return "Project{" +
//                "id=" + projectId +
//                ", project_name='" + projectName + '\'' +
//                ", project_description='" + projectDescription + '\'' +
//                ", deadlines=" + deadlines +
//                '}';
//    }
}
