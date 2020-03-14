package com.ddanilyuk.userDemo1.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "project")
@JsonIgnoreProperties("user")

public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;

    private String projectName;

    private String projectDescription;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

//    @Transient
    @NotNull
    private UUID projectCreatorUuid;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Deadline> deadlines = new ArrayList<>();

    // Todo maybe use User not userIds
//    private List<User> projectUsers = new ArrayList<>();

    @Column
    @ElementCollection(targetClass=UUID.class)
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
        this.user = userOwner;
        this.projectCreatorUuid = user.getUuid();
//        this. projectActiveUserIds = Collections.singletonList(projectActiveUser.getUserId());
    }

    public List<UUID> getProjectActiveUsersId() {
        return projectActiveUsersUuid;
    }

    public void setProjectActiveUsersId(List<UUID> projectActiveUsersId) {
        this.projectActiveUsersUuid = projectActiveUsersId;
    }

    public UUID getProjectCreatorUuid() {
        return projectCreatorUuid;
    }

    public void setProjectCreatorUuid(UUID projectCreatorUuid) {
        this.projectCreatorUuid = projectCreatorUuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
