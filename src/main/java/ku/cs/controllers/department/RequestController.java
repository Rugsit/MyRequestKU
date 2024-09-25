package ku.cs.controllers.department;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ku.cs.controllers.requests.ErrorGeneralRequestFormController;
import ku.cs.controllers.requests.information.MainInformationController;
import ku.cs.models.Session;
import ku.cs.models.department.Department;
import ku.cs.models.department.DepartmentList;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.request.*;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.approver.*;
import ku.cs.models.user.DepartmentUser;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.*;
import ku.cs.services.utils.DateTools;
import ku.cs.views.components.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class RequestController {
    @FXML private Label pageTitleLabel;
    @FXML private Button backButton;
    @FXML private StackPane mainStackPane;
    @FXML private VBox dataVBox;
    @FXML private VBox requestInfoVBox;
    @FXML private HBox requestMenuHBox;
    private TableView<Approver> requestApproverTableView;
    private DefaultTableView<Approver> tableView;
    @FXML private VBox mainEditorVBox;
    @FXML private VBox statusVBox;
    @FXML private VBox editorVBox;
    private DefaultLabel reqType,reqNisitId,reqName,reqCreateTime,reqTimestamp;
    private DefaultButton addApproverButton, requestInfoButton, approveButton, rejectButton;

    private Request request;
    private User requestOwner;
    private int sumApprover = 0;
    private int sumAprroved = 0;
    private int sumApproverFaculty = 0;
    private int sumApprovedFaculty = 0;
    private boolean goToFaculty = false;
    private boolean haveReject = false;
    private boolean showEdit;
    private boolean editMode;

    @FXML
    private Stage currentPopupStage;

    private UserListFileDatasource studentDatasource;
    private DepartmentListFileDatasource departmentDatasource;
    private FacultyListFileDatasource facultyDatasource;
    private RequestListFileDatasource requestDatasource;
    private ApproverListFileDatasource approverDatasource;
    private UserList studentList;
    private DepartmentList departmentList;
    private FacultyList facultyList;
    private ApproverList approverList;
    private RequestList requestList;
    private ApproverList filterApproverList;

    private Approver selectedApprover;
    private UploadImageStack sinatureUploader;

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

        studentDatasource = new UserListFileDatasource("data","student.csv");
        studentList = studentDatasource.readData();
        departmentDatasource = new DepartmentListFileDatasource("data");
        departmentList = departmentDatasource.readData();
        facultyDatasource = new FacultyListFileDatasource("data");
        facultyList = facultyDatasource.readData();
        approverDatasource = new ApproverListFileDatasource("request-approver");
        approverList = approverDatasource.readData();
        requestDatasource = new RequestListFileDatasource("data");


        initLabel();
        initButton();

        if(session != null && session.getData() instanceof Request){
            initDataVBox();
            refreshAllData();

        }


    }
    private void initLabel(){
        new DefaultLabel(pageTitleLabel);
    }
    private void initButton(){
        new RouteButton(backButton,"department-staff-request-list","transparent","#a6a6a6","#000000");
    }
    private void initRequestInfoVBox(){
        requestInfoVBox.getChildren().clear();
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);

        //IMAGE
        VBox imageVBox = new VBox();
        imageVBox.setAlignment(Pos.CENTER);
        imageVBox.setPrefWidth(384);
        SquareImage image = new SquareImage(new ImageView());

        if(!requestOwner.getAvatar().equalsIgnoreCase("no-image")){
            ImageDatasource imageDatasource = new ImageDatasource("users");
            image.setImage(imageDatasource.openImage(requestOwner.getAvatar()));
        }

        image.getImageView().setFitHeight(180);
        image.getImageView().setFitWidth(180);
        image.setClipImage(50,50);
        imageVBox.getChildren().add(image.getImageView());


        //INFO
        VBox infoVBox = new VBox();
        infoVBox.setAlignment(Pos.CENTER_LEFT);
        infoVBox.setPrefWidth(398);
        reqType = new DefaultLabel("");
        reqType.changeText("คำร้อง"  + request.getRequestType(),32, FontWeight.BOLD);
        infoVBox.getChildren().add(reqType);

        HBox container;
        DefaultLabel prefix;

        container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setSpacing(20);
        prefix = new DefaultLabel("");
        prefix.changeText("รหัสนิสิต",28 ,FontWeight.BOLD);
        reqNisitId = new DefaultLabel("");
        reqNisitId.changeText(request.getNisitId(),28, FontWeight.NORMAL);
        container.getChildren().addAll(prefix,reqNisitId);
        infoVBox.getChildren().add(container);


        container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setSpacing(20);
        prefix = new DefaultLabel("");
        prefix.changeText("ชื่อ",28 ,FontWeight.BOLD);
        reqName = new DefaultLabel("");
        reqName.changeText(request.getName(),28, FontWeight.NORMAL);
        container.getChildren().addAll(prefix,reqName);
        infoVBox.getChildren().add(container);


        container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setSpacing(20);
        prefix = new DefaultLabel("");
        prefix.changeText("สร้างคำร้อง",28 ,FontWeight.BOLD);
        reqCreateTime = new DefaultLabel("");
        reqCreateTime.changeText(
                DateTools.localDateTimeToFormatString("yyyy/MM/dd HH:mm", request.getDate()),
                28,
                FontWeight.NORMAL);
        container.getChildren().addAll(prefix,reqCreateTime);
        infoVBox.getChildren().add(container);


        container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setSpacing(20);
        prefix = new DefaultLabel("");
        prefix.changeText("อัพเดทล่าสุด",28 ,FontWeight.BOLD);
        reqTimestamp = new DefaultLabel("");
        reqTimestamp.changeText(DateTools.localDateTimeToFormatString("yyyy/MM/dd HH:mm", request.getTimeStamp()),
                28,
                FontWeight.NORMAL);
        container.getChildren().addAll(prefix,reqTimestamp);
        infoVBox.getChildren().add(container);

        hbox.getChildren().addAll(imageVBox,infoVBox);

        requestInfoVBox.getChildren().add(hbox);

    }

    private void initRequestMenuHBox(){
        requestMenuHBox.getChildren().clear();
//        requestMenuHBox.setStyle("-fx-background-color: red");
        HBox menuHBox = new HBox();
        menuHBox.setAlignment(Pos.CENTER);
        menuHBox.setSpacing(20);

        addApproverButton = new DefaultButton("#7FE8FF","#a6a6a6","#000000");
        addApproverButton.changeText("เพิ่มผู้อนุมัติ",28, FontWeight.NORMAL);

        requestInfoButton = new DefaultButton("#FFA1D9","#a6a6a6","#000000"){
          @Override
          protected void handleClickEvent(){
              button.setOnMouseClicked(e ->{
                  try {
                      if (request == null || requestOwner == null) throw new NullPointerException("Request or Request owner is null");
                      if (currentPopupStage == null || !currentPopupStage.isShowing()) {
                          currentPopupStage = new Stage();
                          FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/main-information.fxml"));
                          AnchorPane scene = fxmlLoader.load();
                          MainInformationController controller = fxmlLoader.getController();
                          controller.setRequest(request);
                          controller.setLoginUser(requestOwner);
                          controller.setBackPageVisible(false);
                          controller.setCurrentPopupStage(currentPopupStage);
                          controller.initializeMainInformation();
                          if (request instanceof GeneralRequestForm) {
                              controller.setTitleLabel("ใบคำร้องทั่วไป");
                          } else if (request instanceof RegisterRequestForm) {
                              controller.setTitleLabel("คำร้องขอลงทะเบียน");
                          } else if (request instanceof AcademicLeaveRequestForm) {
                              controller.setTitleLabel("ใบคำร้องขอลาพักการศึกษา");
                          } else if (request instanceof Ku1AndKu3RequestForm) {
                              if (request.getRequestType().equalsIgnoreCase("KU1")) {
                                  controller.setTitleLabel("แบบลงทะเบียนเรียน KU1");
                              } else {
                                  controller.setTitleLabel("แบบขอเปลี่ยนแปลงการลงทะเบียนเรียน KU3");
                              }
                          }
                          scene.getStylesheets().add(getClass().getResource("/ku/cs/styles/general-request-form-page-style.css").toExternalForm());
//                          currentPopupStage.setTitle("Request");
//                          currentPopupStage.setScene(scene);
//                          currentPopupStage.initModality(Modality.APPLICATION_MODAL);
//                          currentPopupStage.setTitle("Error");
//                          currentPopupStage.show();

                          mainStackPane.getChildren().add(new BlankPopupStack() {

                              @Override
                              protected void initPopupView() {
                                  declineButton.setText("ปิด");
                                  HBox lineEnd = new HBox(declineButton);
                                  VBox mainBox = new VBox(scene);
                                  mainBox.setMaxWidth(600);
                                  mainBox.setMaxHeight(620);
                                  mainBox.setStyle("-fx-background-color: white; -fx-background-radius: 50px");
                                  lineEnd.setAlignment(Pos.CENTER);
                                  scene.setStyle("-fx-pref-height: 620px");
                                  stackPane.getChildren().add(mainBox);
                                  mainBox.getChildren().add(lineEnd);
                                  VBox.setMargin(lineEnd,new Insets(20,0,20,0));
                              }

                              @Override
                              protected void handleAcceptButton() {
                                  acceptButton.setOnMouseClicked(e->{
                                      mainStackPane.getChildren().removeLast();
                                  });
                              }

                              @Override
                              protected void handleDeclineButton() {
                                  declineButton.setOnMouseClicked(e->{
                                      mainStackPane.getChildren().removeLast();
                                  });
                              }
                          });
                      }
                  } catch (IOException ee) {
                      System.err.println("Error: " + ee.getMessage());
                  }
              });
          }
        };
        requestInfoButton.changeText("ข้อมูลคำร้อง",28, FontWeight.NORMAL);
        menuHBox.getChildren().addAll(addApproverButton,requestInfoButton);

        HBox controlHBox = new HBox();
        controlHBox.setAlignment(Pos.CENTER);
        controlHBox.setSpacing(20);
        approveButton = new DefaultButton("transparent","#a6a6a6","#00DE59"){
          @Override
          protected void handleClickEvent(){
              button.setOnMouseClicked(e->{
                  onRequestApproveButton();
              });
          }
        };
        approveButton.changeText("อนุมัติ",28, FontWeight.NORMAL);

        rejectButton = new DefaultButton("transparent","#a6a6a6","#FF5D5D");
        rejectButton.changeText("ไม่อนุมัติ",28, FontWeight.NORMAL);

        DefaultButton reloadButton = new DefaultButton("transparent","#a6a6a6","#FF5D5D"){
            @Override
            protected void handleClickEvent(){
                button.setOnMouseClicked(e->{
                    refreshAllData();
                });
            }
        };
        reloadButton.changeText("R",28, FontWeight.NORMAL);

        controlHBox.getChildren().addAll(rejectButton,approveButton,reloadButton);

        requestMenuHBox.getChildren().addAll(menuHBox,controlHBox);
        requestMenuHBox.setAlignment(Pos.CENTER);
        requestMenuHBox.setSpacing(200);
    }

    private void initDataVBox(){
        dataVBox.getChildren().add(new VBox());
    }

    private void initRequestApproverTableView(){
        requestApproverTableView = new TableView();
        VBox vBox = (VBox) dataVBox.getChildren().getLast();
        vBox.getChildren().clear();
        vBox.getChildren().add(requestApproverTableView);
         tableView = new DefaultTableView<>(requestApproverTableView);
         tableView.getTableView().getColumns().clear();
         tableView.getTableView().getItems().clear();
        tableView.getTableView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Approver>() {
            @Override
            public void changed(ObservableValue<? extends Approver> observable, Approver oldValue, Approver newValue) {
                if (newValue != null) {
                    selectedApprover = newValue;
                    selectedApproverListener();
                }
            }
        });
        requestApproverTableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Approver approver, boolean empty) {
                super.updateItem(approver, empty);
                if (approver != null || !empty) {
                    if (approver.getDisableView()) {
                        setDisable(true);
                    } else {
                        setDisable(false);
                    }

                }
            }
        });

