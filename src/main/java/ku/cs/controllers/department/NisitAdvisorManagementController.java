package ku.cs.controllers.department;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ku.cs.models.Session;
import ku.cs.models.user.*;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.*;
import ku.cs.services.Observer;
import ku.cs.services.Theme;
import ku.cs.views.components.*;

import java.util.*;

public class NisitAdvisorManagementController implements Observer<HashMap<String, String>> {
    @FXML private Label pageTitleLabel;

    @FXML private HBox nisitTableHeaderHBox;
    @FXML private HBox advisorTableHeaderHBox;

    @FXML private Label nisitTableViewLabel;
    @FXML private TableView<User> nisitTableView;
    @FXML private Label advisorTableViewLabel;
    @FXML private TableView<User> advisorTableView;

    @FXML private VBox showNisitVBox;
    @FXML private VBox showAdvisorVBox;

    @FXML private ImageView nisitImageView;
    @FXML private Label nisitNamePrefixLabel;
    @FXML private Label nisitNameLabel;
    @FXML private Label nisitIdPrefixLabel;
    @FXML private Label nisitIdLabel;
    @FXML private Label nisitAdvisorNamePrefixLabel;
    @FXML private Label nisitAdvisorNameLabel;
    @FXML private Label nisitAdvisorIdPrefixLabel;
    @FXML private Label nisitAdvisorIdLabel;

    @FXML private ImageView advisorImageView;
    @FXML private Label advisorNamePrefixLabel;
    @FXML private Label advisorNameLabel;
    @FXML private Label advisorIdPrefixLabel;
    @FXML private Label advisorIdLabel;

    @FXML private Button changeAdvisorButton;
    @FXML private Button backButton;
    @FXML private StackPane mainStackPane;

    private UserListFileDatasource nisitDatasource;
    private UserListFileDatasource advisorDatasource;
    private UserList nisitList;
    private UserList advisorList;
    private Student selectedStudent;
    private Advisor selectedAdvisor;
    private Session session;
    private Theme theme = Theme.getInstance();

    private DefaultSearchBox<User> nisitSearchBox;
    private DefaultSearchBox<User> advisorSearchBox;
    private UserList nisitFilterList;
    private UserList advisorFilterList;

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
        nisitDatasource = new UserListFileDatasource("data","student.csv");
        advisorDatasource = new UserListFileDatasource("data","advisor.csv");
        selectedStudent = null;
        selectedAdvisor = null;

        initNisitTableView();
        initAdvisorTableView();
        refreshNisitTable();
        refreshAdvisorTable();
        initNisitTableHeaderHBox();
        initAdvisorTableHeaderHBox();


        initShowVBox(showNisitVBox);
        initShowVBox(showAdvisorVBox);
        showNisitVBox.setVisible(false);
        showNisitVBox.setDisable(true);
        showAdvisorVBox.setVisible(false);
        showAdvisorVBox.setDisable(true);
        changeAdvisorButton.setDisable(true);
        showAdvisorVBox.setPadding(new Insets(20,0,0,0));

        initLabel();
        initButton();

