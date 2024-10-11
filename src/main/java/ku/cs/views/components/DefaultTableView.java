package ku.cs.views.components;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ku.cs.services.Observer;
import ku.cs.services.Theme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultTableView<S> extends TableView implements Observer<HashMap<String,String>> {
    private TableView<S> tableView;
    private final String DEFAULT_FONT = DefaultLabel.DEFAULT_FONT;
    private final String STYLE_PATH = "/ku/cs/styles/department/pages/department-table-stylesheet.css";
    private String currentStylePath = STYLE_PATH;
    private Theme theme = Theme.getInstance();

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
                "-fx-font-family: " + DEFAULT_FONT  + ";"
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
        currentStylePath = path;
    }

    public TableView<S> getTableView() {
        return tableView;
    }

    //FOR THEME UPDATE ONLY
    public void setStyleSheetAndScaleFont(String path){
        tableView.getStylesheets().clear();
        try {
            tableView.getStylesheets().clear();
            String css = loadModifyCSS(path);
            String tempCss = "data:text/css," + css;
            tableView.getStylesheets().add(tempCss);
        } catch (IOException e) {
            setStyleSheet(currentStylePath);
            throw new RuntimeException(e);
        }
    }

    private String loadModifyCSS(String path) throws IOException {
        InputStream stream = getClass().getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String content = "";
        String line;

        //Regex find -fx-font-size: xx;
        Pattern pattern = Pattern.compile("-fx-font-size:\\s*(\\d+(\\.\\d+)?)\\s*;");
        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                double curSize = Double.parseDouble(matcher.group(1));
                double newSize = theme.getCalculatedFontSize(curSize);
                line = line.replaceFirst("-fx-font-size:\\s*\\d+(\\.\\d+)?;", String.format("-fx-font-size: %.2f;", newSize));
            }
            content += line + "\n";
        }

        reader.close();
        return content;
    }

    @Override
    public void update(HashMap<String, String> data) {
//        for (TableColumn<?, ?> column : tableView.getColumns()) {
//            column.setStyle(column.getStyle() + "-fx-text-fill: " + data.get("textColor") + ";");
//        }
        updateAction();
        setStyleSheetAndScaleFont(currentStylePath);
        tableView.setStyle(tableView.getStyle()+"-fx-font-family: " + data.get("textFont")  + ";");

        tableView.refresh();

    }

    public void updateAction() {
        System.out.println("update tableView action");
    }
}
