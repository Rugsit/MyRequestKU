package ku.cs.controllers.student;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import ku.cs.controllers.requests.ChooseRequestFromController;

import java.io.IOException;


public class StudentRequestsController {
    @FXML Label requestsNumberLabel;
    @FXML Label approvedNumberLabel;
    @FXML Label rejectedNumberLabel;
    @FXML TableView requestListTableView;
    @FXML BorderPane borderPane;
    @FXML
    Button createRequestFormButton;

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
        requestsNumberLabel.setText("0");
        approvedNumberLabel.setText("0");
        rejectedNumberLabel.setText("0");
    }

    @FXML
    public void onCreateFromClick() {
        try {
            String viewPath = "/ku/cs/views/choose-request-form-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            ChooseRequestFromController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            borderPane.setCenter(pane);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

}
