package ku.cs.controllers.department;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ku.cs.models.Session;
import ku.cs.models.user.DepartmentUser;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.FXRouter;
import ku.cs.services.ImageDatasource;
import ku.cs.services.UserListFileDatasource;
import ku.cs.services.utils.DateTools;
import ku.cs.views.components.*;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class NisitManagementController {
    @FXML private Label pageTitleLabel;
    @FXML private StackPane mainStackPane;

    @FXML private VBox nisitTableVBox;
    @FXML private HBox tableHeaderHBox;

    @FXML private Label tableViewLabel;
    @FXML private TableView<User> nisitTableView;
    @FXML private TextField searchTextFiled;

    @FXML private VBox nisitEditorVBox;

    private ImageView nisitImageView;
    private TextFieldStack nisitFirstnameTextField;
    private TextFieldStack nisitLastnameTextField;
    private TextFieldStack nisitIdTextField;
    private TextFieldStack nisitEmailTextField;
    private TextFieldStack nisitPasswordTextField;

    @FXML private Button addNisitButton;
    @FXML private Button backButton;
    @FXML private Button refreshButton;

    private double editorHBoxWidth;
    private double editorHBoxHeight;

    private boolean editMode;
    private boolean showEdit;
    private UserList users;
    private UserListFileDatasource datasource;
    private User selectedUser;
    private DefaultLabel editorErrorLabel;
    private UploadImageStack editorUploadImageStack;
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
        editorErrorLabel = new DefaultLabel("");
        initTableView();
        refreshTableData();
        nisitImageView = new ImageView();

        selectedUser = null;
        selectedUserListener();
//        this.editMode = true;

        this.editorHBoxWidth = 270;
        this.editorHBoxHeight = 50;

        initLabel();
        initButton();
//        nisitImage = new SquareImage(nisitImageView);
//        nisitImage.setClipImage(50,50);
//        nisitImage.setImage(new Image(getClass().getResourceAsStream("/images/profile-test.png")));
//        toggleEditFiled();
//        mainStackPane.getChildren().add(new ConfirmStack("ยืนยัน","This is a long label text that will wrap to the next line if it exceeds the maximum width."));
    }
    private void initLabel(){
        new DefaultLabel(pageTitleLabel);
        new DefaultLabel(tableViewLabel);
    }
    private void initButton(){
        new RouteButton(backButton,"department-staff-request-list","transparent","#a6a6a6","#000000");
        new RouteButton(addNisitButton,"department-staff-add-nisit","#ABFFA4","#80BF7A","#000000",session).changeBackgroundRadius(100);
        DefaultButton refreshBt = new DefaultButton(refreshButton,"transparent","white","#000000"){
            @Override
            protected void handleClickEvent() {
                getButton().setOnMouseClicked(e->{
                   refreshTableData();
                });
            }
        };
        refreshBt.changeBackgroundRadius(15);
    }
    private void initTableView(){
        DefaultTableView<User> nisitTable = new DefaultTableView(nisitTableView){
            @Override
            protected void handleClick() {
//                getTableView().setOnMouseClicked(event -> {
//                    Object selected = getTableView().getSelectionModel().getSelectedItem();
//                    if(selected instanceof User){
//                        selectedUser = (User) selected;
//                        selectedUserListener();
//                    }
//                });
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

        nisitTable.getTableView().getColumns().add(newAvatarImageColumn());
        nisitTable.addColumn("รหัสนิสิต","id");
        nisitTable.addColumn("ชื่อ-นามสกุล","name");
        nisitTable.getTableView().getColumns().add(newUsernameEmailColumn("ชื่อผู้ใช้/อีเมล"));
        nisitTable.getTableView().getColumns().add(newStatusLatestColumn("สถานะ/ล่าสุด"));
//        nisitTable.addColumn("รหัสเริ่มต้น","defaultPassword");
        nisitTable.getTableView().getColumns().add(newDeleteColumn());
        nisitTable.addStyleSheet("/ku/cs/styles/department/pages/nisit-management/department-nisit-management-table-stylesheet.css");

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
        datasource = new UserListFileDatasource("data","student.csv");
        users = datasource.readData();

        UserList filterList;
        if(session != null && session.getUser() != null){
            filterList = new UserList();
            UUID currentDepartment = ((DepartmentUser)session.getUser()).getDepartmentUUID();
            for(User user : users.getUsers("student")){
                if(((Student)user).getDepartmentUUID().equals(currentDepartment)){
                    try {
                        filterList.addUser(user);
                    } catch (UserException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            filterList = users;
        }

        for(User user : filterList.getUsers("student")){
            if(user.isRole("student")){
//                System.out.println(">>>> " + user);
                nisitTableView.getItems().add(user);
            }
        }
        nisitTableView.getSortOrder().clear();
        TableColumn nisitedCol = nisitTableView.getColumns().get(1);
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
            //USER IMAGE
            container = newEditorContainerHBox();
            nisitImageView.setPreserveRatio(true);
            nisitImageView.setFitWidth(200);
            nisitImageView.setFitHeight(200);
            SquareImage nisitImage = new SquareImage(nisitImageView);
            nisitImage.setClipImage(50,50);
            if(!user.getAvatar().equalsIgnoreCase("no-image")){
                ImageDatasource imageDatasource = new ImageDatasource("users");
                nisitImage.setImage(imageDatasource.openImage(user.getAvatar()));
            }

            container.getChildren().add(nisitImageView);
            container.setAlignment(Pos.CENTER);
            children.add(container);

            //ERROR LABEL
            container = newEditorContainerHBox();
            setEditorErrorLabel("");
            container.setAlignment(Pos.CENTER);
            container.setPrefHeight(20);
            //
//            container.setStyle("-fx-background-color: #fff");

            container.getChildren().add(editorErrorLabel);
            children.add(container);

            //TEST FIELDS
            container = newEditorContainerHBox();
            container.getChildren().add(nisitFirstnameTextField = new TextFieldStack(user.getFirstname()));
            container.getChildren().add(nisitLastnameTextField = new TextFieldStack(user.getLastname()));
            children.add(container);
            container = newEditorContainerHBox();
            container.getChildren().add(nisitIdTextField = new TextFieldStack(user.getId()));
            container.getChildren().add(nisitEmailTextField = new TextFieldStack(user.getEmail()));
            children.add(container);
            container = newEditorContainerHBox();
            nisitPasswordTextField = new TextFieldStack("PASSWORD",570,50);
            container.getChildren().add(nisitPasswordTextField);
            children.add(container);

            //UPLOAD IMAGE

            container = newEditorContainerHBox();
            container.setAlignment(Pos.CENTER);

            editorUploadImageStack = new UploadImageStack("users",user.getDefaultAvatarName(),user.getAvatar());
            container.getChildren().add(editorUploadImageStack);
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
            nisitImageView.setPreserveRatio(true);
            nisitImageView.setFitWidth(300);
            nisitImageView.setFitHeight(400);
            CropImage nisitImage = new CropImage(nisitImageView);
            nisitImage.setClipImage(50,50);
            container.getChildren().add(nisitImageView);
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
                                        editorUploadImageStack.cancelUploadedImage();//IF CLICKED
                                        editorUploadImageStack.cancelDeleteImage();//IF CLICKED
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
    private void onSaveButton()  {
        try {
            editorUploadImageStack.saveUploadedImage();//IF CLICKED
            editorUploadImageStack.performDeleteImage();//IF CLICKED

            selectedUser.setFirstname(nisitFirstnameTextField.getData());
            selectedUser.setLastname(nisitLastnameTextField.getData());
            selectedUser.setId(nisitIdTextField.getData());
            selectedUser.setEmail(nisitEmailTextField.getData());
            selectedUser.setAvatar(editorUploadImageStack.getCurFileName());

            if(!nisitPasswordTextField.getData().equalsIgnoreCase("PASSWORD")){
                selectedUser.setPassword(nisitPasswordTextField.getData());
            }


            datasource.writeData(users);
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
            if(!user.getAvatar().equalsIgnoreCase("no-image")){
                ImageDatasource imageDatasource = new ImageDatasource("users");
                imageDatasource.deleteFile(user.getAvatar());
            }
            users.deleteUserByObject(user);
            datasource.writeData(users);
            refreshTableData();
            selectedUserListener();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    private TableColumn<User,?> newAvatarImageColumn(){
        TableColumn<User,VBox> column = new TableColumn<>("");
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE
        column.setCellFactory(c -> new TableCell<>(){
            private SquareImage avatar;
            private HashMap<String,Image> imageCache = new HashMap<>();
            User user;
            @Override
            protected void updateItem(VBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    setAlignment(Pos.CENTER);
                    avatar = new SquareImage(new ImageView());
                    avatar.getImageView().setFitHeight(50);
                    avatar.getImageView().setFitWidth(50);
                    avatar.getImageView().setPreserveRatio(true);
                    avatar.getImageView().setSmooth(true);
                    avatar.setClipImage(50,50);
//                    setStyle("-fx-background-color: red");
                    setGraphic(avatar.getImageView());
                    Task<Image> loadImageTask = new Task<>() {
                        @Override
                        protected Image call() {
                            user = getTableView().getItems().get(getIndex());
                            if(user != null & !user.getAvatar().equalsIgnoreCase("no-image")){
                                if(imageCache.keySet().contains(user.getAvatar())){
                                    return imageCache.get(user.getAvatar());
                                }
                                ImageDatasource imageDatasource = new ImageDatasource("users");
                                imageCache.put(user.getAvatar(),imageDatasource.openImage(user.getAvatar()));
                                return imageCache.get(user.getAvatar());
                            }
                            return null;
                        }
                    };
                    loadImageTask.setOnSucceeded(event ->{
                        if(loadImageTask.getValue() != null){
                            avatar.setImage(loadImageTask.getValue());
                        }
                        setGraphic(avatar.getImageView());
                    });
                    new Thread(loadImageTask).start();

                }
            }
        });
        return column;
    }
    private TableColumn<User,?> newUsernameEmailColumn(String colName){
        TableColumn<User,VBox> column = new TableColumn<>(colName);
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE
        column.setCellFactory(c -> new TableCell<>(){
            private VBox vBox = new VBox();
            private Text line1 = new Text();
            private Text line2 = new Text();
            {
                vBox.setAlignment(Pos.CENTER);
            }
            @Override
            protected void updateItem(VBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());
                    String username = user.getUsername();
                    line1.setText((username.equalsIgnoreCase("no-username")
                            && user.getActiveStatus().equalsIgnoreCase("inactive")
                            ? "not register" : username));
                    line2.setText(user.getEmail());

                    vBox.getChildren().clear();
                    vBox.getChildren().addAll(line1, line2);

                    setGraphic(vBox);
                }
            }
        });
        return column;
    }
    private TableColumn<User,?> newStatusLatestColumn(String colName){
        TableColumn<User,VBox> column = new TableColumn<>(colName);
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE
        column.setCellFactory(c -> new TableCell<>(){
            private VBox vBox = new VBox();
            private DefaultLabel line1 = new DefaultLabel("");
            private Text line2 = new Text();
            {
                vBox.setAlignment(Pos.CENTER);
                line1.changeText("",18,FontWeight.NORMAL);
            }
            @Override
            protected void updateItem(VBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());
                    String status = user.getActiveStatus();
                    line1.changeText(status);
                    if(status.equalsIgnoreCase("active")){
                        line1.changeLabelColor("green");
                    }else {
                        line1.changeLabelColor("red");
                    }

                    line2.setText(DateTools.localDateTimeToFormatString("yyyy/MM/dd HH:mm", user.getLastLogin()));

                    vBox.getChildren().clear();
                    vBox.getChildren().addAll(line1, line2);

                    setGraphic(vBox);
                }
            }
        });
        return column;
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


