package ku.cs.controllers.advisor;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class AdvisorStudentListController {
    @FXML TableView requestListTableView;
    @FXML
    BorderPane borderPane;

    public void initialize() {
        showTable();
    }

    private void showTable(){
        requestListTableView.getColumns().clear();
        TableColumn<String, String> nameColumn = new TableColumn<>("ชื่อ-นามสกุล");
        TableColumn<String, String> idColumn = new TableColumn<>("รหัสนิสิต");
        requestListTableView.getColumns().add(nameColumn);
        requestListTableView.getColumns().add(idColumn);
        nameColumn.setMinWidth(531);
        idColumn.setMinWidth(400);
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

}
