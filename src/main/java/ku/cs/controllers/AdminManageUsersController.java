package ku.cs.controllers;

import javafx.fxml.FXML;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class AdminManageUsersController {
    @FXML
    protected void goToAdminManageStaff() {
        try {
            FXRouter.goTo("admin-manage-staff");
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
