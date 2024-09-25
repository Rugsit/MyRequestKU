package ku.cs.controllers.department;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.request.*;
import ku.cs.models.request.approver.*;
import ku.cs.models.user.DepartmentUser;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.*;
import ku.cs.services.utils.DateTools;
import ku.cs.views.components.*;

import java.io.IOException;
import java.util.UUID;

public class RequestController {
    @FXML private Label pageTitleLabel;
    @FXML private Button backButton;
    @FXML private StackPane mainStackPane;
    @FXML private VBox dataVBox;
    @FXML private VBox requestInfoVBox;
    @FXML private HBox requestMenuHBox;
    @FXML private TableView<Approver> requestApproverTableView;
    private DefaultTableView<Approver> tableView;
    @FXML private VBox mainEditorVBox;
    @FXML private VBox statusVBox;
    @FXML private VBox editorVBox;
    private DefaultLabel reqType,reqNisitId,reqName,reqCreateTime,reqTimestamp;
    private DefaultButton addApproverButton, requestInfoButton, approveButton, rejectButton;

    private Request request;
    private User requestOwner;

    @FXML
    private Stage currentPopupStage;

    private UserListFileDatasource studentDatasource;
    DepartmentListFileDatasource departmentDatasource;
    FacultyListFileDatasource facultyDatasource;
    private ApproverListFileDatasource approverDatasource;
    private UserList studentList;
    private DepartmentList departmentList;
    private FacultyList facultyList;
    private ApproverList approverList;
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

        initLabel();
        initButton();

        if(session != null && session.getData() instanceof Request){
            request = (Request) session.getData();
            requestOwner = studentList.findUserByUUID(request.getOwnerUUID());
            initRequestInfoVBox();
            initRequestMenuHBox();
            initRequestApproverTableView();
            refreshTableData();
            initStatusVBox();

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
        approveButton = new DefaultButton("transparent","#a6a6a6","#00DE59");
        approveButton.changeText("อนุมัติ",28, FontWeight.NORMAL);

        rejectButton = new DefaultButton("transparent","#a6a6a6","#FF5D5D");
        rejectButton.changeText("ไม่อนุมัติ",28, FontWeight.NORMAL);
        controlHBox.getChildren().addAll(approveButton,rejectButton);

        requestMenuHBox.getChildren().addAll(menuHBox,controlHBox);
        requestMenuHBox.setAlignment(Pos.CENTER);
        requestMenuHBox.setSpacing(200);
    }

    private void initRequestApproverTableView(){
         tableView = new DefaultTableView<>(requestApproverTableView);
         tableView.getColumns().clear();
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
        tableView.getItems().clear();
        approverList = approverDatasource.readData();

        if(request != null){
            UUID requestUUID = request.getUuid();
            filterApproverList = approverList.getApproverList(requestUUID);
            //CHECK ADVISOR STATE
            String advisorStatus = "";
            int sumApprover = 0;
            int sumAprroved = 0;
            for(Approver a : filterApproverList.getApprovers()){
                if(a instanceof AdvisorApprover)advisorStatus = a.getStatus();
                if(a instanceof DepartmentApprover || a instanceof OtherApprover){
                    sumApprover++;
                    if(a.getStatus().equals("เรียบร้อย"))sumAprroved++;
                }
            }
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
    private void selectedApproverListener(){
        if(selectedApprover != null){
            initEditorVBox();
        }
    }

    private TableColumn<Approver,?> newEditButtonColumn(){
        TableColumn<Approver, VBox> column = new TableColumn<>("");
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE

        column.setCellFactory(c -> new TableCell<>() {
            private Button actionButton = new Button();
            private final VBox vbox = new VBox(actionButton);
            {
                setPrefSize(60,60);
//                setStyle("-fx-background-color: red");
                vbox.setAlignment(Pos.CENTER);
//                vbox.setPrefSize(35,35);
                DefaultButton b =new DefaultButton(actionButton,"transparent", "#e0e0e0", "#000000"){
                    @Override
                    protected void handleClickEvent() {
                        button.setOnMouseClicked(e -> {
                            Approver approver = getTableView().getItems().get(getIndex());
                            System.out.println("EDIT CLICK : " + approver.getName());
                        });
                    }
                };
                Image deleteButtonImage = new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/edit-approver-icon-blue.png"));
                b.changeBackgroundRadius(20);
                b.setImage(deleteButtonImage,40,40);
            }
            @Override
            protected void updateItem(VBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // No content for empty cells
                } else {
                    setGraphic(vbox); // Set the HBox with the button as the cell's graphic
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
                        line1.setMaxWidth(80);
                        line1.setMaxHeight(60);
                        line1.setWrapText(true);
                        line1.setTextAlignment(TextAlignment.CENTER);
                    }
                    line1.changeText(status);
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
        String tier = selectedApprover.getTier();

        switch (tier){
            case "advisor":
                initEditAdvisor();
                break;
            case "faculty":
                break;
            case "department":
                initEditDepartmentOther();
                break;
            case "other":
                initEditDepartmentOther();
                break;
        }



    }

    private void initEditDepartmentOther(){
        editorVBox.setAlignment(Pos.CENTER);
        editorVBox.setSpacing(15);

        HBox container;

        String approverStatus = selectedApprover.getStatus();
        String statusImageFileName = "editor-approver-fallback.png";
        switch (approverStatus){
            case "รออัพโหลด":
                //IMAGE
                container = newEditorContainer();
                SquareImage editorImage = new SquareImage(new ImageView());
                if(!selectedApprover.getSignatureFile().equals("no-signature")){

                }else{
                    editorImage.setImage(new Image(getClass().getResourceAsStream("/images/pages/department/department-staff-request/" + statusImageFileName)));
                }

                editorImage.getImageView().setFitHeight(150);
                editorImage.getImageView().setFitWidth(150);
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
                DefaultButton button;
                button = new DefaultButton("#ABFFA4","#80BF7A","black");
                button.changeText("บันทึก",24, FontWeight.NORMAL);
                button.setButtonSize(120,60);
                button.changeBackgroundRadius(15);
                container.getChildren().add(button);

                button = new DefaultButton("#FFA4A4","#E19494","black");
                button.changeText("ยกเลิก",24, FontWeight.NORMAL);
                button.setButtonSize(120,60);
                button.changeBackgroundRadius(15);
                container.getChildren().add(button);

                editorVBox.getChildren().add(container);
                break;
            case "เรียบร้อย":
                statusImageFileName = "approver-approve-green-check.png";
                break;
            case "ไม่อนุมัติ":
                statusImageFileName = "approver-reject-red-x.png";
                break;
        }
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
    }


    private HBox newEditorContainer(){
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        return container;
    }







}
