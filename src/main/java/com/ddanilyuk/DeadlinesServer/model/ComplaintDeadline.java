package com.ddanilyuk.DeadlinesServer.model;

import java.util.List;

public class ComplaintDeadline {
    public Deadline deadline;
    /**
     * Список користувачів які потрібно додати до проекту
     */
    public List<String> usersToAdd;

    public ComplaintDeadline(Deadline deadline, List<String> usersToAdd) {
        this.deadline = deadline;
        this.usersToAdd = usersToAdd;
    }
}
