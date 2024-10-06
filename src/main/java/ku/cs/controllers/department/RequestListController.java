package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import ku.cs.models.Session;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.DepartmentUser;
import ku.cs.services.*;
import ku.cs.services.Observer;
import ku.cs.services.Theme;
import ku.cs.services.utils.DateTools;
import ku.cs.views.components.*;
import ku.cs.views.layouts.sidebar.SidebarController;
import ku.cs.models.request.Request;
import ku.cs.models.user.UserList;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class RequestListController implements Observer<HashMap<String, String>> {
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
    private RequestList requestList;
    private RequestListFileDatasource requestDatasource;

    private final String BASE_COLOR = "#FFFFFF";
    private final String HOVER_COLOR = "#a6a6a6";
    private final String BASE_LABEL_COLOR = DefaultLabel.DEFAULT_LABEL_COLOR;
    private final String DEFAULT_FONT = DefaultLabel.DEFAULT_FONT;
    private final String FALLBACK_FONT = DefaultLabel.FALLBACK_FONT;
    private Session session;
    private Theme theme = Theme.getInstance();
    @FXML private Button switchThemeButton;

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
        theme.clearObservers();
        initRouteData();
        requestDatasource = new RequestListFileDatasource("data");
        requestList = requestDatasource.readData();

        initLabel();
        initButton();
        initTableView();
//        Image image = new Image(getClass().getResourceAsStream("/images/profile-test.png"));
//        new SquareImage(userProfileImageView,image).setClipImage(100,100);

        mainAnchorPane.getChildren().add(new SidebarController("request-list",session).getVBox());

        theme.addObserver(this);
        theme.notifyObservers(theme.getTheme());
    }
    private void initLabel() {
        new DefaultLabel(pageTitleLabel);
    }
    private void initButton(){
        DefaultButton switchThemeBT = new DefaultButton(switchThemeButton,"#ABFFA4","#80BF7A","#000000"){
            @Override
            protected void handleClickEvent() {
                getButton().setOnMouseClicked(e->{
                    if(theme.getThemeName().equals("dark")){
                        theme.setTheme("default");
                    }else{
                        theme.setTheme("dark");
                    }
                    changeText(theme.getThemeName());
                });
            }
        };
        switchThemeBT.changeText(theme.getThemeName(),24,FontWeight.NORMAL);
        switchThemeBT.changeBackgroundRadius(10);
    }
    private void initTableView(){
        DefaultTableView<Request> reqTable = new DefaultTableView(requestTableView){
            @Override
            protected void handleClick() {
                getTableView().setOnMouseClicked(e->{
                    Object selected = getTableView().getSelectionModel().getSelectedItem();
                    if(selected instanceof Request){
                        try {
                            if(session != null){
                                session.setData(selected);
                            }else{
                                session = new Session();
                                session.setData(selected);
                            }
                            FXRouter.goTo("request-management",session);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
            @Override
            public void updateAction(){
                if(theme.getTheme() != null){
                    if(theme.getTheme().get("name").equalsIgnoreCase("dark")){
                        setStyleSheet("/ku/cs/styles/department/pages/request-list/dark-department-staff-request-list-table-stylesheet.css");
                    }else{
                        setStyleSheet("/ku/cs/styles/department/pages/request-list/department-staff-request-list-table-stylesheet.css");
                    }

                }
            }
        };
        reqTable.getTableView().getColumns().clear();

        reqTable.addColumn("ประเภทคำร้อง","requestType");
        reqTable.addColumn("รหัสนิสิต","nisitId");
        reqTable.addColumn("ชื่อ-นามสกุล","name");
//        reqTable.addColumn("วันที่และเวลา","timeStamp");
//        reqTable.addColumn("สถานะคำร้อง","statusNow");
        reqTable.getTableView().getColumns().add(newTimestampColumn("เวลาอัปเดต"));
        reqTable.getTableView().getColumns().add(newRequestStatusColumn("สถานะคำร้อง"));
        reqTable.addStyleSheet("/ku/cs/styles/department/pages/request-list/department-staff-request-list-table-stylesheet.css");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


//        Request request = new Request("ศิริสุข ทานธรรม", LocalDateTime.parse("2024/08/02 23:59:45", formatter), LocalDateTime.parse("2024/08/02 23:59:45", formatter), "6610402230", "คำร้องทั่วไป", "รอภาควิชา", "อนุมัติโดยอาจารย์ที่ปรึกษา");

        UserListFileDatasource userDatasource = new UserListFileDatasource("data","student.csv");
        UserList userList = userDatasource.readData();
        RequestList departmentRequest = new RequestList();
        if(session != null && (session.getUser() != null && session.getUser() instanceof DepartmentUser)){;
            for(Request request : requestList.getRequests()){
                UUID requestDepartmentUUID = ((DepartmentUser)userList.findUserByUUID(request.getOwnerUUID())).getDepartmentUUID();
                if(requestDepartmentUUID.equals(((DepartmentUser) session.getUser()).getDepartmentUUID())){
                    departmentRequest.addRequest(request);
                }
            }
            reqTable.getTableView().getItems().addAll(departmentRequest.getRequests());
        }else{
            reqTable.getTableView().getItems().addAll(requestList.getRequests());
        }
        theme.addObserver(reqTable);
    }
    private TableColumn<Request,?> newRequestStatusColumn(String colName){
        TableColumn<Request,VBox> column = new TableColumn<>(colName);
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE
        column.setCellFactory(c -> new TableCell<>(){
            private VBox vBox = new VBox();
            private DefaultLabel line1 = new DefaultLabel("");
            private DefaultLabel line2 = new DefaultLabel("");
            {
                vBox.setAlignment(Pos.CENTER);
                line1.changeText("",20, FontWeight.NORMAL);
                line2.changeText("",18, FontWeight.NORMAL);
                if(theme.getTheme() != null){
                    line1.changeLabelColor(theme.getTheme().get("textColor"));
                    line2.changeLabelColor(theme.getTheme().get("textColor"));
                }
            }
            @Override
            protected void updateItem(VBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    Request request = getTableView().getItems().get(getIndex());
                    String statusNow = request.getStatusNow();

                    line1.changeText(statusNow);
                    if(statusNow.equalsIgnoreCase("ใบคำร้องใหม่")){
                        line1.changeLabelColor("green");
                    }
                    String statusNext = request.getStatusNext();
                    line2.changeText(statusNext);

                    vBox.getChildren().clear();
                    vBox.getChildren().addAll(line1, line2);

                    setGraphic(vBox);
                }
            }
        });
        return column;
    }
    private TableColumn<Request,?> newTimestampColumn(String colName){
        TableColumn<Request,VBox> column = new TableColumn<>(colName);
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE
        column.setCellFactory(c -> new TableCell<>(){
            private VBox vBox = new VBox();
            private DefaultLabel line1 = new DefaultLabel("");
            private DefaultLabel line2 = new DefaultLabel("");
            {
                vBox.setAlignment(Pos.CENTER);
                line1.changeText("",20, FontWeight.NORMAL);
                line2.changeText("",18, FontWeight.NORMAL);
                if(theme.getTheme() != null){
                    line1.changeLabelColor(theme.getTheme().get("textColor"));
                    line2.changeLabelColor(theme.getTheme().get("textColor"));
                }
            }
            @Override
            protected void updateItem(VBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    Request request = getTableView().getItems().get(getIndex());
                    String statusNow = DateTools.localDateTimeToFormatString("yyyy/MM/dd HH:mm", request.getTimeStamp());
                    line1.changeText(statusNow);

                    String statusNext = "สร้าง " + DateTools.localDateTimeToFormatString("yyyy/MM/dd", request.getDate());
                    line2.changeText(statusNext);

                    vBox.getChildren().clear();
                    vBox.getChildren().addAll(line1, line2);

                    setGraphic(vBox);
                }
            }
        });
        return column;
    }
    @FXML
    private void goToSetting() {
        
    }

    @Override
    public void update(HashMap<String, String> data) {
        mainAnchorPane.setStyle(mainAnchorPane.getStyle()+"-fx-background-color: " + data.get("secondary") + ";");
    }
}
