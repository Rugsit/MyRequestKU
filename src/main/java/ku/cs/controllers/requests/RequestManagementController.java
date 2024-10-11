package ku.cs.controllers.requests;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.requests.approver.AddApproverController;
import ku.cs.controllers.requests.approver.EditApproverController;
import ku.cs.controllers.requests.information.MainInformationController;
import ku.cs.controllers.student.StudentRequestInfoController;
import ku.cs.models.Session;
import ku.cs.models.department.DepartmentList;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.request.*;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.approver.*;
import ku.cs.models.user.*;
import ku.cs.services.*;
import ku.cs.services.Observer;
import ku.cs.services.Theme;
import ku.cs.services.utils.DateTools;
import ku.cs.views.components.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class RequestManagementController implements Observer<HashMap<String, String>> {
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
    private Student requestOwner;
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
    private UploadPDFStack signatureUploader;

    private Session session;
    private Theme theme = Theme.getInstance();
    private String textThemeColorHex = "black";

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

        theme.addObserver(this);
        theme.notifyObservers(theme.getTheme());

    }
    private void initLabel(){
        if (session.getUser() instanceof DepartmentUser) {
            pageTitleLabel.setText("จัดการผู้อนุมัติ (ภาควิชา)");
        } else if (session.getUser() instanceof FacultyUser) {
            pageTitleLabel.setText("จัดการผู้อนุมัติ (คณะ)");
        }
        new DefaultLabel(pageTitleLabel);
    }
    private void initButton(){
        if (session.getUser() instanceof DepartmentUser) {
            new RouteButton(backButton,"department-staff-request-list","transparent","#a6a6a6","#000000");
        } else if (session.getUser() instanceof FacultyUser) {
            new RouteButton(backButton,"faculty-page","transparent","#a6a6a6","#000000");

        }
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

        image.getImageView().setFitHeight(150);
        image.getImageView().setFitWidth(150);
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
        prefix.changeText("อัปเดตล่าสุด",28 ,FontWeight.BOLD);
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

        addApproverButton = new DefaultButton("#7FE8FF","#a6a6a6","#000000") {
            @Override
            protected void handleClickEvent() {
                button.setOnMouseClicked(e -> {
                    mainStackPane.getChildren().add(new BlankPopupStack() {
                        HBox mainBox;
                        VBox vBox;
                        @Override
                        protected void initPopupView() {
                            firstButton = new DefaultButton("#ded9d9","#aba4a4","white") {
                                @Override
                                protected void handleClickEvent() {
                                    button.setOnMouseClicked(ev->{
                                        mainStackPane.getChildren().removeLast();
                                        try {
                                            if (currentPopupStage == null || !currentPopupStage.isShowing()) {
                                                currentPopupStage = new Stage();
                                                FXMLLoader fxmlLoader;
                                                Scene scene = null;
                                                fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/add-approver-pane.fxml"));
                                                scene = new Scene(fxmlLoader.load());
                                                AddApproverController controller = fxmlLoader.getController();
                                                controller.setCurrentRequest(request);
                                                controller.setApproverType("request");
                                                controller.setLoginUser(session.getUser());
                                                controller.setStage(currentPopupStage);

                                                currentPopupStage.setScene(scene);
                                                currentPopupStage.initModality(Modality.APPLICATION_MODAL);
                                                currentPopupStage.setTitle("Add approver");

                                                currentPopupStage.setOnHidden(event -> {
                                                    refreshAllData();
                                                });

                                                currentPopupStage.show();
                                            }
                                        } catch (IOException e) {
                                            System.out.println("Error: " + e.getMessage());
                                        }
                                    });

                                }
                            };
                            firstButton.changeBackgroundRadius(25);
                            firstButton.changeText("กรอกผู้อนุมัติคนใหม่", 28, FontWeight.NORMAL);
                            firstButton.setButtonSize(260,60);
                            firstButton.changeLabelColor("#000000");
                            firstButton.setImage(new Image(getClass().getResourceAsStream("/images/icons/pencil-bold.png")), 25, 25);
                            secondButton = new DefaultButton("#ded9d9","#aba4a4","white") {
                                @Override
                                protected void handleClickEvent() {
                                    button.setOnMouseClicked(e -> {
                                        mainStackPane.getChildren().removeLast();
                                        mainStackPane.getChildren().add(new BlankPopupStack() {
                                            VBox approverListBox;
                                            DefaultTableView<Approver> approverTableView;
                                            @Override
                                            protected void initPopupView() {
                                                ApproverListFileDatasource approverListFileDatasource = new ApproverListFileDatasource("approver");
                                                Set<Approver> filteredApprovers = null;
                                                if (session.getUser() instanceof DepartmentUser) {
                                                    ApproverList approverList = approverListFileDatasource.query("department", null);
                                                    filteredApprovers = approverList.getApprovers()
                                                            .stream()
                                                            .filter(approver -> approver.getAssociateUUID().equals(((DepartmentUser)session.getUser()).getDepartmentUUID()))
                                                            .collect(Collectors.toSet());
                                                } else if (session.getUser() instanceof FacultyUser) {
                                                    ApproverList approverList = approverListFileDatasource.query("faculty", null);
                                                    filteredApprovers = approverList.getApprovers()
                                                            .stream()
                                                            .filter(approver -> approver.getAssociateUUID().equals(((FacultyUser)session.getUser()).getFacultyUUID()))
                                                            .collect(Collectors.toSet());
                                                }
                                                approverTableView = new DefaultTableView<>(new TableView<Approver>()) {
                                                    @Override
                                                    protected void handleClick(){
                                                        getTableView().setOnMouseClicked(e->{
                                                            Approver selectedItem = getTableView().getSelectionModel().getSelectedItem();
                                                                if (selectedItem != null) {
                                                                approverListFileDatasource.appendDataFromList(selectedItem, request);
                                                            }
                                                            refreshAllData();
                                                            mainStackPane.getChildren().removeLast();
                                                        });
                                                    }
                                                };
                                                approverTableView.getColumns().clear();
                                                approverTableView.addColumn("ชื่อ", "Firstname");
                                                approverTableView.addColumn("นามสกุล", "Lastname");
                                                approverTableView.addColumn("ตำแหน่ง", "Role");
                                                approverTableView.getTableView().getItems().clear();
                                                approverTableView.getTableView().getItems().addAll(filteredApprovers);
                                                approverTableView.setEditable(false);
                                                Label placeholder = new Label("ไม่มีรายชื่อผู้อนุมัติในระบบ");
                                                placeholder.setStyle("-fx-font-size: 20");
                                                approverTableView.getTableView().setPlaceholder(placeholder);
                                                approverTableView.setStyleSheet("/ku/cs/styles/department/pages/request-list/department-staff-request-list-table-stylesheet.css");
                                                secondButton.setButtonSize(120, 30);
                                                approverListBox = new VBox(approverTableView.getTableView(), secondButton);
                                                approverListBox.setStyle("-fx-background-color: white; -fx-background-radius: 50px");
                                                approverListBox.setMaxWidth(850);
                                                approverListBox.setSpacing(80);
                                                approverListBox.setMaxHeight(620);
                                                approverListBox.setAlignment(Pos.CENTER);
                                                stackPane.getChildren().add(approverListBox);
                                            }
                                            @Override
                                            protected void handleSecondButton(){
                                                secondButton.setOnMouseClicked(e->{
                                                    mainStackPane.getChildren().removeLast();
                                                });
                                            }

                                        });
                                    });
                                }
                            };
                            secondButton.setButtonSize(260,60);
                            secondButton.setImage(new Image(getClass().getResourceAsStream("/images/icons/many-people-icon.png")), 30, 30);
                            secondButton.changeLabelColor("#000000");
                            secondButton.changeBackgroundRadius(25);
                            secondButton.changeText("เพิ่มผู้อนุมัติจากรายชื่อ", 28, FontWeight.NORMAL);
                            DefaultButton exitButton = new DefaultButton("#FF8080","#BC5F5F","white") {
                                @Override
                                protected void handleClickEvent() {
                                    button.setOnMouseClicked(e -> {
                                        mainStackPane.getChildren().removeLast();
                                    });
                                }
                            };
                            exitButton.setButtonSize(120,30);
                            exitButton.changeBackgroundRadius(25);
                            exitButton.changeText("ยกเลิก", 28, FontWeight.NORMAL);
                            exitButton.changeLabelColor("#000000");
                            DefaultLabel title = new DefaultLabel("");
                            title.changeText("เพิ่มผู้อนุมัติ", 36, FontWeight.BOLD);
                            mainBox = new HBox(firstButton, secondButton);
                            mainBox.setAlignment(Pos.CENTER);
                            mainBox.setSpacing(20);
                            mainBox.setMaxWidth(550);
                            mainBox.setMaxHeight(150);
                            vBox = new VBox(title, mainBox, exitButton);
                            vBox.setAlignment(Pos.CENTER);
                            vBox.setSpacing(50);
                            vBox.setMaxWidth(600);
                            vBox.setMaxHeight(280);
                            vBox.setStyle("-fx-background-color: white; -fx-background-radius: 50px");
                            mainBox.setStyle("-fx-background-color: white; -fx-background-radius: 50px");
                            stackPane.getChildren().add(vBox);
                        }

                    });
                });
            }
        };
        addApproverButton.changeText("เพิ่มผู้อนุมัติ",28, FontWeight.NORMAL);

        requestInfoButton = new DefaultButton("#FFA1D9","#a6a6a6","#000000"){
          @Override
          protected void handleClickEvent(){
              button.setOnMouseClicked(e ->{
                  try {
                      if (request == null || requestOwner == null) throw new NullPointerException("Request or Request owner is null");
                      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/main-information.fxml"));
                      AnchorPane scene = fxmlLoader.load();
                      MainInformationController controller = fxmlLoader.getController();
                      controller.setRequest(request);
                      controller.setLoginUser(requestOwner);
                      controller.setBackPageVisible(false);
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

                      mainStackPane.getChildren().add(new BlankPopupStack() {
                          VBox mainBox;
                          HBox lineEnd;
                          @Override
                          protected void initPopupView() {
                              secondButton.setText("ปิด");
                              firstButton.setText("เพิ่มเติม");
                              lineEnd = new HBox(secondButton, firstButton);
                              HBox.setMargin(secondButton, new Insets(0, 10, 0, 0));
                              mainBox = new VBox(scene, lineEnd);
                              mainBox.setMaxWidth(600);
                              mainBox.setMaxHeight(620);
                              mainBox.setStyle("-fx-background-color: white; -fx-background-radius: 50px");
                              if (Theme.getInstance().getCurrentTheme().equals("dark")) {
                                  mainBox.setStyle("-fx-background-color: #536878; -fx-background-radius: 50px");
                              }
                              lineEnd.setAlignment(Pos.CENTER);
                              scene.setStyle("-fx-pref-height: 620px");
                              stackPane.getChildren().add(mainBox);
                              VBox.setMargin(lineEnd,new Insets(20,0,20,0));
                          }

                          @Override
                          protected void handleFirstButton() {
                              firstButton.setOnMouseClicked(e->{
                                  try {
                                      String viewPath = "/ku/cs/views/student-request-info-pane.fxml";
                                      FXMLLoader fxmlLoader = new FXMLLoader();
                                      fxmlLoader.setLocation(getClass().getResource(viewPath));
                                      AnchorPane pane = fxmlLoader.load();
                                      StudentRequestInfoController controller = fxmlLoader.getController();
                                      controller.setLoginUser((Student) requestOwner);
                                      controller.setRequest(request);
                                      controller.showInfo();
                                      controller.showTable();
                                      controller.setSeeInformationVisible(false);
                                      controller.setBackButtonVisible(false);
                                      lineEnd.getChildren().removeLast();

                                      DefaultButton backButton = new DefaultButton("#45a1ed", "#3273a8", "#ffffff");
                                      backButton.setButtonSize(215,60);
                                      backButton.changeBackgroundRadius(25);
                                      backButton.changeText("ย้อนกลับ",28,FontWeight.NORMAL);
                                      backButton.getButton().setOnMouseClicked(event -> {
                                          mainBox.getChildren().clear();
                                          mainBox.getChildren().add(scene);
                                          lineEnd.getChildren().removeLast();
                                          lineEnd.getChildren().add(firstButton);
                                          mainBox.getChildren().add(lineEnd);
                                      });
                                      lineEnd.getChildren().add(backButton.getButton());
                                      mainBox.getChildren().clear();
                                      mainBox.getChildren().addAll(pane, lineEnd);
                                      mainBox.setMaxHeight(620);
                                      mainBox.setStyle("-fx-background-color: white; -fx-background-radius: 50px");
                                      if (Theme.getInstance().getCurrentTheme().equals("dark")) {
                                          mainBox.setStyle("-fx-background-color: #536878; -fx-background-radius: 50px");
                                      }
                                      pane.setStyle("-fx-pref-height: 620px");
                                      VBox.setMargin(lineEnd,new Insets(20,0,20,0));
                                  } catch (IOException exception) {
                                      System.err.println("Error: handle click");
                                  }
                              });
                          }

                          @Override
                          protected void handleSecondButton() {
                              secondButton.setOnMouseClicked(e->{
                                  mainStackPane.getChildren().removeLast();
                              });
                          }
                        });
                  } catch (IOException ee) {
                      System.err.println("Error: " + ee.getMessage());
                  }
              });
          }
        };
        requestInfoButton.changeText("ข้อมูลคำร้อง",24, FontWeight.BOLD);
        addApproverButton.setPrefSize(200,30);
        requestInfoButton.setPrefSize(200,30);
        addApproverButton.changeBackgroundRadius(15);
        requestInfoButton.changeBackgroundRadius(15);
        addApproverButton.setStyle(addApproverButton.getStyle()+"-fx-border-color: black; -fx-border-width: 1;-fx-border-radius: 15;");
        requestInfoButton.setStyle(requestInfoButton.getStyle()+"-fx-border-color: black; -fx-border-width: 1;-fx-border-radius: 15;");
        addApproverButton.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/add-approver-icon-black.png")),30,30);
        requestInfoButton.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/request-info-icon-black.png")),30,30);
        menuHBox.getChildren().addAll(addApproverButton,requestInfoButton);

        HBox controlHBox = new HBox();
        controlHBox.setAlignment(Pos.CENTER);
        controlHBox.setSpacing(20);
        approveButton = new DefaultButton("transparent","#ECECEC","#00DE59"){
          @Override
          protected void handleClickEvent(){
              button.setOnMouseClicked(e->{
                  onRequestApproveButton();
              });
          }
        };
        approveButton.changeText("อนุมัติ",28, FontWeight.NORMAL);

        rejectButton = new DefaultButton("transparent","#ECECEC","#FF5D5D");
        rejectButton.changeText("ไม่อนุมัติ",28, FontWeight.NORMAL);

        DefaultButton reloadButton = new DefaultButton("transparent","#ECECEC","#FF5D5D"){
            @Override
            protected void handleClickEvent(){
                button.setOnMouseClicked(e->{
                    refreshAllData();
                });
            }
        };
        reloadButton.changeText("",28, FontWeight.NORMAL);

        approveButton.setPrefSize(150,30);
        reloadButton.setPrefSize(50,50);
        approveButton.changeBackgroundRadius(15);
        reloadButton.changeBackgroundRadius(50);
        approveButton.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/approve-icon-green.png")),50,30);
        reloadButton.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/reload-icon-orange.png")),30,30);
        controlHBox.getChildren().addAll(rejectButton,approveButton,reloadButton);

        requestMenuHBox.getChildren().addAll(menuHBox,controlHBox);
        requestMenuHBox.setAlignment(Pos.CENTER);
//        requestMenuHBox.setSpacing(200);

    }

    private void initDataVBox(){
        dataVBox.getChildren().add(new VBox());
    }

    private void initRequestApproverTableView(){
        requestApproverTableView = new TableView();
        VBox vBox = (VBox) dataVBox.getChildren().getLast();
        vBox.getChildren().clear();
        vBox.getChildren().add(requestApproverTableView);
        if(tableView!=null){
            theme.removeObserver(tableView);
        }
        tableView = new DefaultTableView<>(requestApproverTableView){
            @Override
            public void updateAction() {
                if (theme.getTheme() != null) {
                    if (theme.getTheme().get("name").equalsIgnoreCase("dark")) {
                        setStyleSheet("/ku/cs/styles/department/pages/request/dark-department-request-approver-table-stylesheet.css");
                    } else {
                        setStyleSheet("/ku/cs/styles/department/pages/request/department-request-approver-table-stylesheet.css");
                    }

                }
            }
        };
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
         theme.addObserver(tableView);
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
                    if(!a.getStatus().equals("รอส่งคณะ") && session.getUser() instanceof DepartmentUser)goToFaculty = true;
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
        requestOwner = (Student) studentList.findUserByUUID(request.getOwnerUUID());

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
            if(sumAprroved == sumApprover && session.getUser() instanceof DepartmentUser){
                if(!request.getStatusNext().equals("คำร้องดำเนินการครบถ้วน")){
                    approveButton.setDisable(false);
                }
            }
            if (!request.getStatusNext().equals("คำร้องส่งต่อให้คณบดี") && session.getUser() instanceof FacultyUser && !(session.getUser() instanceof DepartmentUser)) {
                addApproverButton.setDisable(true);
            }
            if (!request.getStatusNext().equals("คำร้องส่งต่อให้หัวหน้าภาควิชา") && session.getUser() instanceof DepartmentUser) {
                addApproverButton.setDisable(true);
            }
            if(sumApproverFaculty == sumApprovedFaculty && session.getUser() instanceof FacultyUser){
                if(!request.getStatusNext().equals("คำร้องดำเนินการครบถ้วน")){
                    approveButton.setDisable(false);
                } else {
                    addApproverButton.setDisable(true);
                }
            }
        }

        if(goToFaculty && session.getUser() instanceof DepartmentUser){
            approveButton.setDisable(true);
//            addApproverButton.setDisable(true);
//            rejectButton.setDisable(true);
        }

        theme.notifyObservers(theme.getTheme());



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
                            String popUpPath = "/ku/cs/views/edit-approver-pane.fxml";
                            try {
                                if (currentPopupStage == null || !currentPopupStage.isShowing()) {
                                    currentPopupStage = new Stage();
                                }

                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(popUpPath));
                                Pane editPane = fxmlLoader.load();

                                EditApproverController controller = fxmlLoader.getController();
                                controller.setApprover(approver);
                                controller.setStage(currentPopupStage);
                                controller.setApproverDetail(
                                        approver.getFirstname(),
                                        approver.getLastname(),
                                        approver.getRole()
                                );

                                Scene scene = new Scene(editPane);
                                currentPopupStage.setScene(scene);
                                currentPopupStage.setTitle("Edit Approver");
                                currentPopupStage.initModality(Modality.APPLICATION_MODAL);

                                currentPopupStage.setOnHidden(event -> {
                                    refreshAllData();
                                });

                                currentPopupStage.show();

                            } catch (IOException ex) {
                                System.err.println("Error loading popup: " + ex.getMessage());
                            }
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
                    if (session.getUser() instanceof DepartmentUser) {
                        if (approver instanceof AdvisorApprover || approver instanceof FacultyApprover) {
                            editButton.setDisable(true);
    //                        signatureButton.setDisable(true);
                            approveButton.setDisable(true);
                        }
                    } else if (session.getUser() instanceof FacultyUser) {
                        if (approver instanceof AdvisorApprover || approver instanceof DepartmentApprover) {
                            editButton.setDisable(true);
                            approveButton.setDisable(true);
                        }
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
                    Approver approver = getTableView().getItems().get(getIndex());
                    String role = approver.getRole();
                    line1.changeText(role);

                    vBox.getChildren().clear();
                    vBox.getChildren().addAll(line1,line2);
                    String extend = "";
                    if(role.contains("คณบดี")){
                        String facultyUUID = requestOwner.getFacultyUUID().toString();
                        extend = "คณะ" + facultyList.getFacultyByUuid(facultyUUID).getName();
                    }else if(role.contains("หัวหน้าภาควิชา")){
                        String departmentUUID = requestOwner.getDepartmentUUID().toString();
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
                icon.getImageView().setFitWidth(40);
                icon.getImageView().setFitHeight(40);
                icon.setClipImage(10,10);
                setPadding(new Insets(0,20,0,0));
            }
            private String getStatusImageName(String status){
                String fileName = "status-waiting-yellow.png";
                switch (status){
                    case "รออาจารย์ที่ปรึกษา", "รอคณะดำเนินการ":
                        fileName = "approver-waiting-other.png";
                        break;
                    case "เรียบร้อย":
                        fileName = "approver-approved-green.png";
                        break;
                    case "ไม่อนุมัติ":
                        fileName = "approver-rejected-red.png";
                        break;
                    case "รอภาควิชาดำเนินการ", "รออัปโหลด":
                        fileName = "approver-waiting-upload-blue.png";
                        break;
                    case "รอส่งคณะ":
                        fileName = "approver-waiting-send-faculty.png";
                        break;
                }
                return fileName;
            }
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    Approver approver = getTableView().getItems().get(getIndex());
                    String status = approver.getStatus();
                    String statusImageFileName = getStatusImageName(status);

                    if (status.length() > 10) {
                        line1.changeText("",24, FontWeight.BOLD);
                        line1.setMaxHeight(60);
                        line1.setWrapText(true);
                        line1.setTextAlignment(TextAlignment.CENTER);
                    }
                    if(status.equals("รอภาควิชาดำเนินการ")){
                        line1.changeText("",18, FontWeight.BOLD);
                    }
                    if(status.equals("รออาจารย์ที่ปรึกษา")){
                        line1.setMaxWidth(80);
                    }
                    if(status.equals("รอคณะดำเนินการ")){
                        line1.setMaxWidth(75);
                    }
                    line1.changeText(status);
                    if(theme.getTheme() != null){
                        line1.update(theme.getTheme());
                    }
                    if(status.equals("เรียบร้อย")){
                        line1.changeLabelColor("green");
                    } else if (status.equals("ไม่อนุมัติ")) {
                        line1.changeLabelColor("red");
                    }else {
                        line1.changeLabelColor("black");
                        if(theme.getTheme() != null){
                            line1.changeLabelColor(theme.getTheme().get("textColor"));
                        }
                    }


                    HBox statusBox = new HBox();
                    statusBox.setPrefWidth(120);
//                    statusBox.setStyle("-fx-background-color: red");
                    statusBox.setAlignment(Pos.CENTER);
                    statusBox.getChildren().add(line1);

                    hBox.getChildren().clear();
                    hBox.getChildren().addAll(statusBox,icon.getImageView());

                    icon.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/"+statusImageFileName)));
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
        String status = request.getStatusNext();
        String imageFileName = "status-waiting-yellow.png";
        if(status.equals("คำร้องดำเนินการครบถ้วน")){
            imageFileName = "status-approved-green.png";
        }
        if(status.equals("คำร้องถูกปฏิเสธ")) {
            imageFileName = "status-rejected-red.png";
        }
        statusImage.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/" + imageFileName)));

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
            if(selectedApprover.getDisableView()){//disable view => true
                initEditThisTierException("คำร้องยังไม่ถึงระดับของผู้อนุมัติ");
                return;
            }

            if(!approverStatus.equals("ไม่อนุมัติ")) {
                for (Approver a : filterApproverList.getApprovers()) {
                    if (a.getStatus().equals("ไม่อนุมัติ")) {
                        initEditThisTierException("มีผู้อนุมัติที่ปฏิเสธ");
                        return;
                    }
                }
            }

            if (session.getUser() instanceof DepartmentUser) {
                switch (tier){
                    case "advisor":
                        initEditPreviousTier();
                        break;
                    case "faculty":
                        initEditFaculty();
                        break;
                    case "department":
                        initEditThisTier();
                        break;
                    case "other":
                        initEditThisTier();
                        break;
                }
            } else if (session.getUser() instanceof FacultyUser) {
                switch (tier){
                    case "faculty":
                        initEditThisTier();
                        break;
                    case "advisor":
                    case "department":
                    case "other":
                        initEditPreviousTier();
                        break;
                }
            }

        }

        Class<?>[] notifyClass = {UploadPDFStack.class};
        theme.notifyObservers(theme.getTheme(),notifyClass);

    }

    private void initEditThisTier(){
        String approverStatus = selectedApprover.getStatus();
        String statusImageFileName = "editor-approver-fallback.png";
        if (session.getUser() instanceof DepartmentUser) {
            switch (approverStatus){
                case "รอภาควิชาดำเนินการ", "รออัปโหลด":
                    initEditThisTierWaitUpload();
                    break;
                case "เรียบร้อย":
    //                statusImageFileName = "approver-approve-green-check.png";
                    initEditThisTierApprove();
                    break;
                case "ไม่อนุมัติ":
    //                statusImageFileName = "approver-reject-red-x.png";
                    initEditThisTierReject();
                    break;
            }
        } else if (session.getUser() instanceof FacultyUser) {
            switch (approverStatus){
                case "รอคณะดำเนินการ", "รออัปโหลด":
                    initEditThisTierWaitUpload();
                    break;
                case "เรียบร้อย":
                    initEditThisTierApprove();
                    break;
                case "ไม่อนุมัติ":
                    initEditThisTierReject();
                    break;
            }
        }
    }


    private void initEditThisTierException(String error){
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
        titleLabel.changeLabelColor(this.textThemeColorHex);
        titleLabel.changeText("ขัดข้อง",48, FontWeight.BOLD);
        container.getChildren().add(titleLabel);
        editorVBox.getChildren().add(container);

//        container = newEditorContainer();
//        DefaultLabel statusLabel = new DefaultLabel("");
//        statusLabel.changeText("สถานะ " + approverStatus,32, FontWeight.NORMAL);
//        container.getChildren().add(statusLabel);
//        editorVBox.getChildren().add(container);

        container = newEditorContainer();
        DefaultLabel errorLabel = new DefaultLabel("");
        errorLabel.changeLabelColor(this.textThemeColorHex);
        errorLabel.changeText(error,28, FontWeight.NORMAL);
        errorLabel.setWrapText(true);
        container.getChildren().add(errorLabel);
        editorVBox.getChildren().add(container);

    }

    private void initEditThisTierReject(){
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
        statusLabel.changeLabelColor(this.textThemeColorHex);
        statusLabel.changeText(approverStatus,48, FontWeight.BOLD);
        container.getChildren().add(statusLabel);
        editorVBox.getChildren().add(container);

        if(approverStatus.equals("ไม่อนุมัติ") && request.getReasonForNotApprove() != null){
            container = newEditorContainer();
            DefaultLabel rejectReasonLabel = new DefaultLabel("");
            rejectReasonLabel.changeLabelColor(this.textThemeColorHex);
            rejectReasonLabel.changeText("เหตุผล: " + request.getReasonForNotApprove(),24, FontWeight.NORMAL);
            rejectReasonLabel.setWrapText(true);
            container.getChildren().add(rejectReasonLabel);
            editorVBox.getChildren().add(container);
        }
    }

    private void initEditThisTierApprove(){
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
        statusLabel.changeLabelColor(this.textThemeColorHex);
        statusLabel.changeText(approverStatus,48, FontWeight.BOLD);
        container.getChildren().add(statusLabel);
        editorVBox.getChildren().add(container);
    }
    private void initEditThisTierWaitUpload(){
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
        nameLabel.changeLabelColor(this.textThemeColorHex);
        nameLabel.changeText(selectedApprover.getName(),28, FontWeight.BOLD);
        container.getChildren().add(nameLabel);
        nameVBox.getChildren().add(nameLabel);

        //Role
        String role = selectedApprover.getRole();
        String extend = "";
        if(role.contains("คณบดี")){
            String facultyUUID = requestOwner.getFacultyUUID().toString();
            extend = " คณะ" + facultyList.getFacultyByUuid(facultyUUID).getName();
        }else if(role.contains("หัวหน้าภาควิชา")){
            String departmentUUID = requestOwner.getDepartmentUUID().toString();
            extend = " " + departmentList.getDepartmentByUuid(departmentUUID).getName();
        }
        container = newEditorContainer();
        DefaultLabel roleLabel = new DefaultLabel("");
        roleLabel.changeLabelColor(this.textThemeColorHex);
        roleLabel.changeText(role + extend,24, FontWeight.NORMAL);
        container.getChildren().add(roleLabel);
        nameVBox.getChildren().add(container);
        editorVBox.getChildren().add(nameVBox);

        //Signature Uploader
        container = newEditorContainer();
        signatureUploader = new UploadPDFStack("signatures",
                selectedApprover.getSignatureFilename(),
                selectedApprover.getSignatureFile()
        );
        container.getChildren().add(signatureUploader);
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
                if(hBox.getChildren().getFirst() instanceof UploadPDFStack){
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
                                                signatureUploader.cancelUploadedImage();//IF CLICKED
                                                signatureUploader.cancelDeleteImage();//IF CLICKED
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
                                                        titleLabel.changeText("ปฏิเสธคำร้อง",32, FontWeight.BOLD);
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
                                                        if(role.contains("คณบดี")){
                                                            String facultyUUID = requestOwner.getFacultyUUID().toString();
                                                            extend = "คณะ" + facultyList.getFacultyByUuid(facultyUUID).getName();
                                                        }else if(role.contains("หัวหน้าภาควิชา")){
                                                            String departmentUUID = requestOwner.getDepartmentUUID().toString();
                                                            extend = departmentList.getDepartmentByUuid(departmentUUID).getName();
                                                        }

                                                        data.changeText(role + extend ,28, FontWeight.NORMAL);
                                                        container.getChildren().addAll(prefix,data);
                                                        container.setAlignment(Pos.CENTER_LEFT);
                                                        container.setSpacing(20);
                                                        mainBox.getChildren().add(container);


                                                        container = new HBox();
                                                        reasonTextField =  new TextFieldStack("",450,100);
                                                        reasonTextField.setPlaceholder("เหตุผลที่ปฏิเสธ");
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

                                                        HBox lineEnd = new HBox(firstButton, secondButton);
                                                        lineEnd.setAlignment(Pos.CENTER);
                                                        lineEnd.setSpacing(20);
                                                        mainBox.getChildren().addAll(lineEnd);
                                                        stackPane.getChildren().add(mainBox);
                                                    }@Override
                                                    protected void handleFirstButton(){
                                                        firstButton.setOnMouseClicked(e -> {
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
                                                    protected void handleSecondButton(){
                                                        secondButton.setOnMouseClicked(e ->{
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

    private void initEditPreviousTier(){
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
        statusLabel.changeLabelColor(this.textThemeColorHex);
        statusLabel.changeText(approverStatus,48, FontWeight.BOLD);
        container.getChildren().add(statusLabel);
        editorVBox.getChildren().add(container);

        if(approverStatus.equals("ไม่อนุมัติ") && request.getReasonForNotApprove() != null){
            container = newEditorContainer();
            DefaultLabel rejectReasonLabel = new DefaultLabel("");
            rejectReasonLabel.changeLabelColor(this.textThemeColorHex);
            rejectReasonLabel.changeText("เหตุผล: " + request.getReasonForNotApprove(),24, FontWeight.NORMAL);
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
            case "รอคณะดำเนินการ", "เรียบร้อย", "ไม่อนุมัติ":
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
        statusLabel.changeLabelColor(this.textThemeColorHex);
        statusLabel.changeText(approverStatus,48, FontWeight.BOLD);
        container.getChildren().add(statusLabel);
        editorVBox.getChildren().add(container);

        if(approverStatus.equals("ไม่อนุมัติ") && request.getReasonForNotApprove() != null){
            container = newEditorContainer();
            DefaultLabel rejectReasonLabel = new DefaultLabel("");
            rejectReasonLabel.changeLabelColor(this.textThemeColorHex);
            rejectReasonLabel.changeText("เหตุผล: " + request.getReasonForNotApprove(),24, FontWeight.NORMAL);
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
            signatureUploader.saveUploadedImage();//IF CLICKED
            signatureUploader.performDeleteImage();//IF CLICKED

            selectedApprover.setSignatureFile(signatureUploader.getCurFileName());
            approverDatasource.writeData(approverList);

            requestApproverTableView.refresh();
            selectedApproverListener();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    private void onRejectApprover(String reasonForNotApprove){
        try {
            requestDatasource.appendData(request,"log");
            selectedApprover.setStatus("ไม่อนุมัติ");
            request.setReasonForNotApprove(reasonForNotApprove);
            if (session.getUser() instanceof DepartmentUser) {
                request.setStatusNow("ปฏิเสธโดยหัวหน้าภาควิชา");
                request.setStatusNext("คำร้องถูกปฏิเสธ");
            } else if (session.getUser() instanceof FacultyUser) {
                request.setStatusNow("ปฏิเสธโดยคณบดี");
                request.setStatusNext("คำร้องถูกปฏิเสธ");
            }
            request.setTimeStamp(LocalDateTime.now());

            approverDatasource.writeData(approverList);
            requestDatasource.writeData(requestList);

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

            approverDatasource.writeData(approverList);

            refreshAllData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onSignatureImageView(Approver approver){
        try {
            PDFDatasource datasource = new PDFDatasource();
            File pdfFile = datasource.getPDFFile(approver.getSignatureFile());
            if(pdfFile!=null){
                mainStackPane.getChildren().add(new PDFViewPopup(pdfFile){
                    @Override
                    protected void initPopupView(){
                        super.initPopupView();
                        titleLabel.changeText("ลายเซ็น");
                    }
                    @Override
                    protected void handleFirstButton(){
                        firstButton.setOnMouseClicked(e -> {
                            datasource.downloadFile(approver.getSignatureFile());
                        });
                    }
                    @Override
                    protected void handleSecondButton(){
                        secondButton.setOnMouseClicked(e ->{
                            mainStackPane.getChildren().removeLast();
                        });
                    }
                });
            }
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
            requestDatasource.appendData(request,"log");
            request.setTimeStamp(LocalDateTime.now());
            request.setFacultyUUID(requestOwner.getFacultyUUID());
            request.setStatusNow("อนุมัติโดยหัวหน้าภาควิชา");
            request.setStatusNext("คำร้องส่งต่อให้คณบดี");

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
            if(sumApprover == 0 || session.getUser() instanceof FacultyUser){
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
            requestDatasource.appendData(request,"log");
            if(sumApprover != 0){
                for(Approver a : filterApproverList.getApprovers()){
                    if(a instanceof FacultyApprover){
                        a.setStatus("เรียบร้อย");
                    }
                }
            }

            request.setTimeStamp(LocalDateTime.now());
            if (session.getUser() instanceof DepartmentUser) {
                request.setStatusNow("อนุมัติโดยหัวหน้าภาควิชา");
                request.setStatusNext("คำร้องดำเนินการครบถ้วน");
            } else if (session.getUser() instanceof FacultyUser) {
                request.setStatusNow("อนุมัติโดยคณบดี");
                request.setStatusNext("คำร้องดำเนินการครบถ้วน");
            }

            approverDatasource.writeData(approverList);
            requestDatasource.writeData(requestList);

            refreshAllData();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(HashMap<String, String> data) {
        mainStackPane.setStyle(mainStackPane.getStyle()+"-fx-background-color: " + data.get("secondary") + ";");
        this.textThemeColorHex = data.get("textColor");
    }
}
