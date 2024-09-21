package ku.cs.controllers;

import ku.cs.models.user.User;

public interface ParentController {
    void setLoginUser(User loginUser);
    void loadProfile();
}
