package ku.cs.controllers.department;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
import ku.cs.views.layouts.theme.themeSettingPopup;

import java.io.IOException;
import java.util.*;

public class RequestListController implements Observer<HashMap<String, String>> {
    @FXML private AnchorPane mainAnchorPane;
    @FXML private Label pageTitleLabel;

    @FXML private TableView<Request> requestTableView;
    private DefaultTableView<Request> reqTable;
    private RequestList requestList;
    private RequestList departmentRequest;
    private RequestListFileDatasource requestDatasource;
    private UserListFileDatasource userDatasource;
    private UserList userList;

    private Session session;
    private Theme theme = Theme.getInstance();
    @FXML private Button switchThemeButton;
    @FXML private HBox tableTopHBox;
    private DefaultSearchBox<Request> searchBox;

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
        userDatasource = new UserListFileDatasource("data","student.csv");
        requestDatasource = new RequestListFileDatasource("data");

        initLabel();
        initButton();
        initTableView();
        refreshTableData();
        initTableTopHBox();

        mainAnchorPane.getChildren().add(new SidebarController("request-list",session).getVBox());

        theme.addObserver(this);
        theme.notifyObservers(theme.getTheme());
    }
    private void initLabel() {
        new DefaultLabel(pageTitleLabel);
    }
    private void initButton(){
        DefaultButton switchThemeBT = new DefaultButton(switchThemeButton,"white","white","white"){
            private Image defaultImage = new Image(getClass().getResourceAsStream("/images/icons/sun-icon.png"));
            private Image darkImage = new Image(getClass().getResourceAsStream("/images/icons/moon-icon.png"));
            @Override
            public void update(HashMap<String, String> data) {
                String curTheme = data.get("name");
                Image iconImage;
                if(curTheme.equals("dark")){
                    iconImage = darkImage;
                    baseColorHex = "#2731B7";
                    hoverColorHex = "#212A9E";
                    changeColor(baseColorHex);
                }else{
                    iconImage = defaultImage;
                    baseColorHex = "#69EEFF";
                    hoverColorHex = "#62DCEC";
                    changeColor(baseColorHex);
                }
                setImage(iconImage,30,30);
                setButtonSize(50,50);
                changeBackgroundRadius(100);
                setMaxSize(50,50);
            }


            @Override
            protected void handleClickEvent() {
                getButton().setOnMouseClicked(e -> {
                    mainAnchorPane.getChildren().addLast(new themeSettingPopup() {
                        @Override
                        protected void handleSecondButton() {
                            secondButton.setOnMouseClicked(e -> {
                                mainAnchorPane.getChildren().removeLast();
                            });
                        }
                    });
                });
            }
        };
        switchThemeBT.changeText("");
    }
    private void initTableTopHBox(){
        Map<String,StringExtractor<Request>> filterList= new LinkedHashMap<>();
        filterList.put("ประเภทคำร้อง", obj -> obj.getRequestType());
        filterList.put("รหัสนิสิต", obj -> obj.getNisitId());
        filterList.put("ชื่อ-นามสกุล", obj -> obj.getName());
        filterList.put("เวลาอัปเดต", new StringExtractor<>() {
            @Override
            public String extract(Request obj) {
                String timestamp = DateTools.localDateTimeToFormatString("yyyy/MM/dd HH:mm", obj.getTimeStamp());
                return timestamp;
            }
        });
        filterList.put("วันที่สร้าง", new StringExtractor<>() {
            @Override
            public String extract(Request obj) {
                String timeCreated = "สร้าง " + DateTools.localDateTimeToFormatString("yyyy/MM/dd", obj.getDate());
                return timeCreated;
            }
        });
        filterList.put("สถานะปัจจุบัน", obj -> obj.getStatusNow());
        filterList.put("สถานะถัดไป", obj -> obj.getStatusNext());

        Map<String,Comparator<Request>> comparatorList= new LinkedHashMap<>();
        Comparator<Request> requestTimeComparator = new Comparator<Request>() {
            @Override
            public int compare(Request o1, Request o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        };
        Comparator<Request> requestTimestampComparator = new Comparator<Request>() {
            @Override
            public int compare(Request o1, Request o2) {
                return o1.getTimeStamp().compareTo(o2.getTimeStamp());
            }
        };
        comparatorList.put("เวลาอัปเดต",requestTimestampComparator);
        comparatorList.put("วันที่สร้าง",requestTimeComparator);

        searchBox = new DefaultSearchBox<>(departmentRequest.getRequests(), filterList,comparatorList,652,50){
            @Override
            protected void initialize(){
                super.initialize();
                //FORCE SORT LATEST UPDATE
                filterBox.getSelectionModel().select(4);//IDX -> เวลาอัปเดต
                compareBox.getSelectionModel().selectLast();//IDX DESCENDING
            }
            @Override
            protected void searchAction(){
                refreshSearchTableData(getQueryItems());
            }
        };
        tableTopHBox.getChildren().addAll(searchBox);
    }
    private void initTableView(){
        reqTable = new DefaultTableView(requestTableView){
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
        reqTable.getTableView().getColumns().add(newTimestampColumn("เวลาอัปเดต"));
        reqTable.getTableView().getColumns().add(newRequestStatusColumn("สถานะคำร้อง"));
        reqTable.addStyleSheet("/ku/cs/styles/department/pages/request-list/department-staff-request-list-table-stylesheet.css");

        theme.addObserver(reqTable);
    }

    private void refreshTableData(){
        reqTable.getTableView().getItems().clear();
        userList = userDatasource.readData();
        requestList = requestDatasource.readData();
        departmentRequest = new RequestList();

        if(session != null && (session.getUser() != null && session.getUser() instanceof DepartmentUser)){;
            for(Request request : requestList.getRequests()){
                UUID requestDepartmentUUID = ((DepartmentUser)userList.findUserByUUID(request.getOwnerUUID())).getDepartmentUUID();
                if(requestDepartmentUUID.equals(((DepartmentUser) session.getUser()).getDepartmentUUID())){
                    departmentRequest.addRequest(request);
                }
            }
        }else{
            departmentRequest.concatenate(requestList);
        }
        reqTable.getTableView().getItems().addAll(departmentRequest.getRequests());
        if(searchBox!=null){
            searchBox.setSearchItems(departmentRequest.getRequests());
        }
    }
    private void refreshSearchTableData(Collection<Request> requests){
        reqTable.getTableView().getItems().clear();
        reqTable.getTableView().getItems().addAll(requests);
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
                    line1.update(theme.getTheme());
                    line2.update(theme.getTheme());
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
                    }else{
                        line1.changeLabelColor("black");
                        if(theme.getTheme() != null){
                            line1.changeLabelColor(theme.getTheme().get("textColor"));
                        }
                    }
                    String statusNext = request.getStatusNext();
                    line2.changeText(statusNext);
                    if(statusNext.contains("ครบถ้วน")){
                        line2.changeLabelColor("green");
                    }else if (statusNext.contains("ปฏิเสธ")){
                        line2.changeLabelColor("red");
                    }else if (statusNext.contains("ส่งต่อ")){
                        line2.changeLabelColor("orange");
                    }else{
                        line2.changeLabelColor("black");
                        if(theme.getTheme() != null){
                            line2.changeLabelColor(theme.getTheme().get("textColor"));
                        }
                    }

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
                    line1.update(theme.getTheme());
                    line2.update(theme.getTheme());
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

    @Override
    public void update(HashMap<String, String> data) {
        mainAnchorPane.setStyle(mainAnchorPane.getStyle()+"-fx-background-color: " + data.get("secondary") + ";");
    }
}
