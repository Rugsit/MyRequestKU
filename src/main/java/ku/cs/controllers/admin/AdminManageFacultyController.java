package ku.cs.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.models.department.Department;
import ku.cs.models.department.DepartmentList;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.user.Admin;
import ku.cs.models.user.User;
import ku.cs.services.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminManageFacultyController {
    // store data what object that currently login now
    private Admin loginUser;


    // dataList for write data to file
    private FacultyList facultyList;
    private DepartmentList departmentList;

    // FXML Component
    @FXML
    private Stage currentPopupStage;
    @FXML
    private TextField searchTextField;
    @FXML
    private Tab facultyTab;
    @FXML
    private Tab departmentTab;
    @FXML
    private TabPane tabpane;
    @FXML
    private TableView<Faculty> facultyTableView;
    @FXML
    private TableView<Department> departmentTableView;
    @FXML
    private Button addButton;
    @FXML
    private AnchorPane mainAnchorPane;

    public void initialize() {

        SetTransition.setButtonBounce(addButton);

        Label placeHolder = new Label("ไม่พบข้อมูล");
        facultyTableView.setPlaceholder(placeHolder);
        departmentTableView.setPlaceholder(placeHolder);
        tabpane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue == facultyTab) {
                    loadFaculty();
                    searchFaculty(searchTextField.getText());
                } else if (newValue == departmentTab) {
                    loadDepartment();
                    searchDepartment(searchTextField.getText());
                }
            }
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Tab tab = tabpane.getSelectionModel().getSelectedItem();
            if (!newValue.trim().isEmpty()) {
                if (tab == facultyTab) {
                    searchFaculty(newValue);
                } else if (tab == departmentTab) {
                    searchDepartment(newValue);
                }
            } else {
                if (tab == facultyTab) {
                    facultyTableView.getItems().clear();
                    facultyTableView.getItems().addAll(facultyList.getFacultyList());
                } else if (tab == departmentTab) {
                    departmentTableView.getItems().clear();
                    departmentTableView.getItems().addAll(departmentList.getDepartments());
                }
            }
        });

        facultyTableView.setRowFactory(e -> {
            TableRow<Faculty> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Faculty faculty = row.getItem();
                    showEditPopup("faculty", faculty, facultyList);
                }
            });
            return row;
        });
        facultyTableView.setOnKeyPressed(e -> {
            Faculty faculty = facultyTableView.getSelectionModel().getSelectedItem();
            if (faculty != null && e.getCode() == KeyCode.ENTER) {
                showEditPopup("faculty", faculty, facultyList);
            }
        });

        departmentTableView.setRowFactory(e -> {
            TableRow<Department> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Department department = row.getItem();
                    showEditPopup("department", department, departmentList);;
                }
            });
            return row;
        });
        departmentTableView.setOnKeyPressed(e -> {
            Department department = departmentTableView.getSelectionModel().getSelectedItem();
            if (department != null && e.getCode() == KeyCode.ENTER) {
                showEditPopup("department", department, departmentList);
            }
        });
        loadFaculty();

    }

    public void setLoginUser(Admin loginUser) {
        this.loginUser = loginUser;
    }

    private void searchDepartment(String newValue) {
        Set<Department> filter = departmentList.getDepartments()
                .stream()
                .filter(department -> department.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                        department.getFaculty().toLowerCase().contains(newValue.toLowerCase()))
                .collect(Collectors.toSet());

        departmentTableView.getItems().clear();
        departmentTableView.getItems().addAll(filter);
    }

    private void searchFaculty(String newValue) {
        Set<Faculty> filter = facultyList.getFacultyList()
                .stream()
                .filter(faculty -> faculty.getName().toLowerCase().contains(newValue.toLowerCase()))
                .collect(Collectors.toSet());

        facultyTableView.getItems().clear();
        facultyTableView.getItems().addAll(filter);
    }

    public void loadDepartment() {
        departmentTableView.setVisible(true);
        facultyTableView.setVisible(false);

        departmentTableView.getItems().clear();
        departmentTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Department, String> department = new TableColumn<>("ภาควิชา");
        department.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Department, String> faculty = new TableColumn<>("คณะที่สังกัด");
        faculty.setCellValueFactory(new PropertyValueFactory<>("faculty"));
        TableColumn<Department, String> departmentId = new TableColumn<>("รหัสภาควิชา");
        departmentId.setCellValueFactory(new PropertyValueFactory<>("id"));

        departmentTableView.getColumns().setAll(department, faculty, departmentId);
        faculty.setSortType(TableColumn.SortType.ASCENDING);
        departmentTableView.getSortOrder().add(faculty);
        readFacultyOrDepartment("department");
        departmentTableView.getItems().addAll(departmentList.getDepartments());
    }

    public void loadFaculty() {
        departmentTableView.setVisible(false);
        facultyTableView.setVisible(true);

        facultyTableView.getItems().clear();
        facultyTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Faculty, String> faculty = new TableColumn<>("คณะ");
        faculty.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Faculty, String> facultyId = new TableColumn<>("รหัสคณะ");
        facultyId.setCellValueFactory(new PropertyValueFactory<>("id"));

        facultyTableView.getColumns().setAll(faculty, facultyId);
        readFacultyOrDepartment("faculty");
        facultyTableView.getItems().addAll(facultyList.getFacultyList());
    }

    private void readFacultyOrDepartment(String target) {
        if (target.equals("faculty")) {
            Datasource<FacultyList> datasource = new FacultyListFileDatasource("data");
            facultyList = datasource.readData();
        } else if (target.equals("department")) {
            Datasource<DepartmentList> datasource = new DepartmentListFileDatasource("data");
            departmentList = datasource.readData();
        }
    }

    private void showEditPopup(String target, Object object, Object objectList) {
        try {
            if (currentPopupStage == null || !currentPopupStage.isShowing()) {
                currentPopupStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/edit-" + target + ".fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                EditFormController controller = fxmlLoader.getController();
                controller.setCurrentObject(object);
                controller.setStage(currentPopupStage);
                controller.setListForWrite(objectList);
                controller.showOldFacultyDepartmentData();
                controller.setCurrentControllPage(this);
                controller.setChoiceBox();
                scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-confirm-edit-page-style.css").toExternalForm());
                currentPopupStage.setScene(scene);
                currentPopupStage.initModality(Modality.APPLICATION_MODAL);
                currentPopupStage.setTitle("Confirm");
                currentPopupStage.show();
            }
        } catch (IOException ee) {
            System.err.println("Error: " + ee.getMessage());
        }
    }

    public void resetSearch() {
        searchTextField.setText("");
    }

   @FXML
   public void addPopup() {
        Tab tab = tabpane.getSelectionModel().getSelectedItem();
        String addFacultyDepartment = tab == facultyTab ? "faculty" : "department";
        try {
            if (currentPopupStage == null || !currentPopupStage.isShowing()) {
                currentPopupStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/add-" + addFacultyDepartment + ".fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                AddFormController controller = fxmlLoader.getController();
                controller.setStage(currentPopupStage);
                if (addFacultyDepartment.equals("faculty")) controller.setListForWrite(facultyList);
                else controller.setListForWrite(departmentList);
                controller.setCurrentControllpage(this);
                controller.setChoiceBox();
                scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/error-confirm-edit-page-style.css").toExternalForm());
                currentPopupStage.setScene(scene);
                currentPopupStage.initModality(Modality.APPLICATION_MODAL);
                currentPopupStage.setTitle("Confirm");
                currentPopupStage.show();
            }
        } catch (IOException ee) {
            System.err.println("Error: " + ee.getMessage());
        }
    }

    @FXML
    protected void onLogoutClicked() {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
