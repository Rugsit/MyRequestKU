package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class AdvisorRequestsController {
    @FXML TableView requestListTableView;
    @FXML
    BorderPane borderPane;

    public void initialize() {
        showTable();
    }

    private void showTable(){
        requestListTableView.getColumns().clear();
        TableColumn<String, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        TableColumn<String, String> dateColumn = new TableColumn<>("วันที่ยื่นคำร้อง");
        TableColumn<String, String> typeColumn = new TableColumn<>("ประเภทคำร้อง");
        TableColumn<String, String> statusColumn = new TableColumn<>("สถานะคำร้อง");
        requestListTableView.getColumns().add(nameColumn);
        requestListTableView.getColumns().add(dateColumn);
        requestListTableView.getColumns().add(typeColumn);
        requestListTableView.getColumns().add(statusColumn);
        nameColumn.setMinWidth(150);
        dateColumn.setMinWidth(200);
        typeColumn.setMinWidth(200);
        statusColumn.setMinWidth(381);
    }


    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

}
