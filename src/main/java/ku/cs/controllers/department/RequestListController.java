package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ku.cs.models.Session;
import ku.cs.models.department.DepartmentList;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.User;
import ku.cs.services.DepartmentListFileDatasource;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.views.components.DefaultLabel;
import ku.cs.views.components.DefaultTableView;
import ku.cs.views.components.RouteButton;
import ku.cs.views.components.SquareImage;
import ku.cs.views.layouts.sidebar.SidebarController;
import ku.cs.models.request.Request;
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
    private Session session;

    private void initRouteData(){
        Object object = FXRouter.getData();
        if(object instanceof Session){
            this.session = (Session) object;
        }else{
            session = null;
        }
    }

    @FXML
    public void initialize() {
        initRouteData();

        initLabel();
        initTableView();
        Image image = new Image(getClass().getResourceAsStream("/images/profile-test.png"));
        new SquareImage(userProfileImageView,image).setClipImage(100,100);

        mainAnchorPane.getChildren().add(new SidebarController("request-list",session).getVBox());

    }
    private void initLabel() {
        new DefaultLabel(pageTitleLabel);
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
        reqTable.addStyleSheet("/ku/cs/styles/department/pages/request-list/department-staff-request-list-table-stylesheet.css");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


//        Request request = new Request("ศิริสุข ทานธรรม", LocalDateTime.parse("2024/08/02 23:59:45", formatter), LocalDateTime.parse("2024/08/02 23:59:45", formatter), "6610402230", "คำร้องทั่วไป", "รอภาควิชา", "อนุมัติโดยอาจารย์ที่ปรึกษา");
//        UserListFileDatasource userListDatasource = new UserListFileDatasource("data","department-staff.csv");
//        UserList departmentStaff = userListDatasource.readData();
//
//        RequestListFileDatasource datasource = new RequestListFileDatasource("data");
//        RequestList requestList = datasource.readData();
//
//        for(Request request : requestList.getRequests()){
//
//        }
//        RequestList
//        reqTable.getTableView().getItems().add(request);


    }



}
