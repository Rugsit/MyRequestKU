package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import ku.cs.controllers.department.components.DefaultLabel;
import ku.cs.controllers.department.components.RouteButton;
import ku.cs.controllers.department.components.SquareImage;
import ku.cs.models.Request;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class RequestListController {
    @FXML private Label pageTitleLabel;

    @FXML private ImageView topSideImageView;
    @FXML private VBox routeButtonVBox;
    @FXML private Button requestSideButton;
    @FXML private Button nisitManagementSideButton;
    @FXML private Button approverManagementSideButton;
    @FXML private Button nisitAdvisorManagementSideButton;

    @FXML private VBox userProfileVBox;
    @FXML private ImageView userProfileImageView;
    @FXML private Button logoutButton;

    @FXML private TextField seachTextField;
    @FXML private TableView<Request> requestTableView;

    private final String BASE_COLOR = "#FFFFFF";
    private final String HOVER_COLOR = "#a6a6a6";
    private final String BASE_LABEL_COLOR = DefaultLabel.DEFAULT_LABEL_COLOR;
    private final String DEFAULT_FONT = DefaultLabel.DEFAULT_FONT;
    private final String FALLBACK_FONT = DefaultLabel.FALLBACK_FONT;

    @FXML
    public void initialize() {
        initLabel();
        initSidebar();
        initTableView();
        Image image = new Image(getClass().getResourceAsStream("/images/profile-test.png"));
        new SquareImage(userProfileImageView,image).setClipImage(100,100);

    }
    private void initLabel() {
        new DefaultLabel(pageTitleLabel);
    }
    private void initSidebar(){
        new RouteButton(requestSideButton,"department-staff-request-list",HOVER_COLOR,HOVER_COLOR,BASE_LABEL_COLOR);
        new RouteButton(nisitAdvisorManagementSideButton,"department-staff-nisit-advisor-management",BASE_COLOR,HOVER_COLOR,BASE_LABEL_COLOR);
        new RouteButton(nisitManagementSideButton,"department-staff-nisit-management",BASE_COLOR,HOVER_COLOR,BASE_LABEL_COLOR);
        new RouteButton(approverManagementSideButton,"department-staff-approver-list",BASE_COLOR,HOVER_COLOR,BASE_LABEL_COLOR);
        new RouteButton(logoutButton,"login",BASE_COLOR,HOVER_COLOR,BASE_LABEL_COLOR);

    }
    private void initTableView(){
        requestTableView.getColumns().clear();
        TableColumn<Request,String> nameColumn = new TableColumn("ชื่อ-นามสกุล");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Request,String> timeStampColumn = new TableColumn("วันที่และเวลา");
        timeStampColumn.setCellValueFactory(new PropertyValueFactory<>("timeStamp"));
        TableColumn<Request,String> nisitIdColumn = new TableColumn("รหัสนิสิต");
        nisitIdColumn.setCellValueFactory(new PropertyValueFactory<>("nisitId"));
        TableColumn<Request,String> requestTypeColumn = new TableColumn("ประเภทคำร้อง");
        requestTypeColumn.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        TableColumn<Request,String> requestStatusColumn = new TableColumn("สถานะคำร้อง");
        requestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusNow"));

        requestTableView.getColumns().addAll(nameColumn,timeStampColumn,nisitIdColumn,requestTypeColumn,requestStatusColumn);
        Request request = new Request("ศิริสุข ทานธรรม", "23:59", "02/08/2024", "6610402230", "คำร้องทั่วไป", "รอภาควิชา", "อนุมัติโดยอาจารย์ที่ปรึกษา");
        requestTableView.getItems().add(request);

        requestTableView.setStyle(
                "-fx-font-family: " + DEFAULT_FONT + ";"
        );
        requestTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        requestTableView.getStylesheets().add(getClass().getResource("/ku/cs/styles/department/pages/request-list/department-staff-request-list-table-stylesheet.css").toExternalForm());
        requestTableView.setOnMouseClicked(e->{
            Request selected = requestTableView.getSelectionModel().getSelectedItem();
            if(selected != null){
                try {
                    FXRouter.goTo("department-staff-request",selected);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }



}