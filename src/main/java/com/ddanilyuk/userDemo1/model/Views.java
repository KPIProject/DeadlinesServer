package com.ddanilyuk.userDemo1.model;


public final class Views {
    public interface defaultView {}

    public interface usersView extends defaultView, deadlinesView {}

    public interface projectView extends defaultView, deadlinesView {}

    public interface projectUsersView {}


    public interface deadlinesView {}
}
