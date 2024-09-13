package ku.cs.controllers.department;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.FXRouter;
import ku.cs.services.UserListFileDatasource;
import ku.cs.services.utils.DateTools;
import ku.cs.views.components.*;

import java.io.IOException;
import java.time.LocalDateTime;


public class AddNisitController {
    @FXML private StackPane mainStackPane;
    @FXML private Button backButton;
    @FXML private Label pageTitleLabel;
    @FXML private Label tableViewLabel;
    private double editorHBoxWidth;
    private double editorHBoxHeight;

    @FXML private TableView<User> nisitTableView;
    @FXML private VBox nisitEditorVBox;
    @FXML private Button saveNisitListButton;
    @FXML private Button resetNisitListButton;
    @FXML private Button addNisitButton;
    @FXML private Button addNisitCSVButton;
    private TextFieldStack nisitFirstnameTextField;
    private TextFieldStack nisitLastnameTextField;
    private TextFieldStack nisitIdTextField;
    private UserList users;
    private UserListFileDatasource datasource;
    private User selectedUser;
    private DefaultLabel editorErrorLabel;
    private boolean editMode;
    private boolean showEdit;

    @FXML
    public void initialize(){
        editorErrorLabel = new DefaultLabel("");
        initTableView();
        users = new UserList();
//        try {
//            users.addUser("6610402230","no-username","student","ศิริสุข","ทานธรรม", DateTools.localDateTimeToFormatString(User.DATE_FORMAT, LocalDateTime.now()),"no-email","123456789","science","computer science");
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        refreshTableData();

        selectedUser = null;
        selectedUserListener();

        this.editorHBoxWidth = 400;
        this.editorHBoxHeight = 50;

        initLabel();
        initButton();
    }

