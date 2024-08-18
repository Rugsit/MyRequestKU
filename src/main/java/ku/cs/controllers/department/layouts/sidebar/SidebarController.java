package ku.cs.controllers.department.layouts.sidebar;

import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import ku.cs.controllers.department.components.DefaultLabel;
import ku.cs.cs211671project.MainApplication;

public class SidebarController {
    private VBox vBox;
    private double width;
    private double height;
    private final String sidebarTopImagePath = "/images/logos/side-bar-ku-logo.png";
    private final String BASE_COLOR = "transparent";
    private final String HOVER_COLOR = "#EBEBEB";
    private final String BASE_LABEL_COLOR = DefaultLabel.DEFAULT_LABEL_COLOR;

    public SidebarController(){
        this.width = 260;
        this.height = MainApplication.windowHeight;//720
        initVBox();
        setMount(0,0);
        setupChildren();
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

        NavList navListLayout = new NavList(width,310);
        String iconPath = "/images/pages/department/sidebar/";
        String requestIconPath = iconPath + "request-list.png";
        String nisitManageIconPath = iconPath + "nisit-management.png";
        String aprroverManageIconPath = iconPath + "approver-management.png";
        String nisitAdvisorManageIconPath = iconPath + "nisit-advisor-management.png";

        navListLayout.addRouteButton("ใบคำร้อง","department-staff-request-list",HOVER_COLOR,HOVER_COLOR,BASE_LABEL_COLOR,requestIconPath);
        navListLayout.addRouteButton("จัดการนิสิต","department-staff-nisit-management",BASE_COLOR,HOVER_COLOR,BASE_LABEL_COLOR,nisitManageIconPath);
        navListLayout.addRouteButton("จัดการผู้อนุมัติ","department-staff-approver-list",BASE_COLOR,HOVER_COLOR,BASE_LABEL_COLOR,aprroverManageIconPath);
        navListLayout.addRouteButton("จัดการที่ปรึกษานิสิต","department-staff-nisit-advisor-management",BASE_COLOR,HOVER_COLOR,BASE_LABEL_COLOR,nisitAdvisorManageIconPath);
        navListLayout.setMount(0,150);

        Image userImage = new Image(getClass().getResourceAsStream("/images/profile-test.png"));
        MiniProfile miniProfile = new MiniProfile(userImage,"Sirisuk Tharntham");
        miniProfile.mount(0,460);
        //The VBox bypass child mount location but still need
        vBox.getChildren().addAll(centerImageLayout.getVBox(),navListLayout.getVBox(),miniProfile.getVBox());

    }
    public VBox getVBox(){
        return vBox;
    }

}
