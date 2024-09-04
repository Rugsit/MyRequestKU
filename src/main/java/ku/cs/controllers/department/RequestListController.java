package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ku.cs.views.components.DefaultLabel;
import ku.cs.views.components.DefaultTableView;
import ku.cs.views.components.RouteButton;
import ku.cs.views.components.SquareImage;
import ku.cs.views.layouts.sidebar.SidebarController;
import ku.cs.models.Request;
import ku.cs.models.user.UserList;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestListController {
    @FXML private AnchorPane mainAnchorPane;
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
    @FXML private UserList userList;

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


        mainAnchorPane.getChildren().add(new SidebarController().getVBox());

        UserListFileDatasource userDataSource = new UserListFileDatasource("data","users.csv");
        userList = userDataSource.readData();
        System.out.println(userList);

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
        DefaultTableView<Request> reqTable = new DefaultTableView(requestTableView){
            @Override
            protected void handleCLick() {
                getTableView().setOnMouseClicked(e->{
                    Object selected = getTableView().getSelectionModel().getSelectedItem();
                    if(selected instanceof Request){
                        try {
                            FXRouter.goTo("department-staff-request",selected);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        };
        reqTable.getTableView().getColumns().clear();

        reqTable.addColumn("ชื่อ-นามสกุล","name");
        reqTable.addColumn("วันที่และเวลา","timeStamp");
        reqTable.addColumn("รหัสนิสิต","nisitId");
        reqTable.addColumn("ประเภทคำร้อง","requestType");
        reqTable.addColumn("สถานะคำร้อง","statusNow");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        Request request = new Request("ศิริสุข ทานธรรม", LocalDateTime.parse("2024/08/02 23:59:45", formatter), LocalDateTime.parse("2024/08/02 23:59:45", formatter), "6610402230", "คำร้องทั่วไป", "รอภาควิชา", "อนุมัติโดยอาจารย์ที่ปรึกษา");
        reqTable.getTableView().getItems().add(request);

    }



}