    private void initButton(){
        new RouteButton(backButton,"department-staff-nisit-management","transparent","#a6a6a6","#000000");
        DefaultButton resetNisitListBT = new DefaultButton(resetNisitListButton,"#FF8080","#BC5F5F","white"){
          @Override
          protected void handleClickEvent() {
              getButton().setOnMouseClicked(e->{
                  mainStackPane.getChildren().add(new ConfirmStack("ยืนยัน","คุณต้องการรีเซ็ตข้อมูลทั้งหมดใช้หรือไม่"){
                      @Override
                      protected void handleAcceptButton(){
                          getAcceptButton().setOnMouseClicked(e -> {
                              System.out.println("Accept button clicked");
                              mainStackPane.getChildren().removeLast();
                              onResetButton();
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
              });
          }
        };
        resetNisitListBT.changeBackgroundRadius(15);
        DefaultButton saveAddNisitBT = new DefaultButton(saveNisitListButton,"#78D88C","#5AA469","white"){
            @Override
            protected void handleClickEvent() {
                getButton().setOnMouseClicked(e -> {
                    System.out.println("saveAddNisit clicked");
                    onSaveAddNisitButton();
                });
            }
        };
        saveAddNisitBT.changeBackgroundRadius(15);
        DefaultButton addNisitBT = new DefaultButton(addNisitButton,"#A4EFFF","#8ED2E1","black"){
            @Override
            protected void handleClickEvent() {
                getButton().setOnMouseClicked(e -> {
                    System.out.println("addNisitButton clicked");
                    onAddNisitButton();
                });
            }
        };
        addNisitBT.changeBackgroundRadius(30);
        DefaultButton addNisitCSVBT = new DefaultButton(addNisitCSVButton,"#FFA1D9","#E490C2","black"){
            @Override
            protected void handleClickEvent() {
                getButton().setOnMouseClicked(e -> {
                    System.out.println("addNisitCSVButton clicked");
                    mainStackPane.getChildren().add(new ConfirmStack("Alert","ระบบยังไม่ได้ทำโปรดเข้าใจ"){
                        @Override
                        protected void initialize(){
                            super.initialize();
                            getAcceptButton().changeText("เข้าใจ");
                            getDeclineButton().changeText("เข้าใจ");
                        }
                        @Override
                        protected void handleAcceptButton(){
                            getAcceptButton().setOnMouseClicked(e -> {
                                System.out.println("Accept button clicked");
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
                });
            }
        };
        addNisitCSVBT.changeBackgroundRadius(30);


    }
    private void initLabel(){
        new DefaultLabel(pageTitleLabel);
        new DefaultLabel(tableViewLabel);
    }
    private void initTableView(){
        DefaultTableView<User> nisitTable = new DefaultTableView(nisitTableView){
            @Override
            protected void handleCLick() {
                getTableView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
                    @Override
                    public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                        if (newValue != null) {
                            selectedUser = newValue;
                            selectedUserListener();
                        }
                    }
                });
            }
        };
        nisitTable.getTableView().getColumns().clear();
        nisitTable.getTableView().getItems().clear();

        nisitTable.addColumn("รหัสนิสิต","id");
        nisitTable.addColumn("ชื่อ-นามสกุล","name");
        nisitTable.addColumn("คณะ","faculty");
        nisitTable.addColumn("ภาควิชา","department");
        nisitTable.getTableView().getColumns().add(newDeleteColumn());
//        nisitTable.addStyleSheet("/ku/cs/styles/department/pages/nisit-management/department-nisit-management-table-stylesheet.css");

    }
    private void selectedUserListener(){
        if(selectedUser != null){
            showEdit = true;
            editMode = true;
        }else {
            showEdit = false;
            editMode = false;
        }
        initNisitEditor(selectedUser);
        toggleEditFiled();
        nisitEditorVBox.setDisable(!showEdit);
    }
    private void refreshTableData(){
        nisitTableView.getItems().clear();

        for(User user : users.getUsers()){
            if(user.isRole("student")){
//                System.out.println(">>>> " + user);
                nisitTableView.getItems().add(user);
            }
        }
        nisitTableView.getSortOrder().clear();
        TableColumn nisitedCol = nisitTableView.getColumns().get(0);
        nisitedCol.setSortable(true);
        nisitTableView.getSortOrder().add(nisitedCol);
        nisitedCol.setSortType(TableColumn.SortType.ASCENDING);
        nisitTableView.sort();
        nisitedCol.setSortable(false);

    }
    private void initNisitEditor(User user){

        ObservableList<Node> children =  nisitEditorVBox.getChildren();
        children.clear();
        HBox container;
        if(user != null){
            //ERROR LABEL
            container = newEditorContainerHBox();
            setEditorErrorLabel("");
            container.setAlignment(Pos.CENTER);
            container.setPrefHeight(20);
            container.getChildren().add(editorErrorLabel);
            children.add(container);

            //TEXT FIELDS
            container = newEditorContainerHBox();
            container.getChildren().add(nisitIdTextField = new TextFieldStack(user.getId(),editorHBoxWidth,editorHBoxHeight));
            container.setAlignment(Pos.CENTER);
            children.add(container);
            container = newEditorContainerHBox();
            container.getChildren().add(nisitFirstnameTextField = new TextFieldStack(user.getFirstname(),editorHBoxWidth,editorHBoxHeight));
            container.setAlignment(Pos.CENTER);
            children.add(container);
            container = newEditorContainerHBox();
            container.getChildren().add(nisitLastnameTextField = new TextFieldStack(user.getLastname(),editorHBoxWidth,editorHBoxHeight));
            container.setAlignment(Pos.CENTER);
            children.add(container);

            //EDIT SAVE CANCEL BUTTON
            container = newEditorContainerHBox();
            Button button;
            button = new Button();
            button.setId("editButton");
            container.getChildren().add(button);
            children.add(container);
            button = new Button();
            button.setId("cancelButton");
            container.getChildren().add(button);

        }else{
            nisitEditorVBox.setAlignment(Pos.CENTER);
            container = newEditorContainerHBox();
            DefaultLabel fallbackLabel = new DefaultLabel("");
            fallbackLabel.changeText("No user selected!",18,FontWeight.NORMAL);
            fallbackLabel.changeLabelColor("black");
            container.getChildren().add(fallbackLabel);
            container.setAlignment(Pos.CENTER);
            children.add(container);
        }

    }
    private HBox newEditorContainerHBox(){
        double w = editorHBoxWidth;
        double h = editorHBoxHeight;
        HBox container = new HBox();
        container.setPrefSize(w,h);
        return container;
    }
    private void toggleEditFiled(){
        editMode = !editMode;
        System.out.println(editMode);
        boolean editable = editMode;
        String editButtonColor = editable ? "#ABFFA4" : "#FFA4A4";
        String editButtonHoverColor = editable ? "#80BF7A" : "#E19494";
        String cancelButtonColor = "#FFA4A4";
        String cancelButtonHoverColor = "#E19494";

        String buttonLabelColor = editable ? "#000000" : "#000000";
        String editButtonLabel = editable ? "บันทึก" : "แก้ไข";
        String cancelButtonLabel = "ยกเลิก";

        nisitEditorVBox.getChildren().forEach(node -> {
            if(node instanceof HBox){
                HBox hbox = (HBox) node;
                hbox.setSpacing(20);
                VBox.setMargin(hbox,new Insets(15,0,0,0));
                for(int i = 0;i < hbox.getChildren().size();i++){
                    Node child = hbox.getChildren().get(i);
                    if(child instanceof TextFieldStack){
                        TextFieldStack t = (TextFieldStack) child;
                        t.toggleTextField(editMode);
//                        if(i == 0){
//                            HBox.setMargin(child,new Insets(0,0,0,0));
//                        }
                    }else if(child instanceof StackPane){
                        child.setVisible(editMode);
                        child.setDisable(!editMode);

                    }else if(child instanceof Button){
                        hbox.setAlignment(Pos.CENTER);
                        Button button = (Button) child;
                        if(button.getId().equals("editButton")){
                            DefaultButton b = new DefaultButton(button, editButtonColor, editButtonHoverColor, buttonLabelColor){
                                @Override
                                protected void handleClickEvent(){
                                    button.setOnMouseClicked(e -> {
                                        toggleEditFiled();
                                        if(!editMode){
                                            onSaveButton();
                                        }
                                    });
                                }
                            };
                            b.changeText(editButtonLabel,28, FontWeight.NORMAL);
                            b.changeBackgroundRadius(20);
                        }else if(button.getId().equals("cancelButton")){
                            DefaultButton b = new DefaultButton(button, cancelButtonColor, cancelButtonHoverColor, buttonLabelColor){
                                @Override
                                protected void handleClickEvent(){
                                    button.setOnMouseClicked(e -> {
                                        selectedUserListener();
                                    });
                                }
                            };
                            b.changeText(cancelButtonLabel,28, FontWeight.NORMAL);
                            b.changeBackgroundRadius(20);
                            button.setVisible(editMode);
                        }else if(button.getId().equals("deleteButton")){}
                    }
                }
            }
        });
    }
    private void onAddNisitButton(){
        mainStackPane.getChildren().add(
                new BlankPopupStack(){
                    private VBox mainBox;
                    private VBox topBox;
                    private VBox bottomBox;
                    private HBox lineEnd;
                    private VBox verticalTextFieldBox;
                    private TextFieldStack addNisitIdTextField;
                    private TextFieldStack addNisitFirstnameTextField;
                    private TextFieldStack addNisitLastnameTextField;
                    private DefaultLabel errorLabel;

                    @Override
                    protected void initPopupView(){
                        double mainWidth = 600;
                        double mainHeight = 600;
                        mainBox = new VBox();
                        topBox = new VBox();
                        bottomBox = new VBox();
                        mainBox.setMaxWidth(mainWidth);
                        mainBox.setMaxHeight(mainHeight);
                        mainBox.setStyle("-fx-background-color: white; -fx-background-radius: 50;");

                        topBox.setPrefWidth(mainWidth);
                        topBox.setPrefHeight(500);
                        topBox.setAlignment(Pos.TOP_CENTER);
                        //TITLE
                        VBox top = new VBox();
                        top.setAlignment(Pos.CENTER);
                        top.setPrefHeight(100);
                        DefaultLabel title = new DefaultLabel("");
                        title.changeText("เพิ่มรายชื่อนิสิต",32,FontWeight.BOLD);
                        top.getChildren().add(title);


                        //TEXT FIELDS
                        verticalTextFieldBox = new VBox();
                        verticalTextFieldBox.setAlignment(Pos.CENTER);
                        verticalTextFieldBox.setSpacing(30);
                        verticalTextFieldBox.setMaxWidth(mainWidth-100);

                        ObservableList<Node> children =  verticalTextFieldBox.getChildren();
                        children.clear();

                        children.add(addNisitIdTextField = new TextFieldStack("NisitID",mainWidth-100,60));
                        children.add(addNisitFirstnameTextField = new TextFieldStack("Firstname",mainWidth-100,60));
                        children.add(addNisitLastnameTextField = new TextFieldStack("Lastname",mainWidth-100,60));
                        children.add(errorLabel = new DefaultLabel(""));

                        children.forEach(child ->{
                            if(child instanceof TextFieldStack){
                                TextFieldStack t = (TextFieldStack) child;
                                t.toggleTextField(true);
                                t.setStyle("-fx-border-color: black;-fx-border-radius: 20;");

                            }
                        });
                        topBox.getChildren().addAll(top,verticalTextFieldBox);


                        //BOTTOM
                        bottomBox.setPrefWidth(mainWidth);
                        bottomBox.setPrefHeight(100);

                        lineEnd = new HBox(acceptButton,declineButton);
                        lineEnd.setAlignment(Pos.CENTER);
                        lineEnd.setSpacing(20);

                        bottomBox.getChildren().add(lineEnd);
                        mainBox.getChildren().addAll(topBox, bottomBox);
                        stackPane.getChildren().add(mainBox);
                    }

                    @Override
                    protected void handleAcceptButton(){
                        acceptButton.setOnMouseClicked(e -> {
                            System.out.println("Accept button clicked");
                            UserList tmpUserList = new UserList();
                            ObservableList<Node> children =  verticalTextFieldBox.getChildren();
                            children.forEach(child ->{
                                if(child instanceof TextFieldStack){
                                    TextFieldStack t = (TextFieldStack) child;
                                    t.toggleTextField(false);
                                }
                            });
                            try {
                                tmpUserList.addUser(
                                        addNisitIdTextField.getData(),
                                        "no-username",
                                        "student",
                                        addNisitFirstnameTextField.getData(),
                                        addNisitLastnameTextField.getData(),
                                        DateTools.localDateTimeToFormatString(User.DATE_FORMAT,LocalDateTime.now()),
                                        "no-email",
                                        "DEFAULT",
                                        "MyFaculty",
                                        "MyDepartment"
                                );
                                users.getUsers().addAll(tmpUserList.getUsers());
                                setEditorErrorLabel("");
                                mainStackPane.getChildren().removeLast();
                                refreshTableData();
                            } catch (UserException err){
                                errorLabel.changeText(err.getMessage(),24,FontWeight.NORMAL);
                                errorLabel.changeLabelColor("red");
                                System.out.println("UserException Error : " + err.getMessage());
                                children.forEach(child ->{
                                    if(child instanceof TextFieldStack){
                                        TextFieldStack t = (TextFieldStack) child;
                                        t.toggleTextField(true);
                                    }
                                });
                                err.printStackTrace();
                            }
                        });
                    }
                    @Override
                    protected void handleDeclineButton(){
                        declineButton.setOnMouseClicked(e ->{
                            System.out.println("Decline button clicked");
                            mainStackPane.getChildren().removeLast();
                        });
                    }

                }
        );
    }
    private void onResetButton(){
        users = new UserList();
        refreshTableData();
        selectedUser = null;
        selectedUserListener();
    }
    private void onSaveAddNisitButton(){
        if(users.getUsers().isEmpty()){
            mainStackPane.getChildren().add(new ConfirmStack("Alert","ไม่สามารถบันทึกข้อมูลได้เนื่องจากไม่มีข้อมูล"){
                @Override
                protected void initialize(){
                    super.initialize();
                    getAcceptButton().changeText("เข้าใจ");
                    getDeclineButton().changeText("เข้าใจ");
                }
                @Override
                protected void handleAcceptButton(){
                    getAcceptButton().setOnMouseClicked(e -> {
                        System.out.println("Accept button clicked");
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
        }else{
            mainStackPane.getChildren().add(new ConfirmStack("ยืนยัน","ยืนยันเพื่อบันทึกข้อมูลนิสิต"){
                @Override
                protected void handleAcceptButton(){
                    getAcceptButton().setOnMouseClicked(e -> {
                        System.out.println("Accept button clicked");
                        try {
                            datasource = new UserListFileDatasource("data","student.csv");
                            UserList studentUserList = datasource.readData();

                            studentUserList.getUsers().addAll(users.getUsers());

                            datasource.writeData(studentUserList);
                        }catch (Exception err){
                            System.out.println("Error : [add-nisit-controller] on save error");
                            err.printStackTrace();
                        }finally {
                            try {
                                FXRouter.goTo("department-staff-nisit-management");
                            } catch (IOException err) {
                                throw new RuntimeException(err);
                            }
                        }
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


    }
    private void onSaveButton()  {
        try {
            selectedUser.setId(nisitIdTextField.getData());
            selectedUser.setFirstname(nisitFirstnameTextField.getData());
            selectedUser.setLastname(nisitLastnameTextField.getData());

            nisitTableView.refresh();
            selectedUserListener();

            setEditorErrorLabel("");
        } catch (UserException e){
            selectedUserListener();
            setEditorErrorLabel(e.getMessage());
            System.out.println("UserException Error : " + e.getMessage());
            toggleEditFiled();
            e.printStackTrace();
        }
    }
    private void onDeleteButton(User user){
        try {
            if(selectedUser != null && selectedUser.equals(user)){
                selectedUser = null;
            }
            users.deleteUserByObject(user);
            refreshTableData();
            selectedUserListener();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    private TableColumn<User,?> newDeleteColumn(){
        TableColumn<User, HBox> column = new TableColumn<>("");
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE

        column.setCellFactory(c -> new TableCell<>() {
            private Button actionButton = new Button();
            private final HBox hbox = new HBox(actionButton);
            {
                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.setPrefSize(35,35);
                DefaultButton b =new DefaultButton(actionButton,"transparent", "#e0e0e0", "#000000"){
                    @Override
                    protected void handleClickEvent() {
                        button.setOnMouseClicked(e -> {
                            User user = getTableView().getItems().get(getIndex());
                            System.out.println("DeleteButton clicked for item: " + user.getId() + " " + user.getName());
                            mainStackPane.getChildren().add(new ConfirmStack("ยืนยัน","คุณต้องการลบใช่มั้ย"){
                                @Override
                                protected void handleAcceptButton(){
                                    getAcceptButton().setOnMouseClicked(e -> {
                                        System.out.println("Accept button clicked");
                                        mainStackPane.getChildren().removeLast();
                                        onDeleteButton(user);
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

                        });
                    }
                };
                Image deleteButtonImage = new Image(getClass().getResourceAsStream("/images/pages/department/global/red-bin.png"));
//                b.setButtonSize(50,50);
                b.changeBackgroundRadius(20);
                b.setImage(deleteButtonImage,35,35);
//                b.changeText("",20, FontWeight.NORMAL);
            }
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // No content for empty cells
                } else {
                    setGraphic(hbox); // Set the HBox with the button as the cell's graphic
                }
            }
        });
        return column;
    }
    private void setEditorErrorLabel(String error){
        this.editorErrorLabel.changeLabelColor("red");
        this.editorErrorLabel.changeText(error,18, FontWeight.BOLD);
    }
}
