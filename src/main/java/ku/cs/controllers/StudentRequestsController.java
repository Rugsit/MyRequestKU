package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.services.FXRouter;

import java.io.IOException;


public class StudentRequestsController {
    @FXML Circle tabProfilePicCircle;
    @FXML Label tabAccountNameLabel;
    @FXML Label requestsNumberLabel;
    @FXML Label approvedNumberLabel;
    @FXML Label rejectedNumberLabel;
    @FXML TableView requestListTableView;


    // TODO: fetch data from datasource instead
    public void initialize() {
        showInfo();
        showTable();
    }

    private void showTable(){
        requestListTableView.getColumns().clear();
        TableColumn<String, String> typeColumn = new TableColumn<>("ประเภทคำร้อง");
        TableColumn<String, String> createColumn = new TableColumn<>("วันที่ยื่นคำร้อง");
        TableColumn<String, String> latestColumn = new TableColumn<>("วันที่อัพเดทล่าสุด");
        TableColumn<String, String> statusColumn = new TableColumn<>("สถานะคำร้อง");
        requestListTableView.getColumns().add(typeColumn);
        requestListTableView.getColumns().add(createColumn);
        requestListTableView.getColumns().add(latestColumn);
        requestListTableView.getColumns().add(statusColumn);
        typeColumn.setMinWidth(150);
        createColumn.setMinWidth(200);
        latestColumn.setMinWidth(200);
        statusColumn.setMinWidth(381);
    }

    // TODO: fetch data from datasource instead
    private void showInfo(){
        Image profilePic = new Image(getClass().getResourceAsStream("/images/users/side-bar-profile.png"));
        tabProfilePicCircle.setFill(new ImagePattern(profilePic));
        requestsNumberLabel.setText("0");
        approvedNumberLabel.setText("0");
        rejectedNumberLabel.setText("0");
        tabAccountNameLabel.setText("นายเมมโมรี่โฟม รักนอน");
    }

    @FXML
    public void onSideProfileClicked(){
        try{
            FXRouter.goTo("student-profile");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
