package com.ddanilyuk.DeadlinesServer.model;


@SuppressWarnings("ALL")
// обеження виводу інформації для джейсон
public final class Views {

    // Default output
    public interface defaultView {
    }
// при логіні виводиться та реєстрації
    public interface loginView extends defaultView {
    }

    public interface usersViewDebugVersion extends loginView, usersView {
    }

    // Output with List<Project> projectsCreated, List<Project> projectsAppended in User
    // UUID projectOwnerUuid, List<UUID> projectActiveUsersUuid in Project
    public interface usersView extends defaultView, deadlinesView {
    }

    // Output with User projectOwner, List<User> projectUsers in Project
    public interface projectView extends defaultView, deadlinesDetailView {
    }

    // All deadlines field without project and List<User> deadlineExecutors
    public interface deadlinesView {
    }

    // With List<User> deadlineExecutors
    // Calls in deadlineDetails
    public interface deadlinesDetailView extends defaultView, deadlinesView {
    }
}