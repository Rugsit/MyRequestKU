package ku.cs.views.layouts.sidebar;

import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import ku.cs.models.Session;
import ku.cs.models.user.User;
import ku.cs.services.FXRouter;
import ku.cs.services.ImageDatasource;
import ku.cs.services.Observer;
import ku.cs.services.Theme;
import ku.cs.views.components.DefaultImage;
import ku.cs.views.components.DefaultLabel;
import ku.cs.cs211671project.MainApplication;

import java.io.IOException;
import java.util.HashMap;

public class SidebarController implements Observer<HashMap<String,String>> {
    private VBox vBox;
    private double width;
    private double height;
    private final String sidebarTopImagePath = "/images/logos/side-bar-ku-logo.png";
    private final String BASE_COLOR = "transparent";
    private final String HOVER_COLOR = "#EBEBEB";
    private final String BASE_LABEL_COLOR = DefaultLabel.DEFAULT_LABEL_COLOR;
    private Session session;
    private String curPage;
    private Theme theme = Theme.getInstance();;

    public SidebarController(String curPage, Session session){
        this.session = session;
        this.curPage = curPage;
        this.width = 260;
        this.height = MainApplication.windowHeight;//720
        initVBox();
        setMount(0,0);
        setupChildren();
        theme.addObserver(this);
    }
    private void initVBox(){
        vBox = new VBox();
        vBox.setPrefWidth(width);
        vBox.setPrefHeight(height);
    }
    public void setMount(double x, double y){
        vBox.setLayoutX(x);
        vBox.setLayoutY(y);
    }
    private void setupChildren(){
        Image sidebarImage = new Image(getClass().getResourceAsStream(sidebarTopImagePath));
        CenterImage centerImageLayout = new CenterImage(width,150,sidebarImage);
        centerImageLayout.setMount(0,0);

        NavList navListLayout = new NavList(width,310,this.session);
        String iconPath = "/images/pages/department/sidebar/";
        String requestIconPath = iconPath + "request-list.png";
        String nisitManageIconPath = iconPath + "nisit-management.png";
        String aprroverManageIconPath = iconPath + "approver-management.png";
        String nisitAdvisorManageIconPath = iconPath + "nisit-advisor-management.png";

        navListLayout.addRouteButton("ใบคำร้อง","department-staff-request-list",BASE_COLOR,HOVER_COLOR,BASE_LABEL_COLOR,requestIconPath);
        navListLayout.addRouteButton("จัดการนิสิต","department-staff-nisit-management",BASE_COLOR,HOVER_COLOR,BASE_LABEL_COLOR,nisitManageIconPath);
        navListLayout.addRouteButton("จัดการผู้อนุมัติ","department-staff-approver-list",BASE_COLOR,HOVER_COLOR,BASE_LABEL_COLOR,aprroverManageIconPath);
        navListLayout.addRouteButton("จัดการที่ปรึกษานิสิต","department-staff-nisit-advisor-management",BASE_COLOR,HOVER_COLOR,BASE_LABEL_COLOR,nisitAdvisorManageIconPath);
        navListLayout.setMount(0,150);

        String name = "Firstname Lastname";
        Image userImage = new Image(getClass().getResourceAsStream(DefaultImage.fallbackImagePath));
        if(session != null && session.getUser() != null){
            name = session.getUser().getName();
            if(!session.getUser().getAvatar().equalsIgnoreCase("no-image")){
                ImageDatasource imageDatasource = new ImageDatasource("users");
                userImage = imageDatasource.openImage(session.getUser().getAvatar());
            }
        }

        MiniProfile miniProfile = new MiniProfile(userImage,name){
            @Override
            protected void handleClickEvent(){
                profileImage.setOnMouseClicked(e->{
                    if(!curPage.equalsIgnoreCase("profile")){
                        try {
                            FXRouter.goTo("department-staff-profile",session);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                    }
                });
            }
        };
        miniProfile.mount(0,460);
        //The VBox bypass child mount location but still need
        vBox.getChildren().addAll(centerImageLayout.getVBox(),navListLayout.getVBox(),miniProfile.getVBox());

    }
    public VBox getVBox(){
        return vBox;
    }

    @Override
    public void update(HashMap<String, String> data) {
        vBox.setStyle(vBox.getStyle() + "-fx-background-color: " + data.get("primary")+";");
    }
}