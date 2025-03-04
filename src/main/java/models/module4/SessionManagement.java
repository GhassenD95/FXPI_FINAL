package models.module4;

import models.module1.User;

public class SessionManagement {
    private static SessionManagement instance = null;
    private boolean isSessionActive = false;
    private User currentUser;

    private SessionManagement() {
    }
    public static SessionManagement getInstance() {
        if (instance == null) {
            instance = new SessionManagement();
        }
        return instance;
    }
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    public void logout() {
        this.currentUser = null;
    }
}