        theme.addObserver(this);
        theme.notifyObservers(theme.getTheme());
    }
    private void initLabel(){
        new DefaultLabel(pageTitleLabel);
        new DefaultLabel(nisitTableViewLabel);
        new DefaultLabel(advisorTableViewLabel);

        new DefaultLabel(nisitNamePrefixLabel);
        new DefaultLabel(nisitNameLabel);
        new DefaultLabel(nisitIdPrefixLabel);
        new DefaultLabel(nisitIdLabel);
        new DefaultLabel(nisitAdvisorNamePrefixLabel);
        new DefaultLabel(nisitAdvisorNameLabel);
        new DefaultLabel(nisitAdvisorIdPrefixLabel);
        new DefaultLabel(nisitAdvisorIdLabel);

        new DefaultLabel(advisorNamePrefixLabel);
        new DefaultLabel(advisorNameLabel);
        new DefaultLabel(advisorIdPrefixLabel);
        new DefaultLabel(advisorIdLabel);

    }
    private void initButton(){
        new RouteButton(backButton,"department-staff-request-list","transparent","#a6a6a6","#000000");
        DefaultButton changeAdvisorBT = new DefaultButton(changeAdvisorButton,"#FF4E4E","#D03E3E","#FFFFFF"){
          @Override
          protected void handleClickEvent(){
              button.setOnMouseClicked(e -> {
                  mainStackPane.getChildren().add(new ConfirmStack("ยืนยัน","คุณต้องการเปลี่ยนอาจารย์ที่ปรึกษาใช่มั้ย"){
                      @Override
                      protected void handleAcceptButton(){
                          getAcceptButton().setOnMouseClicked(e -> {
                              mainStackPane.getChildren().removeLast();
                              onChangeAdvisor();
                          });
                      }
                      @Override
                      protected void handleDeclineButton(){
                          getDeclineButton().setOnMouseClicked(e -> {
                              mainStackPane.getChildren().removeLast();
                          });
                      }
                  });

              });
          }
        };
        changeAdvisorBT.changeBackgroundRadius(15);
    }
    private void initNisitTableView(){
        DefaultTableView<User> nisitTable = new DefaultTableView(nisitTableView){
            @Override
            protected void handleClick() {
                getTableView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
                    @Override
                    public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                        if (newValue != null) {
                            selectedStudent = (Student) newValue;
                        }
                        selectedStudentListener();
                    }
                });
            }
            @Override
            public void updateAction(){
                if(theme.getTheme() != null){
                    if(theme.getTheme().get("name").equalsIgnoreCase("dark")){
                        setStyleSheet("/ku/cs/styles/department/pages/nisit-advisor-management/dark-department-nisit-advisor-management-nisit-table-stylesheet.css");
                    }else{
                        setStyleSheet("/ku/cs/styles/department/pages/nisit-advisor-management/department-nisit-advisor-management-nisit-table-stylesheet.css");
                    }

                }
            }
        };
        nisitTable.getTableView().getColumns().clear();
        nisitTable.getTableView().getItems().clear();
        nisitTable.addColumn("รหัสนิสิต","id");
        nisitTable.addColumn("ชื่อ-นามสกุล","name");
        nisitTable.addColumn("อีเมล","email");
        nisitTable.addColumn("คณะ","faculty");
        nisitTable.addColumn("ภาควิชา","department");
        nisitTable.getTableView().getColumns().add(newStatusColumn("สถานะ"));
        nisitTable.getTableView().getColumns().add(newDeleteColumn());
        nisitTable.addStyleSheet("/ku/cs/styles/department/pages/nisit-advisor-management/department-nisit-advisor-management-nisit-table-stylesheet.css");

        theme.addObserver(nisitTable);
    }
    private void initAdvisorTableView(){
        DefaultTableView<User> nisitTable = new DefaultTableView(advisorTableView){
            @Override
            protected void handleClick() {
                getTableView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
                    @Override
                    public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                        if (newValue != null) {
                            selectedAdvisor = (Advisor) newValue;
                        }
                        selectedAdvisorListener();
                    }
                });
            }
            @Override
            public void updateAction(){
                if(theme.getTheme() != null){
                    if(theme.getTheme().get("name").equalsIgnoreCase("dark")){
                        setStyleSheet("/ku/cs/styles/department/pages/nisit-advisor-management/dark-department-nisit-advisor-management-advisor-table-stylesheet.css");
                    }else{
                        setStyleSheet("/ku/cs/styles/department/pages/nisit-advisor-management/department-nisit-advisor-management-advisor-table-stylesheet.css");
                    }

                }
            }
        };
        nisitTable.getTableView().getColumns().clear();
        nisitTable.getTableView().getItems().clear();
        nisitTable.addColumn("รหัสอาจารย์","id");
        nisitTable.addColumn("ชื่อ-นามสกุล","name");
        nisitTable.addColumn("อีเมล","email");
        nisitTable.addColumn("คณะ","faculty");
        nisitTable.addColumn("ภาควิชา","department");
        nisitTable.addStyleSheet("/ku/cs/styles/department/pages/nisit-advisor-management/department-nisit-advisor-management-advisor-table-stylesheet.css");

        theme.addObserver(nisitTable);
    }
    private void initNisitTableHeaderHBox(){
        Map<String,StringExtractor<User>> filterList= new LinkedHashMap<>();
        filterList.put("รหัสนิสิต", obj -> obj.getId());
        filterList.put("ชื่อ-นามสกุล", obj -> obj.getName());
        filterList.put("อีเมล", obj -> obj.getEmail());
        filterList.put("คณะ", obj -> ((DepartmentUser)obj).getFaculty());
        filterList.put("ภาควิชา", obj -> ((DepartmentUser)obj).getDepartment());
        filterList.put("ที่ปรึกษา", new StringExtractor<User>() {
            @Override
            public String extract(User obj) {
                Student student = (Student) obj;
                String status;
                if(student.getAdvisor() != null){
                    User cuurentAdvisor = advisorList.findUserByUUID(student.getAdvisor());
                    if(cuurentAdvisor != null){
                        status = "มีที่ปรึกษา";
                    }else{
                        status = "มีที่ปรึกษา(!)";
                    }
                }else{
                    status = "ไม่มีที่ปรึกษา";
                }

                return status;
            }
        });

        Map<String, Comparator<User>> comparatorList= new LinkedHashMap<>();


        nisitSearchBox = new DefaultSearchBox<>(new ArrayList<>(this.nisitFilterList.getUsers()), filterList,comparatorList,500,30){
            @Override
            protected void searchAction(){
                refreshNisitSearchTableData(getQueryItems());
            }
            @Override
            protected void initStyle(){
                super.initStyle();
                filterBox.changeFontSize(16);
                compareBox.changeFontSize(16);
                searchBox.setFontSize(16);
            }
        };
        nisitTableHeaderHBox.getChildren().add(nisitSearchBox);
    }
    private void initAdvisorTableHeaderHBox(){
        Map<String,StringExtractor<User>> filterList= new LinkedHashMap<>();
        filterList.put("รหัสอาจารย์", obj -> obj.getId());
        filterList.put("ชื่อ-นามสกุล", obj -> obj.getName());
        filterList.put("อีเมล", obj -> obj.getEmail());
        filterList.put("คณะ", obj -> ((DepartmentUser)obj).getFaculty());
        filterList.put("ภาควิชา", obj -> ((DepartmentUser)obj).getDepartment());

        Map<String, Comparator<User>> comparatorList= new LinkedHashMap<>();


        advisorSearchBox = new DefaultSearchBox<>(new ArrayList<>(this.advisorFilterList.getUsers()), filterList,comparatorList,500,30){
            @Override
            protected void searchAction(){
                refreshAdvisorSearchTableData(getQueryItems());
            }
            @Override
            protected void initStyle(){
                super.initStyle();
                filterBox.changeFontSize(16);
                compareBox.changeFontSize(16);
                searchBox.setFontSize(16);
            }
        };
        advisorTableHeaderHBox.getChildren().add(advisorSearchBox);
    }
    private void refreshNisitTable(){
        nisitTableView.getItems().clear();
        nisitList = nisitDatasource.readData();

        if(session != null && session.getUser() != null){
            nisitFilterList = new UserList();
            UUID currentDepartment = ((DepartmentUser)session.getUser()).getDepartmentUUID();
            for(User user : nisitList.getUsers("student")){
                if(((Student)user).getDepartmentUUID().equals(currentDepartment)){
                    try {
                        nisitFilterList.addUser(user);
                    } catch (UserException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            nisitFilterList = nisitList;
        }


        if(nisitSearchBox!=null){
            nisitSearchBox.setSearchItems(new ArrayList<>(nisitFilterList.getUsers()));
            nisitSearchBox.forceSearch();

        }else {
            for(User user : nisitFilterList.getUsers("student")){
                if(user.isRole("student")){
                    nisitTableView.getItems().add(user);
                }
            }
        }
    }
    private void refreshAdvisorTable(){
        advisorTableView.getItems().clear();
        advisorList = advisorDatasource.readData();
        if(session != null && session.getUser() != null){
            advisorFilterList = new UserList();
            UUID currentDepartment = ((DepartmentUser)session.getUser()).getDepartmentUUID();
            for(User user : advisorList.getUsers("advisor")){
                if(((Advisor)user).getDepartmentUUID().equals(currentDepartment)){
                    try {
                        advisorFilterList.addUser(user);
                    } catch (UserException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            advisorFilterList = advisorList;
        }


        if(advisorSearchBox!=null){
            advisorSearchBox.setSearchItems(new ArrayList<>(advisorFilterList.getUsers()));
            advisorSearchBox.forceSearch();

        }else {
            for(User user : advisorFilterList.getUsers("advisor")){
                if(user.isRole("advisor")){
                    advisorTableView.getItems().add(user);
                }
            }
        }
    }
    private void refreshNisitSearchTableData(Collection<? extends User> users){
        nisitTableView.getItems().clear();
        nisitTableView.getItems().addAll(users);
    }
    private void refreshAdvisorSearchTableData(Collection<? extends User> users){
        advisorTableView.getItems().clear();
        advisorTableView.getItems().addAll(users);
    }
    private void selectedStudentListener(){
        if(selectedStudent != null){
            changeShowNisitData();
            showNisitVBox.setVisible(true);
            showNisitVBox.setDisable(false);
        }else{
            showNisitVBox.setVisible(false);
            showNisitVBox.setDisable(true);
        }
        completeSelectedListener();
    }
    private void selectedAdvisorListener(){
        if(selectedAdvisor != null){
            changeShowAdvisorData();
            showAdvisorVBox.setVisible(true);
            showAdvisorVBox.setDisable(false);
        }else{
            showAdvisorVBox.setVisible(false);
            showAdvisorVBox.setDisable(true);
        }
        completeSelectedListener();
    }
    private void completeSelectedListener(){
        if(selectedStudent != null && selectedAdvisor != null){
            changeAdvisorButton.setDisable(false);
        }else{
            changeAdvisorButton.setDisable(true);
        }
    }
    private void changeShowNisitData(){
        nisitNameLabel.setText(selectedStudent.getName());
        nisitIdLabel.setText(selectedStudent.getId());
        UUID advisorUUID = selectedStudent.getAdvisor();
        if(advisorUUID != null){
            User currentAdvisor = advisorList.findUserByUUID(advisorUUID);
            if(currentAdvisor != null){
                nisitAdvisorNameLabel.setText(currentAdvisor.getName());
                nisitAdvisorIdLabel.setText(currentAdvisor.getId());
            }else{
                nisitAdvisorNameLabel.setText("ไม่พบอาจารย์ที่ปรึกษาในระบบ");
                nisitAdvisorIdLabel.setText("-");
            }
        }else{
            nisitAdvisorNameLabel.setText("ไม่มีอาจารย์ที่ปรึกษา");
            nisitAdvisorIdLabel.setText("-");
        }
        SquareImage nisitImage = new SquareImage(nisitImageView);
        nisitImage.setClipImage(50,50);
        if(!selectedStudent.getAvatar().equalsIgnoreCase("no-image")){
            ImageDatasource imageDatasource = new ImageDatasource("users");
            nisitImage.setImage(imageDatasource.openImage(selectedStudent.getAvatar()));
        }
    }
    private void changeShowAdvisorData(){
        advisorNameLabel.setText(selectedAdvisor.getName());
        advisorIdLabel.setText(selectedAdvisor.getId());
        SquareImage advisorImage = new SquareImage(advisorImageView);
        advisorImage.setClipImage(50,50);
        if(!selectedAdvisor.getAvatar().equalsIgnoreCase("no-image")){
            ImageDatasource imageDatasource = new ImageDatasource("users");
            advisorImage.setImage(imageDatasource.openImage(selectedAdvisor.getAvatar()));
        }
    }
    private void onChangeAdvisor(){
        try {
            selectedStudent.setAdvisor(selectedAdvisor);
            nisitDatasource.writeData(nisitList);
            nisitTableView.refresh();
            selectedStudentListener();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void onRemoveLinkAdvisor(Student student){
        try {
            student.setAdvisor("no-advisor");
            nisitDatasource.writeData(nisitList);
            nisitTableView.refresh();
            selectedStudentListener();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private TableColumn<User,?> newStatusColumn(String colName){
        TableColumn<User,Text> column = new TableColumn<>(colName);
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE
        column.setCellFactory(c -> new TableCell<>(){
            private DefaultLabel line1 = new DefaultLabel("");
            {
                line1.changeText("",20, FontWeight.NORMAL);
                if(theme.getTheme() != null){
                    line1.update(theme.getTheme());
                }
            }
            @Override
            protected void updateItem(Text item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null);
                } else {
                    Student student = (Student) getTableView().getItems().get(getIndex());
                    String status;
                    if(student.getAdvisor() != null){
                        User cuurentAdvisor = advisorList.findUserByUUID(student.getAdvisor());
                        if(cuurentAdvisor != null){
                            status = "มีที่ปรึกษา";
                        }else{
                            status = "มีที่ปรึกษา(!)";
                        }
                    }else{
                        status = "ไม่มีที่ปรึกษา";
                    }

                    line1.changeText(status);
                    setGraphic(line1);
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
                hbox.setPrefSize(25,25);
                DefaultButton b = new DefaultButton(actionButton,"transparent", "#e0e0e0", "#000000"){
                    @Override
                    protected void handleClickEvent() {
                        button.setOnMouseClicked(e -> {
                            User user = getTableView().getItems().get(getIndex());
                            mainStackPane.getChildren().add(new ConfirmStack("ยืนยัน","คุณต้องการลบอาจารย์ที่ปรึกษาใช่มั้ย"){
                                @Override
                                protected void handleAcceptButton(){
                                    getAcceptButton().setOnMouseClicked(e -> {
                                        mainStackPane.getChildren().removeLast();
                                        onRemoveLinkAdvisor((Student) user);
                                    });
                                }
                                @Override
                                protected void handleDeclineButton(){
                                    getDeclineButton().setOnMouseClicked(e -> {
                                        mainStackPane.getChildren().removeLast();
                                    });
                                }
                            });

                        });
                    }
                };
                Image deleteButtonImage = new Image(getClass().getResourceAsStream("/images/pages/department/global/red-bin.png"));

                b.changeBackgroundRadius(20);
                b.setImage(deleteButtonImage,25,25);
            }
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().get(getIndex()) == null) {
                    setGraphic(null); // No content for empty cells
                } else {
                    Student student = (Student) getTableView().getItems().get(getIndex());
                    if(student.getAdvisor() != null){
                        hbox.setDisable(false);
                    }else{
                        hbox.setDisable(true);
                    }
                    setGraphic(hbox); // Set the HBox with the button as the cell's graphic
                }
            }
        });
        return column;
    }
    private void initShowVBox(VBox vbox){
        vbox.getChildren().forEach(child ->{
            if(child instanceof HBox){
                HBox hbox = (HBox) child;
                if(hbox.getId() != null && hbox.getId().contains("NameHBox")){
                    VBox.setMargin(hbox,new Insets(10,0,0,20));
                }else{
                    //Insets top right bottom left
                    VBox.setMargin(hbox,new Insets(5,0,0,20));
                }
                hbox.setSpacing(10);
            }
        });
    }

    @Override
    public void update(HashMap<String, String> data) {
        mainStackPane.setStyle(mainStackPane.getStyle()+"-fx-background-color: " + data.get("secondary") + ";");
    }
}
