package com.ddanilyuk.DeadlinesServer.model;

import java.util.List;

/**
 * Клас створення проекту зі списком всіх юзерів
 */
public class ComplaintProject {
    public Project project;
    public List<String> usersToAdd;

    public ComplaintProject(Project project, List<String> usersToAdd) {
        this.project = project;
        this.usersToAdd = usersToAdd;
    }
}
