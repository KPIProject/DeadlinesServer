package com.ddanilyuk.userDemo1.model;


import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "project")
//@JsonIgnoreProperties("user")

public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.defaultView.class)
    private int projectId;

    @JsonView(Views.defaultView.class)
    private String projectName;

    @JsonView(Views.defaultView.class)
    private String projectDescription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
//    @JsonBackReference
//    @JsonManagedReference
//    @JsonView(Views.projectView.class)
    @JsonView(Views.projectView.class)
    private User projectOwner;

//    @Transient
    @NotNull
    @JsonView(Views.defaultView.class)
    private UUID projectCreatorUuid;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonView(Views.defaultView.class)
    private List<Deadline> deadlines = new ArrayList<>();


    @Column
    @JsonView(Views.projectView.class)
    @ElementCollection(targetClass=User.class)
    @ManyToMany(mappedBy = "projectsAppended")
    private List<User> projectUsers = new ArrayList<>();

    @Column
    @ElementCollection(targetClass=UUID.class)
    @JsonView(Views.usersView.class)
//    @JsonView(Views.userWithoutProjectsAndPassword.class)
    private List<UUID> projectActiveUsersUuid = new ArrayList<>();


    public Project() {
    }

    public Project(String projectName, String projectDescription) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
    }

    public Project(String projectName, String projectDescription, User userOwner) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectOwner = userOwner;
        this.projectCreatorUuid = projectOwner.getUuid();
//        this. projectActiveUserIds = Collections.singletonList(projectActiveUser.getUserId());
    }

    public List<User> getProjectUsers() {
        return projectUsers;
    }

    public void setProjectUsers(List<User> projectUsers) {
        this.projectUsers = projectUsers;
    }

    public List<UUID> getProjectActiveUsersUuid() {
        return projectActiveUsersUuid;
    }

    public void setProjectActiveUsersUuid(List<UUID> projectActiveUsersId) {
        this.projectActiveUsersUuid = projectActiveUsersId;
    }

    public UUID getProjectCreatorUuid() {
        return projectCreatorUuid;
    }

    public void setProjectCreatorUuid(UUID projectCreatorUuid) {
        this.projectCreatorUuid = projectCreatorUuid;
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
