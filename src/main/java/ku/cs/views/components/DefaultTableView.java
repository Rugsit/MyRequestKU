package ku.cs.views.components;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.services.Observer;

import java.util.HashMap;

public class DefaultTableView<S> extends TableView implements Observer<HashMap<String,String>> {
    private TableView<S> tableView;
    private final String DEFAULT_FONT = DefaultLabel.DEFAULT_FONT;
    private final String STYLE_PATH = "/ku/cs/styles/department/pages/department-table-stylesheet.css";

    public DefaultTableView() {
        this(null);
        this.tableView = this;

    }
    public DefaultTableView(TableView<S> tableView) {
        this.tableView = tableView;
        initTableStyle();
        handleClick();
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
        column.setSortable(false);//BLOCK SORT BY CLICK
        column.setReorderable(false);//BLOCK DRAG BY MOUSE
        column.setCellValueFactory(new PropertyValueFactory<>(objectProperty));
        tableView.getColumns().add(column);
    }
    protected void handleClick(){
        tableView.setOnMouseClicked(event -> {
            S selected = tableView.getSelectionModel().getSelectedItem();
            if(selected != null){
                System.out.println("Table clicked!");
            }
        });
    }
    public void addStyleSheet(String path){
        tableView.getStylesheets().add(getClass().getResource(path).toExternalForm());
    }
    public void setStyleSheet(String path){
        tableView.getStylesheets().clear();
        tableView.getStylesheets().add(getClass().getResource(path).toExternalForm());
    }

    public TableView<S> getTableView() {
        return tableView;
    }

    @Override
    public void update(HashMap<String, String> data) {
        for (TableColumn<?, ?> column : tableView.getColumns()) {
            column.setStyle(column.getStyle() + "-fx-text-fill: " + data.get("textColor") + ";");
        }
        updateAction();
        tableView.refresh();

    }

    public void updateAction() {
    }
}
