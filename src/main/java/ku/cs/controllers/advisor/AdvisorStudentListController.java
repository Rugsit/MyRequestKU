package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ku.cs.models.request.Request;
import ku.cs.models.user.Advisor;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.Datasource;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdvisorStudentListController {
    @FXML
    private TableView<User> requestListTableView;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField searchTextField;

    private Datasource<UserList> datasource;
    private UserList userlist;
    private AdvisorPageController advisorPageController;

    public void initialize() {
        loadStudents();
        showTable();
        loadRequestsDetail();
    }



    public void setAdvisorPageController(AdvisorPageController advisorPageController) {
        this.advisorPageController = advisorPageController;
    }

    private void showTable() {
        requestListTableView.getColumns().clear();
        Label placeHolder = new Label("ไม่พบข้อมูล");
        requestListTableView.setPlaceholder(placeHolder);
        requestListTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create and configure name column
        TableColumn<User, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMinWidth(531);

        // Create and configure ID column
        TableColumn<User, String> idColumn = new TableColumn<>("รหัสนิสิต");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setMinWidth(400);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                search();
            } else {
                loadStudents();
            }
        });

        requestListTableView.getColumns().add(nameColumn);
        requestListTableView.getColumns().add(idColumn);
    }

    public void initializeController() {
        if (advisorPageController == null) {
            throw new IllegalStateException("advisorPageController has not been set.");
        }
        loadStudents();
        System.out.println(advisorPageController.getAdvisorUUID());
    }

    private void loadStudents() {
        datasource = new UserListFileDatasource("data", "student.csv");
        userlist = datasource.readData();
        requestListTableView.getItems().clear();

        for (User user : userlist.getUsers()) {
            if (user instanceof Student) {
                Student student = (Student) user;
                if (advisorPageController != null && advisorPageController.getAdvisorUUID().equals(student.getAdvisor())) {
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

                    if (newValue instanceof Student) {
                        Student selectedStudent = (Student) newValue;
                        controller.setSelectedStudentId(selectedStudent.getId());
                        controller.setStudentName(selectedStudent.getName());
                        controller.setStudent(selectedStudent);
                        controller.initializeStudentRequests();
                        controller.setAdvisorPageController(advisorPageController);
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

    private void search() {
        ArrayList<User> users = new ArrayList<>();
        for (User user : userlist.getUsers()) {
            if (user instanceof Student) {
                Student student = (Student) user;
                // Add students who match the advisor
                if (advisorPageController != null && advisorPageController.getAdvisorUUID().equals(student.getAdvisor())) {
                    users.add(student);
                }
            }
        }

        Set<User> filter = users.stream()
                .filter(user -> user.getName().toLowerCase().contains(searchTextField.getText().toLowerCase()) ||
                        user.getId().contains(searchTextField.getText()))
                .collect(Collectors.toSet());

        requestListTableView.getItems().clear();
        requestListTableView.getItems().addAll(filter);
    }
}
