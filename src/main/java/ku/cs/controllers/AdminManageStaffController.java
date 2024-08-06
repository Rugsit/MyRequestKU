package ku.cs.controllers;

import javafx.fxml.FXML;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class AdminManageStaffController {
    @FXML
    protected void goToAdminManageUsers() {
        try {
            FXRouter.goTo("admin-manage-users");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void goToAdminManageFaculty() {
        try {
            FXRouter.goTo("admin-manage-faculty-department");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }
}
