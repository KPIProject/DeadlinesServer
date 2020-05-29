package com.ddanilyuk.DeadlinesServer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Клас дедлайнів з усіма полями і описами
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "deadline")
@JsonIgnoreProperties("project")
public class Deadline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.deadlinesView.class)
    private int deadlineId;


    @JsonView(Views.deadlinesView.class)
    private String deadlineName;


    @Size(max = 8192)
    @JsonView(Views.deadlinesView.class)
    private String deadlineDescription;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    /**
     * Зберігається номер проекту до якого цей дедлайн відноситься
     */
    @NotNull
    @JsonView(Views.deadlinesView.class)
    private int deadlineProjectId;

    /**
     * Список виконавців проекту
     */
    @Column
    @JsonView(Views.deadlinesDetailView.class)
    @ManyToMany
    @JoinTable(
            name = "deadline_users",
            joinColumns = @JoinColumn(name = "deadline_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> deadlineExecutors = new ArrayList<>();


    @Column
    @JsonView(Views.defaultView.class)
    private long deadlineCreatedTime;


    @Column
    @JsonView(Views.defaultView.class)
    private long deadlineExecutionTime;


    @Column
    @JsonView(Views.defaultView.class)
    private Boolean completeMark;

    /**
     * Коли один із користувачів відмічає проект як завршений, сюди вписується його ім'я
     */
    @Column
    @JsonView(Views.defaultView.class)
    private String completedBy;


    public Deadline() {

    }

    /**
     *
     * @param deadlineName - ім'я дедлайну
     * @param deadlineDescription - опис дедлайну
     */
    public Deadline(String deadlineName, String deadlineDescription) {
        this.deadlineName = deadlineName;
        this.deadlineDescription = deadlineDescription;
        /**
         * Задавання часу створення дедлайну
         */
        Date dateNow = new Date();
        deadlineCreatedTime = dateNow.getTime();
    }

    /**
     *
     * @param deadlineName - назва дедлайну
     * @param deadlineDescription - опис дедлайну
     * @param project - проект в якому дедлайн
     */
    public Deadline(String deadlineName, String deadlineDescription, Project project) {
        this.deadlineName = deadlineName;
        this.deadlineDescription = deadlineDescription;
        this.project = project;
        this.deadlineProjectId = project.getProjectId();
        /**
         * Задавання часу створення дедлайну
         */
        Date dateNow = new Date();
        deadlineCreatedTime = dateNow.getTime();
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public Boolean getCompleteMark() {
        return completeMark;
    }

    public void setCompleteMark(Boolean doneMark) {
        this.completeMark = doneMark;
    }

    public long getDeadlineCreatedTime() {
        return deadlineCreatedTime;
    }

    public void setDeadlineCreatedTime(long deadlineCreatedTime) {
        this.deadlineCreatedTime = deadlineCreatedTime;
    }

    public long getDeadlineExecutionTime() {
        return deadlineExecutionTime;
    }

    public void setDeadlineExecutionTime(long deadlineExecutionTime) {
        this.deadlineExecutionTime = deadlineExecutionTime;
    }

    public List<User> getDeadlineExecutors() {
        return deadlineExecutors;
    }

    public void setDeadlineExecutors(List<User> deadlineExecutors) {
        this.deadlineExecutors = deadlineExecutors;
    }

    public int getDeadlineProjectId() {
        return deadlineProjectId;
    }

    public void setDeadlineProjectId(int deadlineProjectId) {
        this.deadlineProjectId = deadlineProjectId;
    }

    public int getDeadlineId() {
        return deadlineId;
    }

    public void setDeadlineId(int deadlineId) {
        this.deadlineId = deadlineId;
    }

    public String getDeadlineName() {
        return deadlineName;
    }

    public void setDeadlineName(String deadlineName) {
        this.deadlineName = deadlineName;
    }

    public String getDeadlineDescription() {
        return deadlineDescription;
    }

    public void setDeadlineDescription(String deadlineDescription) {
        this.deadlineDescription = deadlineDescription;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
