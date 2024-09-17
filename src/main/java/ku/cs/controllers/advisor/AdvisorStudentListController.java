package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.UserListFileDatasource;

public class AdvisorStudentListController {
    @FXML TableView requestListTableView;
    @FXML BorderPane borderPane;
    private Datasource<UserList> datasource;
    private UserList userlist;

    public void initialize() {
        loadStudents();
        showTable();
    }


    private void showTable() {
        requestListTableView.getColumns().clear();

        // Create and configure name column
        TableColumn<User, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name")); // Bind to getName() method
        nameColumn.setMinWidth(531);

        // Create and configure ID column
        TableColumn<User, String> idColumn = new TableColumn<>("รหัสนิสิต");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setMinWidth(400);

        requestListTableView.getColumns().add(nameColumn);
        requestListTableView.getColumns().add(idColumn);

    }

    private void loadStudents() {
        datasource = new UserListFileDatasource("data", "student.csv");

        userlist = datasource.readData();
        requestListTableView.getItems().clear();

        for (User user : userlist.getUsers()) {
            if (user instanceof Student) {
                Student student = (Student) user;
                if (AdvisorPageController.getAdvisorUUID().equals(student.getAdvisor())) {
                    requestListTableView.getItems().add(student);
                }
            }
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

}
