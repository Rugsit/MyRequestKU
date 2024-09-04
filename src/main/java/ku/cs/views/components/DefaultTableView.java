package ku.cs.views.components;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class DefaultTableView<S> extends TableView {
    private TableView<S> tableView;
    private final String DEFAULT_FONT = DefaultLabel.DEFAULT_FONT;
    private final String STYLE_PATH = "/ku/cs/styles/department/pages/request-list/department-staff-request-list-table-stylesheet.css";

    public DefaultTableView() {
        this(null);
        this.tableView = this;

    }
    public DefaultTableView(TableView<S> tableView) {
        this.tableView = tableView;
        initTableStyle();
        handleCLick();
        tableView.getSelectionModel().clearSelection();
    }
    private void initTableStyle(){
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setStyle(
                "-fx-font-family: " + DEFAULT_FONT + ";"
        );
        tableView.getStylesheets().add(getClass().getResource(STYLE_PATH).toExternalForm());

    }
    public void addColumn(String label, String objectProperty) {
        TableColumn<S, String> column = new TableColumn<>(label);
        column.setCellValueFactory(new PropertyValueFactory<>(objectProperty));
        tableView.getColumns().add(column);
    }
    protected void handleCLick(){
        tableView.setOnMouseClicked(event -> {
            S selected = tableView.getSelectionModel().getSelectedItem();
            if(selected != null){
                System.out.println("Table clicked!");
            }
        });
    }

    public TableView<S> getTableView() {
        return tableView;
    }
}