//         tableView.addColumn("","name");
        tableView.getTableView().getColumns().add(newNameColumn(""));
        tableView.getTableView().getColumns().add(newPositionColumn(""));
//         tableView.addColumn("","role");
         tableView.getTableView().getColumns().add(newEditButtonColumn());
         tableView.getTableView().getColumns().add(newStatusColumn(""));
//         tableView.addColumn("","status");


         tableView.setStyleSheet("/ku/cs/styles/department/pages/request/department-request-approver-table-stylesheet.css");
    }
    private void refreshTableData(){
        tableView.getTableView().getItems().clear();
        approverList = approverDatasource.readData();

        if(request != null){
            UUID requestUUID = request.getUuid();
            filterApproverList = approverList.getApproverList(requestUUID);
            //CHECK ADVISOR STATE
            String advisorStatus = "";
            sumApprover = 0;
            sumAprroved = 0;
            sumApproverFaculty = 0;
            sumApprovedFaculty = 0;
            haveReject = false;
            goToFaculty = false;
            for(Approver a : filterApproverList.getApprovers()){
                if(a instanceof AdvisorApprover)advisorStatus = a.getStatus();
                if(a instanceof DepartmentApprover || a instanceof OtherApprover){
                    sumApprover++;
                    if(a.getStatus().equals("เรียบร้อย"))sumAprroved++;
                }
                if(a instanceof FacultyApprover){
                    sumApproverFaculty++;
                    if(a.getStatus().equals("เรียบร้อย"))sumApprovedFaculty++;
                    if(!a.getStatus().equals("รอส่งคณะ"))goToFaculty = true;
                }
                if(a.getStatus().equals("ไม่อนุมัติ"))haveReject = true;
            }
            System.out.println(haveReject);
            //ADD APPROVER TO TABLE
            for(Approver a : filterApproverList.getApprovers()){

                if(!(a instanceof AdvisorApprover)){
                    if(advisorStatus.equals("เรียบร้อย")){
                        a.setDisableView(false);//DEPARTMENT OT OTHER
                        if (a instanceof FacultyApprover ) {
                            if(sumAprroved != sumApprover){
                                a.setDisableView(true);
                            }
                        }
                    }else{
                        a.setDisableView(true);
                    }
                }
                requestApproverTableView.getItems().add(a);
            }
        }
        requestApproverTableView.getSortOrder().clear();
//        TableColumn nisitedCol = nisitTableView.getColumns().get(1);
//        nisitedCol.setSortable(true);
//        nisitTableView.getSortOrder().add(nisitedCol);
//        nisitedCol.setSortType(TableColumn.SortType.ASCENDING);
//        nisitTableView.sort();
//        nisitedCol.setSortable(false);

    }
    private void refreshAllData(){
        requestList = requestDatasource.readData();
        request = (Request) session.getData();
        request = requestList.findByRequestUUID(request.getUuid());
        requestOwner = studentList.findUserByUUID(request.getOwnerUUID());

        initRequestApproverTableView();
        refreshTableData();
        initRequestInfoVBox();
        initRequestMenuHBox();
        initStatusVBox();

        selectedApprover = null;
        selectedApproverListener();

        addApproverButton.setDisable(false);
        requestInfoButton.setDisable(false);
        approveButton.setDisable(true);
        rejectButton.setDisable(true);//NO NEED
        rejectButton.setVisible(false);//NO NEED

        if(haveReject){
            addApproverButton.setDisable(true);
        }


        if(sumApprover == 0){
            for(Approver a : filterApproverList.getApprovers()){
                if(a instanceof AdvisorApprover){
                    if(a.getStatus().equals("เรียบร้อย")){
//                        addApproverButton.setDisable(true);
                        if(!request.getStatusNext().equals("คำร้องดำเนินการครบถ้วน")){
                            approveButton.setDisable(false);
                        }else{
                            approveButton.setDisable(true);
                            addApproverButton.setDisable(true);
                        }

                    }
                }
            }

        }else{
            if(sumAprroved == sumApprover){
                if(!request.getStatusNext().equals("คำร้องดำเนินการครบถ้วน")){
                    approveButton.setDisable(false);
                }
            }
        }

        if(goToFaculty){
            approveButton.setDisable(true);
            addApproverButton.setDisable(true);
//            rejectButton.setDisable(true);
        }



    }

    private void selectedApproverListener(){
        if(selectedApprover != null){
            showEdit = true;
            editMode = true;
        }else {
            showEdit = false;
            editMode = false;
        }

        initEditorVBox();
        editorVBox.setDisable(!showEdit);
        toggleEditUploader();
    }

    private TableColumn<Approver,?> newEditButtonColumn(){
        TableColumn<Approver, HBox> column = new TableColumn<>("");
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE

        column.setCellFactory(c -> new TableCell<>() {
            private Button editButton = new Button();
            private Button signatureButton = new Button();
            private Button approveButton = new Button();

            private final HBox hBox = new HBox(editButton,signatureButton,approveButton);
            {
//                setStyle("-fx-background-color: red");
                setPrefSize(200,60);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(10);

                //EDIT BUTTON
                DefaultButton editBT = new DefaultButton(editButton,"transparent", "#e0e0e0", "#000000"){
                    @Override
                    protected void handleClickEvent() {
                        button.setOnMouseClicked(e -> {
                            Approver approver = getTableView().getItems().get(getIndex());
                            System.out.println("EDIT CLICK : " + approver.getName());
                        });
                    }
                };
                Image deleteButtonImage = new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/edit-approver-icon-blue.png"));
                editBT.changeBackgroundRadius(20);
                editBT.setImage(deleteButtonImage,40,40);
                if(haveReject)editBT.getButton().setDisable(true);

                //SIGNATURE BUTTON
                DefaultButton signatureBT = new DefaultButton(signatureButton,"transparent", "#e0e0e0", "#000000"){
                    @Override
                    protected void handleClickEvent() {
                        button.setOnMouseClicked(e -> {
                            Approver approver = getTableView().getItems().get(getIndex());
                            System.out.println("Signature CLICK : " + approver.getName());
                            onSignatureImageView(approver);
                        });
                    }
                };
                Image signatureButtonImage = new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/signature-image-purple-icon.png"));
                signatureBT.changeBackgroundRadius(20);
                signatureBT.setImage(signatureButtonImage,40,40);

                //APPROVE BUTTON
                DefaultButton approveBT = new DefaultButton(approveButton,"transparent", "#e0e0e0", "#000000"){
                    @Override
                    protected void handleClickEvent() {
                        button.setOnMouseClicked(e -> {
                            Approver approver = getTableView().getItems().get(getIndex());
                            System.out.println("Approve CLICK : " + approver.getName());
                            onApproveApprover(approver);
                        });
                    }
                };
                Image approveButtonImage = new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/approve-approver-green-icon.png"));
                approveBT.changeBackgroundRadius(20);
                approveBT.setImage(approveButtonImage,40,40);
                if(haveReject)approveBT.getButton().setDisable(true);

            }
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // No content for empty cells
                } else {
                    Approver approver = getTableView().getItems().get(getIndex());
                    if (approver instanceof AdvisorApprover || approver instanceof FacultyApprover) {
                        editButton.setDisable(true);
//                        signatureButton.setDisable(true);
                        approveButton.setDisable(true);
                    }
                    if(approver.getSignatureFile().equals("no-image")){
                        signatureButton.setDisable(true);
                        approveButton.setDisable(true);
                    }
                    if(approver.getStatus().equals("เรียบร้อย")){
                        editButton.setDisable(true);
                        approveButton.setDisable(true);
                    }

                    setGraphic(hBox); // Set the HBox with the button as the cell's graphic
                }
            }
        });
        return column;
    }

    private TableColumn<Approver,?> newNameColumn(String colName)   {
        TableColumn<Approver,VBox> column = new TableColumn<>(colName);
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE
        column.setCellFactory(c -> new TableCell<>(){
            private VBox vBox = new VBox();
            private DefaultLabel line1 = new DefaultLabel("");
            private DefaultLabel line2 = new DefaultLabel("");
            {
                vBox.setAlignment(Pos.CENTER);
                line1.changeText("",28, FontWeight.BOLD);
                line2.changeText("",20, FontWeight.NORMAL);
            }
            @Override
            protected void updateItem(VBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    Approver approver = getTableView().getItems().get(getIndex());
                    String name = approver.getName();

                    line1.changeText(name);
                    line2.changeText("ผู้อนุมัติ");
                    vBox.getChildren().clear();
                    vBox.getChildren().addAll(line1,line2);
                    setGraphic(vBox);
                }
            }
        });
        return column;
    }

    private TableColumn<Approver,?> newPositionColumn(String colName)   {
        TableColumn<Approver,VBox> column = new TableColumn<>(colName);
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE
        column.setCellFactory(c -> new TableCell<>(){
            private VBox vBox = new VBox();
            private DefaultLabel line1 = new DefaultLabel("");
            private DefaultLabel line2 = new DefaultLabel("");
            {
                vBox.setAlignment(Pos.CENTER);
                line1.changeText("",24, FontWeight.BOLD);
                line2.changeText("",22, FontWeight.NORMAL);
            }
            @Override
            protected void updateItem(VBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    Approver approver = getTableView().getItems().get(getIndex());
                    String role = approver.getRole();
                    line1.changeText(role);

                    vBox.getChildren().clear();
                    vBox.getChildren().addAll(line1,line2);
                    String extend = "";
                    DepartmentUser user = (DepartmentUser) requestOwner;
                    if(role.contains("คณบดี")){
                        String facultyUUID = user.getFacultyUUID().toString();
                        extend = "คณะ" + facultyList.getFacultyByUuid(facultyUUID).getName();
                    }else if(role.contains("หัวหน้าภาควิชา")){
                        String departmentUUID = user.getDepartmentUUID().toString();
                        extend = departmentList.getDepartmentByUuid(departmentUUID).getName();
                    }else{
                        vBox.getChildren().removeLast();
                    }
                    line2.changeText(extend);
                    setGraphic(vBox);
                }
            }
        });
        return column;
    }

    private TableColumn<Approver,?> newStatusColumn(String colName){
        TableColumn<Approver,HBox> column = new TableColumn<>(colName);
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE
        column.setCellFactory(c -> new TableCell<>(){
            private HBox hBox = new HBox();
            private DefaultLabel line1 = new DefaultLabel("");
            private SquareImage icon = new SquareImage(new ImageView());
            {
//                setStyle("-fx-background-color: red");
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setSpacing(20);
                line1.changeText("",28, FontWeight.BOLD);
                icon.getImageView().setFitWidth(50);
                icon.getImageView().setFitHeight(50);
                icon.setClipImage(10,10);
//                setPadding(new Insets(0,20,0,0));
            }
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    Approver approver = getTableView().getItems().get(getIndex());
                    String status = approver.getStatus();

                    if (status.length() > 10) {
                        line1.changeText("",24, FontWeight.BOLD);
                        line1.setMaxHeight(60);
                        line1.setWrapText(true);
                        line1.setTextAlignment(TextAlignment.CENTER);
                    }
                    if(status.equals("รออาจารย์ที่ปรึกษา")){
                        line1.setMaxWidth(80);
                    }
                    if(status.equals("รอคณะดำเนินการ")){
                        line1.setMaxWidth(75);
                    }
                    line1.changeText(status);
                    if(status.equals("เรียบร้อย")){
                        line1.changeLabelColor("green");
                    } else if (status.equals("ไม่อนุมัติ")) {
                        line1.changeLabelColor("red");
                    }else {
                        line1.changeLabelColor("black");
                    }


                    HBox statusBox = new HBox();
                    statusBox.setPrefWidth(120);
//                    statusBox.setStyle("-fx-background-color: red");
                    statusBox.setAlignment(Pos.CENTER);
                    statusBox.getChildren().add(line1);

                    hBox.getChildren().clear();
                    hBox.getChildren().addAll(statusBox,icon.getImageView());

                    setGraphic(hBox);
                }
            }
        });
        return column;
    }

    private void initStatusVBox(){
        statusVBox.getChildren().clear();
        statusVBox.setAlignment(Pos.CENTER);
        SquareImage statusImage = new SquareImage(new ImageView());
        statusImage.getImageView().setFitHeight(150);
        statusImage.getImageView().setFitWidth(150);
        statusImage.setClipImage(50,50);
        VBox.setMargin(statusImage.getImageView(),new Insets(0,0,10,0));

        DefaultLabel statusNow = new DefaultLabel("");
        statusNow.changeText(request.getStatusNow(),24, FontWeight.NORMAL);

        DefaultLabel statusNext = new DefaultLabel("");
        statusNext.changeText(request.getStatusNext(),24, FontWeight.NORMAL);

        statusVBox.getChildren().addAll(statusImage.getImageView(),statusNow,statusNext);
    }

    private void initEditorVBox(){
        editorVBox.getChildren().clear();
        if(showEdit){
            String tier = selectedApprover.getTier();
            String approverStatus = selectedApprover.getStatus();
            if(!approverStatus.equals("ไม่อนุมัติ")) {
                for (Approver a : filterApproverList.getApprovers()) {
                    if (a.getStatus().equals("ไม่อนุมัติ")) {
                        initEditDepartmentOtherException("มีผู้อนุมัติที่ปฏิเสธ");
                        return;
                    }
                }
            }

            switch (tier){
                case "advisor":
                    initEditAdvisor();
                    break;
                case "faculty":
                    initEditFaculty();
                    break;
                case "department":
                    initEditDepartmentOther();
                    break;
                case "other":
                    initEditDepartmentOther();
                    break;
            }

        }



    }

    private void initEditDepartmentOther(){
        String approverStatus = selectedApprover.getStatus();
        String statusImageFileName = "editor-approver-fallback.png";
        switch (approverStatus){
            case "รออัพโหลด":
                initEditDepartmentOtherWaitUpload();
                break;
            case "เรียบร้อย":
//                statusImageFileName = "approver-approve-green-check.png";
                initEditDepartmentOtherApprove();
                break;
            case "ไม่อนุมัติ":
//                statusImageFileName = "approver-reject-red-x.png";
                initEditDepartmentOtherReject();
                break;
        }
    }


    private void initEditDepartmentOtherException(String error){
        String approverStatus = selectedApprover.getStatus();
        String statusImageFileName = "approver-reject-red-x.png";
        editorVBox.setAlignment(Pos.CENTER);
        editorVBox.setSpacing(15);

        HBox container;
        //IMAGE
        container = newEditorContainer();
        SquareImage editorImage = new SquareImage(new ImageView());
        editorImage.getImageView().setFitHeight(150);
        editorImage.getImageView().setFitWidth(150);
        editorImage.setClipImage(50,50);
        editorImage.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/" + statusImageFileName)));
        container.getChildren().add(editorImage.getImageView());
        editorVBox.getChildren().add(container);

        container = newEditorContainer();
        DefaultLabel titleLabel = new DefaultLabel("");
        titleLabel.changeText("ขัดข้อง",48, FontWeight.BOLD);
        container.getChildren().add(titleLabel);
        editorVBox.getChildren().add(container);

        container = newEditorContainer();
        DefaultLabel statusLabel = new DefaultLabel("");
        statusLabel.changeText("สถานะ " + approverStatus,32, FontWeight.NORMAL);
        container.getChildren().add(statusLabel);
        editorVBox.getChildren().add(container);

        container = newEditorContainer();
        DefaultLabel errorLabel = new DefaultLabel("");
        errorLabel.changeText(error,28, FontWeight.NORMAL);
        errorLabel.setWrapText(true);
        container.getChildren().add(errorLabel);
        editorVBox.getChildren().add(container);

    }

    private void initEditDepartmentOtherReject(){
        String approverStatus = selectedApprover.getStatus();
        String statusImageFileName = "approver-reject-red-x.png";
        editorVBox.setAlignment(Pos.CENTER);
        editorVBox.setSpacing(15);

        HBox container;
        //IMAGE
        container = newEditorContainer();
        SquareImage editorImage = new SquareImage(new ImageView());
        editorImage.getImageView().setFitHeight(150);
        editorImage.getImageView().setFitWidth(150);
        editorImage.setClipImage(50,50);
        editorImage.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/" + statusImageFileName)));
        container.getChildren().add(editorImage.getImageView());
        editorVBox.getChildren().add(container);

        container = newEditorContainer();
        DefaultLabel statusLabel = new DefaultLabel("");
        statusLabel.changeText(approverStatus,48, FontWeight.BOLD);
        container.getChildren().add(statusLabel);
        editorVBox.getChildren().add(container);

        if(approverStatus.equals("ไม่อนุมัติ") && request.getReasonForNotApprove() != null){
            container = newEditorContainer();
            DefaultLabel rejectReasonLabel = new DefaultLabel("");
            rejectReasonLabel.changeText("เหตุผล " + request.getReasonForNotApprove(),24, FontWeight.NORMAL);
            rejectReasonLabel.setWrapText(true);
            container.getChildren().add(rejectReasonLabel);
            editorVBox.getChildren().add(container);
        }
    }

    private void initEditDepartmentOtherApprove(){
        String approverStatus = selectedApprover.getStatus();
        String statusImageFileName = "approver-approve-green-check.png";
        editorVBox.setAlignment(Pos.CENTER);
        editorVBox.setSpacing(15);

        HBox container;
        //IMAGE
        container = newEditorContainer();
        SquareImage editorImage = new SquareImage(new ImageView());
        editorImage.getImageView().setFitHeight(150);
        editorImage.getImageView().setFitWidth(150);
        editorImage.setClipImage(50,50);
        editorImage.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/" + statusImageFileName)));
        container.getChildren().add(editorImage.getImageView());
        editorVBox.getChildren().add(container);

        container = newEditorContainer();
        DefaultLabel statusLabel = new DefaultLabel("");
        statusLabel.changeText(approverStatus,48, FontWeight.BOLD);
        container.getChildren().add(statusLabel);
        editorVBox.getChildren().add(container);
    }
    private void initEditDepartmentOtherWaitUpload(){
        editorVBox.setAlignment(Pos.CENTER);
        editorVBox.setSpacing(15);

        String statusImageFileName = "editor-approver-fallback.png";
        HBox container;
        //IMAGE
        container = newEditorContainer();
        CropImage editorImage = new SquareImage(new ImageView());
//        if(!selectedApprover.getSignatureFile().equals("no-image")){
//            ImageDatasource imageDatasource = new ImageDatasource("signatures");
//            editorImage.setImage(imageDatasource.openImage(selectedApprover.getSignatureFile()));
//            editorImage.getImageView().setFitWidth(200);
//            editorImage.getImageView().setFitHeight(150);
//            editorImage.setClipImage(50,50);
//        }else{
//            editorImage.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/" + statusImageFileName)));
//            editorImage.getImageView().setFitWidth(150);
//            editorImage.getImageView().setFitHeight(150);
//            editorImage.setClipImage(50,50);
//        }
        editorImage.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/" + statusImageFileName)));
        editorImage.getImageView().setFitWidth(150);
        editorImage.getImageView().setFitHeight(150);
        editorImage.setClipImage(50,50);


        container.getChildren().add(editorImage.getImageView());
        editorVBox.getChildren().add(container);

        //Name LABEL
        VBox nameVBox = new VBox();
        nameVBox.setAlignment(Pos.CENTER);
        container = newEditorContainer();
        DefaultLabel nameLabel = new DefaultLabel("");
        nameLabel.changeText(selectedApprover.getName(),28, FontWeight.BOLD);
        container.getChildren().add(nameLabel);
        nameVBox.getChildren().add(nameLabel);

        //Role
        String role = selectedApprover.getRole();
        String extend = "";
        DepartmentUser user = (DepartmentUser) requestOwner;
        if(role.contains("คณบดี")){
            String facultyUUID = user.getFacultyUUID().toString();
            extend = " คณะ" + facultyList.getFacultyByUuid(facultyUUID).getName();
        }else if(role.contains("หัวหน้าภาควิชา")){
            String departmentUUID = user.getDepartmentUUID().toString();
            extend = " " + departmentList.getDepartmentByUuid(departmentUUID).getName();
        }
        container = newEditorContainer();
        DefaultLabel roleLabel = new DefaultLabel("");
        roleLabel.changeText(role + extend,24, FontWeight.NORMAL);
        container.getChildren().add(roleLabel);
        nameVBox.getChildren().add(container);
        editorVBox.getChildren().add(nameVBox);

        //Sinature Uploader
        container = newEditorContainer();
        sinatureUploader = new UploadImageStack("signatures",
                selectedApprover.getSignatureFilename(),
                selectedApprover.getSignatureFile()
        );
        container.getChildren().add(sinatureUploader);
        editorVBox.getChildren().add(container);

        //EDIT SAVE CANCEL BUTTON
        container = newEditorContainer();
        container.setSpacing(20);
        Button button;
        button = new Button();
        button.setId("leftButton");
        container.getChildren().add(button);

        button = new Button();
        button.setId("rightButton");
        container.getChildren().add(button);

        editorVBox.getChildren().add(container);
    }

    private void toggleEditUploader(){
        editMode = !editMode;

        ObservableList children = editorVBox.getChildren();
        children.forEach(child -> {
            if(child instanceof HBox){
                HBox hBox = (HBox) child;
                if(hBox.getChildren().getFirst() instanceof UploadImageStack){
                    hBox.setVisible(editMode);
                }
                if(hBox.getChildren().getFirst() instanceof Button){
                    hBox.getChildren().forEach(subChild -> {
                        if(subChild instanceof Button){
                            Button b = (Button) subChild;
                            DefaultButton button = null;
                            if(editMode){
                                if(b.getId() != null && b.getId().equals("leftButton")){
                                    button = new DefaultButton(b,"#ABFFA4","#80BF7A","black"){
                                        @Override
                                        protected void handleClickEvent(){
                                            button.setOnMouseClicked(e -> {
                                                onSaveSignatureUploader();
                                            });
                                        }
                                    };
                                    button.changeText("บันทึก",24, FontWeight.NORMAL);

                                }

                                if(b.getId() != null && b.getId().equals("rightButton")){
                                    button = new DefaultButton(b,"#FFA4A4","#E19494","black"){
                                        @Override
                                        protected void handleClickEvent(){
                                            button.setOnMouseClicked(e -> {
                                                sinatureUploader.cancelUploadedImage();//IF CLICKED
                                                sinatureUploader.cancelDeleteImage();//IF CLICKED
                                                selectedApproverListener();
                                            });
                                        }
                                    };
                                    button.changeText("ยกเลิก",24, FontWeight.NORMAL);
                                }
                            }else{
                                if(b.getId() != null && b.getId().equals("leftButton")){
                                    button = new DefaultButton(b,"#ABFFA4","#80BF7A","black"){
                                        @Override
                                        protected void handleClickEvent(){
                                            button.setOnMouseClicked(e -> {
                                                toggleEditUploader();
                                            });
                                        }
                                    };
                                    button.changeText("แก้ไข",24, FontWeight.NORMAL);

                                }
                                if(b.getId() != null && b.getId().equals("rightButton")){
                                    button = new DefaultButton(b,"#FFA4A4","#E19494","black"){
                                        @Override
                                        protected void handleClickEvent(){
                                            button.setOnMouseClicked(e -> {
                                                mainStackPane.getChildren().add(new BlankPopupStack(){
                                                    private VBox mainBox;
                                                    private TextFieldStack reasonTextField;
                                                    private DefaultLabel errorLabel;
                                                    @Override
                                                    protected void initPopupView(){
                                                        stackPane.setAlignment(Pos.CENTER);
                                                        mainBox = new VBox();
                                                        mainBox.setSpacing(20);
                                                        mainBox.setMaxWidth(500);
                                                        mainBox.setMaxHeight(430);
                                                        mainBox.setStyle("-fx-background-color: white;-fx-background-radius: 30;");
                                                        HBox container;
                                                        DefaultLabel prefix;
                                                        DefaultLabel data;

                                                        //TITLE
                                                        container = new HBox();
                                                        DefaultLabel titleLabel = new DefaultLabel("");
                                                        titleLabel.changeText("ปฏิเสธผู้อนุมัติคำร้อง",32, FontWeight.BOLD);
                                                        container.getChildren().add(titleLabel);
                                                        container.setAlignment(Pos.CENTER);
                                                        mainBox.getChildren().add(container);
                                                        VBox.setMargin(container,new Insets(30,0,0,0));

                                                        //NAME APPROVER
                                                        container = new HBox();
                                                        prefix = new DefaultLabel("");
                                                        data = new DefaultLabel("");
                                                        prefix.changeText("     ชื่อ",28, FontWeight.BOLD);
                                                        data.changeText(selectedApprover.getName(),28, FontWeight.NORMAL);
                                                        container.getChildren().addAll(prefix,data);
                                                        container.setAlignment(Pos.CENTER_LEFT);
                                                        container.setSpacing(20);
                                                        mainBox.getChildren().add(container);


                                                        //POSITION APPROVER
                                                        container = new HBox();
                                                        prefix = new DefaultLabel("");
                                                        prefix.changeText("     ตำแหน่ง",28, FontWeight.BOLD);
                                                        data = new DefaultLabel("");

                                                        String role = selectedApprover.getRole();
                                                        String extend = "";
                                                        DepartmentUser user = (DepartmentUser) requestOwner;
                                                        if(role.contains("คณบดี")){
                                                            String facultyUUID = user.getFacultyUUID().toString();
                                                            extend = "คณะ" + facultyList.getFacultyByUuid(facultyUUID).getName();
                                                        }else if(role.contains("หัวหน้าภาควิชา")){
                                                            String departmentUUID = user.getDepartmentUUID().toString();
                                                            extend = departmentList.getDepartmentByUuid(departmentUUID).getName();
                                                        }

                                                        data.changeText(extend + role,28, FontWeight.NORMAL);
                                                        container.getChildren().addAll(prefix,data);
                                                        container.setAlignment(Pos.CENTER_LEFT);
                                                        container.setSpacing(20);
                                                        mainBox.getChildren().add(container);


                                                        container = new HBox();
                                                        reasonTextField =  new TextFieldStack("เหตุผลที่ปฏิเสธ",450,100);
                                                        reasonTextField.setStyle("-fx-border-color: black;-fx-border-radius: 20;");
                                                        reasonTextField.toggleTextField(true);

                                                        container.getChildren().addAll(reasonTextField);
                                                        container.setAlignment(Pos.CENTER);
                                                        mainBox.getChildren().add(container);

                                                        //ERROR LABEL
                                                        container = new HBox();
                                                        container.setPrefHeight(30);
                                                        errorLabel = new DefaultLabel("");
                                                        container.setAlignment(Pos.CENTER);
                                                        container.getChildren().add(errorLabel);
                                                        mainBox.getChildren().add(container);

                                                        HBox lineEnd = new HBox(acceptButton,declineButton);
                                                        lineEnd.setAlignment(Pos.CENTER);
                                                        lineEnd.setSpacing(20);
                                                        mainBox.getChildren().addAll(lineEnd);
                                                        stackPane.getChildren().add(mainBox);
                                                    }@Override
                                                    protected void handleAcceptButton(){
                                                        acceptButton.setOnMouseClicked(e -> {
                                                            reasonTextField.toggleTextField(false);
                                                            if(!reasonTextField.getData().isEmpty()){
                                                                onRejectApprover(reasonTextField.getData());
                                                                mainStackPane.getChildren().removeLast();
                                                            }else{
                                                                errorLabel.changeText("reason must not be empty",24,FontWeight.NORMAL);
                                                                errorLabel.changeLabelColor("red");
                                                                reasonTextField.toggleTextField(true);
                                                            }
                                                        });
                                                    }
                                                    @Override
                                                    protected void handleDeclineButton(){
                                                        declineButton.setOnMouseClicked(e ->{
                                                            mainStackPane.getChildren().removeLast();
                                                        });
                                                    }
                                                });

                                            });
                                        }
                                    };
                                    button.changeText("ปฏิเสธ",24, FontWeight.NORMAL);
                                }
                            }
                            if(button != null){
                                button.setButtonSize(120,60);
                                button.changeBackgroundRadius(15);
                            }

                        }
                    });
                }

            }
        });
    }

    private void initEditAdvisor(){
        String approverStatus = selectedApprover.getStatus();
        String statusImageFileName = "editor-approver-fallback.png";
        switch (approverStatus){
            case "รออาจารย์ที่ปรึกษา":
                statusImageFileName = "approver-waiting-yellow-pen.png";
                break;
            case "เรียบร้อย":
                statusImageFileName = "approver-approve-green-check.png";
                break;
            case "ไม่อนุมัติ":
                statusImageFileName = "approver-reject-red-x.png";
                break;
        }
        editorVBox.setAlignment(Pos.CENTER);
        editorVBox.setSpacing(15);

        HBox container;
        //IMAGE
        container = newEditorContainer();
        SquareImage editorImage = new SquareImage(new ImageView());
        editorImage.getImageView().setFitHeight(150);
        editorImage.getImageView().setFitWidth(150);
        editorImage.setClipImage(50,50);
        editorImage.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/" + statusImageFileName)));
        container.getChildren().add(editorImage.getImageView());
        editorVBox.getChildren().add(container);

        container = newEditorContainer();
        DefaultLabel statusLabel = new DefaultLabel("");
        statusLabel.changeText(approverStatus,48, FontWeight.BOLD);
        container.getChildren().add(statusLabel);
        editorVBox.getChildren().add(container);

        if(approverStatus.equals("ไม่อนุมัติ") && request.getReasonForNotApprove() != null){
            container = newEditorContainer();
            DefaultLabel rejectReasonLabel = new DefaultLabel("");
            rejectReasonLabel.changeText("เหตุผล " + request.getReasonForNotApprove(),24, FontWeight.NORMAL);
            rejectReasonLabel.setWrapText(true);
            container.getChildren().add(rejectReasonLabel);
            editorVBox.getChildren().add(container);
        }

    }


    private void initEditFaculty(){
        String approverStatus = selectedApprover.getStatus();
        String statusImageFileName = "editor-approver-fallback.png";
        switch (approverStatus){
            case "รอส่งคณะ":
                initEditFacultyWaitForSend();
                break;
            case "รอคณะดำเนินการ":
                initEditFacultyGlobal();
                break;
            case "เรียบร้อย":
                initEditFacultyGlobal();
                break;
            case "ไม่อนุมัติ":
                initEditFacultyGlobal();
                break;
        }
    }
    private void initEditFacultyWaitForSend(){
        HBox container = newEditorContainer();
        DefaultButton sendToFacultButton = new DefaultButton("red","orange","white"){
            @Override
            protected void handleClickEvent(){
                button.setOnMouseClicked(e->{
                    onSendToFacultyButton();
                });
            }
        };
        sendToFacultButton.setButtonSize(250,250);
        sendToFacultButton.changeBackgroundRadius(50);
        sendToFacultButton.changeText("ส่งหาคณะ",48,FontWeight.BOLD);
        container.getChildren().add(sendToFacultButton);
        container.setAlignment(Pos.CENTER);

        editorVBox.getChildren().add(container);
        editorVBox.setAlignment(Pos.CENTER);
    }
    private void initEditFacultyGlobal(){
        String approverStatus = selectedApprover.getStatus();
        String statusImageFileName = "editor-approver-fallback.png";
        switch (approverStatus){
            case "รอคณะดำเนินการ":
                statusImageFileName = "approver-waiting-yellow-pen.png";
                break;
            case "เรียบร้อย":
                statusImageFileName = "approver-approve-green-check.png";
                break;
            case "ไม่อนุมัติ":
                statusImageFileName = "approver-reject-red-x.png";
                break;
        }
        editorVBox.setAlignment(Pos.CENTER);
        editorVBox.setSpacing(15);

        HBox container;
        //IMAGE
        container = newEditorContainer();
        SquareImage editorImage = new SquareImage(new ImageView());
        editorImage.getImageView().setFitHeight(150);
        editorImage.getImageView().setFitWidth(150);
        editorImage.setClipImage(50,50);
        editorImage.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/" + statusImageFileName)));
        container.getChildren().add(editorImage.getImageView());
        editorVBox.getChildren().add(container);

        container = newEditorContainer();
        DefaultLabel statusLabel = new DefaultLabel("");
        statusLabel.changeText(approverStatus,48, FontWeight.BOLD);
        container.getChildren().add(statusLabel);
        editorVBox.getChildren().add(container);

        if(approverStatus.equals("ไม่อนุมัติ") && request.getReasonForNotApprove() != null){
            container = newEditorContainer();
            DefaultLabel rejectReasonLabel = new DefaultLabel("");
            rejectReasonLabel.changeText("เหตุผล " + request.getReasonForNotApprove(),24, FontWeight.NORMAL);
            rejectReasonLabel.setWrapText(true);
            container.getChildren().add(rejectReasonLabel);
            editorVBox.getChildren().add(container);
        }

    }
    private HBox newEditorContainer(){
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private void onSaveSignatureUploader(){
        try {
            sinatureUploader.saveUploadedImage();//IF CLICKED
            sinatureUploader.performDeleteImage();//IF CLICKED

            selectedApprover.setSignatureFile(sinatureUploader.getCurFileName());
            approverDatasource.writeData(approverList);

            requestApproverTableView.refresh();
            selectedApproverListener();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    private void onRejectApprover(String reasonForNotApprove){
        try {
            selectedApprover.setStatus("ไม่อนุมัติ");
            request.setReasonForNotApprove(reasonForNotApprove);
            request.setStatusNow("ปฏิเสธโดยหัวหน้าภาควิชา");
            request.setStatusNext("คำร้องถูกปฏิเสธ");
            request.setTimeStamp(LocalDateTime.now());

            approverDatasource.writeData(approverList);
            requestDatasource.writeData(requestList);

//            session.setData(request);
//            FXRouter.goTo("department-staff-request",session);
            refreshAllData();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onApproveApprover(Approver approver){
        mainStackPane.getChildren().add(new ConfirmStack("ยันยันการอนุมัติ","คุณสามารถอนุมัติได้เพียงครั้งเดียวและไม่สามารถแก้ไขได้"){
            @Override
            protected void initialize(){
                super.initialize();
                getAcceptButton().changeText("ตกลง");
                getDeclineButton().changeText("ยกเลิก");
            }
            @Override
            protected void handleAcceptButton(){
                getAcceptButton().setOnMouseClicked(e -> {
                    System.out.println("Accept button clicked");
                    onApproveApproverConfirm(approver);
                    mainStackPane.getChildren().removeLast();
                });
            }
            @Override
            protected void handleDeclineButton(){
                getDeclineButton().setOnMouseClicked(e -> {
                    System.out.println("Decline button clicked");
                    mainStackPane.getChildren().removeLast();
                });
            }
        });

    }
    private void onApproveApproverConfirm(Approver approver){
        try{
            approver.setStatus("เรียบร้อย");
            request.setTimeStamp(LocalDateTime.now());

            sumApprover = 0;
            sumAprroved = 0;
            sumApproverFaculty = 0;
            sumApprovedFaculty = 0;
            for(Approver a : filterApproverList.getApprovers()){
                if(a instanceof DepartmentApprover || a instanceof  OtherApprover){
                    sumApprover++;
                    if(a.getStatus().equals("เรียบร้อย")){
                        sumAprroved++;
                    }
                }
                if(a instanceof  FacultyApprover){
                    sumApproverFaculty++;
                    if(a.getStatus().equals("เรียบร้อย"))sumApprovedFaculty++;
                }
            }

            if(sumAprroved == sumApprover){
                if(sumApproverFaculty == 0){
                    request.setStatusNow("อนุมัติโดยหัวหน้าภาควิชา");
                    request.setStatusNext("คำร้องดำเนินการครบถ้วน");
                }else{
                    request.setStatusNow("อนุมัติโดยหัวหน้าภาควิชา");
                    request.setStatusNext("คำร้องส่งต่อให้คณบดี");
                }

            }

            approverDatasource.writeData(approverList);
            requestDatasource.writeData(requestList);

            refreshAllData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onSignatureImageView(Approver approver){
        try {
            mainStackPane.getChildren().add(new BlankPopupStack(){
                private VBox mainBox;
                @Override
                protected void initPopupView(){
                    mainBox = new VBox();
                    mainBox.setMaxWidth(960);
                    mainBox.setMaxHeight(640);
                    mainBox.setSpacing(10);
                    mainBox.setStyle("-fx-background-color: white;-fx-background-radius: 50");
                    //TITLE
                    HBox container = new HBox();
                    container.setAlignment(Pos.CENTER);
                    DefaultLabel titleLabel = new DefaultLabel("");
                    titleLabel.changeText("ลายเซ็น",32,FontWeight.BOLD);
                    container.getChildren().add(titleLabel);
                    mainBox.getChildren().add(container);
                    VBox.setMargin(container,new Insets(10,0,0,0));



                    //IMAGE
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(854);
                    imageView.setFitHeight(480);
                    ImageDatasource imageDatasource = new ImageDatasource("signatures");
                    Image signatureImage = imageDatasource.openImage(approver.getSignatureFile());
                    new CropImage(imageView,signatureImage).setClipImage(100,100);


                    //LINE END BUTTON
                    HBox content = new HBox(imageView);
                    content.setAlignment(Pos.CENTER);
                    HBox lineEnd = new HBox(declineButton);
                    lineEnd.setPrefHeight(100);
                    lineEnd.setAlignment(Pos.CENTER);
                    mainBox.getChildren().addAll(content,lineEnd);
                    stackPane.getChildren().add(mainBox);

                }

                @Override
                protected void handleDeclineButton() {
                    declineButton.setOnMouseClicked(e->{
                        mainStackPane.getChildren().removeLast();
                    });
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onSendToFacultyButton(){
        try {
            mainStackPane.getChildren().add(new ConfirmStack("ยันยัน","คุณต้องการส่งหาคณะใช่มั้ย ถ้าส่งแล้วคำร้องจะถูกส่งไปให้คณะจัดการต่อ"){
                @Override
                protected void initialize(){
                    super.initialize();
                    getAcceptButton().changeText("ตกลง");
                    getDeclineButton().changeText("ยกเลิก");
                }
                @Override
                protected void handleAcceptButton(){
                    getAcceptButton().setOnMouseClicked(e -> {
                        System.out.println("Accept button clicked");
                        onSendToFacultyConfirm();
                        mainStackPane.getChildren().removeLast();
                    });
                }
                @Override
                protected void handleDeclineButton(){
                    getDeclineButton().setOnMouseClicked(e -> {
                        System.out.println("Decline button clicked");
                        mainStackPane.getChildren().removeLast();
                    });
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void onSendToFacultyConfirm(){
        try {
            for(Approver a : filterApproverList.getApprovers()){
                if(a instanceof FacultyApprover && a.getStatus().equals("รอส่งคณะ")){
                    a.setStatus("รอคณะดำเนินการ");
                }
            }
            request.setTimeStamp(LocalDateTime.now());
            DepartmentUser user = (DepartmentUser) requestOwner;
            request.setFacultyUUID(user.getFacultyUUID());

            approverDatasource.writeData(approverList);
            requestDatasource.writeData(requestList);

            refreshAllData();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onRequestApproveButton(){
        try {
            String message = "";
            if(sumApprover == 0){
                message = "คำร้องของคุณจะถูกอนุมัติและสิ้นสุดการดำเนินการ";
            }else{
                message = "คำร้องของคุณจะถูกอนุมัติและสิ้นสุดการดำเนินการ โดยข้ามการอนุมัติในระดับคณะ";
            }
            mainStackPane.getChildren().add(new ConfirmStack("ยันยัน",message + " คุณต้องการอนุมัติคำร้องนี้ใช่มั้ย"){
                @Override
                protected void initialize(){
                    super.initialize();
                    getAcceptButton().changeText("ตกลง");
                    getDeclineButton().changeText("ยกเลิก");
                }
                @Override
                protected void handleAcceptButton(){
                    getAcceptButton().setOnMouseClicked(e -> {
                        System.out.println("Accept button clicked");
                        onRequestApproveConfirm();
                        mainStackPane.getChildren().removeLast();
                    });
                }
                @Override
                protected void handleDeclineButton(){
                    getDeclineButton().setOnMouseClicked(e -> {
                        System.out.println("Decline button clicked");
                        mainStackPane.getChildren().removeLast();
                    });
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onRequestApproveConfirm(){
        try {
            if(sumApprover != 0){
                for(Approver a : filterApproverList.getApprovers()){
                    if(a instanceof FacultyApprover){
                        a.setStatus("เรียบร้อย");
                    }
                }
            }

            request.setTimeStamp(LocalDateTime.now());
            request.setStatusNow("อนุมัติโดยหัวหน้าภาควิชา");
            request.setStatusNext("คำร้องดำเนินการครบถ้วน");

            approverDatasource.writeData(approverList);
            requestDatasource.writeData(requestList);

            refreshAllData();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
