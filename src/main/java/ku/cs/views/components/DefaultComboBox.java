package ku.cs.views.components;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

public abstract class DefaultComboBox<T> extends ComboBox<T> {
    protected ComboBox<T> comboBox;
    protected final String DEFAULT_FONT;
    protected final String FALLBACK_FONT;
    protected double fontSize = 24;
    protected StringExtractor<T> extractor;

    public DefaultComboBox(){
        this.comboBox = this;
        this.DEFAULT_FONT = DefaultLabel.DEFAULT_FONT;
        this.FALLBACK_FONT = DefaultLabel.FALLBACK_FONT;
        initialize();
    }
    public DefaultComboBox(ComboBox<T> comboBox) {
        this.comboBox = comboBox;
        this.DEFAULT_FONT = DefaultLabel.DEFAULT_FONT;
        this.FALLBACK_FONT = DefaultLabel.FALLBACK_FONT;
        initialize();
    }

    protected void setStringExtractor() {
        this.extractor = new StringExtractor<T>() {
            @Override
            public String extract(T obj) {
                return obj.toString();
            }
        };
    }
    protected void setComboBoxStyle(){
        String style = "-fx-font-size: " + fontSize + ";" +
                "-fx-font-family:" + DEFAULT_FONT +";" +
                "-fx-text-fill: black;" +
                "-fx-background-color: white;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 1;";
        comboBox.setStyle(style);
    }

    protected void setCellFactoryStyle(ListCell<T> cell){
        cell.setStyle(
                "-fx-text-fill: black; " +
                "-fx-background-color: white; " +
//                "-fx-background-radius: 10; " +
                "-fx-border-color: transparent; " +
                "-fx-border-radius: 10; " +
                "-fx-border-width: 0;"
        );
        cell.setOnMouseEntered(e -> cell.setStyle(
                cell.getStyle() +
                "-fx-background-color: #009dff;"
        ));
        cell.setOnMouseExited(e -> cell.setStyle(
                cell.getStyle() +
                "-fx-background-color: white;"
        ));
    }

    private void initialize(){
        setStringExtractor();
        setComboBoxStyle();
        comboBox.setPlaceholder(new Text("ไม่มีข้อมูล"));
        //FOR SUB CELLS
        comboBox.setCellFactory(listView -> new ListCell<>(){
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(extractor.extract(item));
                    setCellFactoryStyle(this);
                }
            }
        });
        //FOR MAIN BUTTON CELL
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(extractor.extract(item));
                }
            }
        });
//        comboBox.setEditable(true);
    }
    public void changeBackgroundRadius(int radius){
        comboBox.setStyle(comboBox.getStyle()+
                "-fx-background-radius: " + radius + ";"+
                "-fx-border-radius: " + radius + ";");
    }
    public void changeFontSize(double fontSize){
        comboBox.setStyle(comboBox.getStyle()+"-fx-font-size: " + fontSize + ";");

    }
    public ComboBox<T> getComboBox() {
        return this.comboBox;
    }
}
