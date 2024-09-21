package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.UUID;

public class AdvisorStudentListController {
    @FXML TableView requestListTableView;
    @FXML BorderPane borderPane;
    private Datasource<UserList> datasource;
    private UserList userlist;

    public void initialize() {
        loadStudents();
        showTable();
        loadRequestsDetail();
    }


    private void showTable() {
        requestListTableView.getColumns().clear();

        Label placeHolder = new Label("ไม่พบข้อมูล");
        requestListTableView.setPlaceholder(placeHolder);
        requestListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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

    private void loadRequestsDetail() {
        requestListTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    String viewPath = "/ku/cs/views/advisor-student-requests-pane.fxml";
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewPath));
                    Pane pane = fxmlLoader.load();
                    AdvisorStudentRequestsController controller = fxmlLoader.getController();

                    // Pass selected student details to the controller
                    if (newValue instanceof Student) {
                        Student selectedStudent = (Student) newValue;
                        String  selectedStudentId = selectedStudent.getId();

                        controller.setSelectedStudentId(selectedStudentId);
                        controller.setStudentName(selectedStudent.getName());
                    }

                    borderPane.setCenter(pane);
                    controller.setBorderPane(borderPane);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

}
