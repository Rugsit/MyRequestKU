package ku.cs.services;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import ku.cs.models.request.Request;

import java.util.regex.Pattern;

public class RequestStatusColumn {
    public static void setTableStatus(TableColumn<Request, String> status, String whichStatus) {
        status.setMaxWidth(250);
        status.setSortable(false);
        status.setCellFactory(c -> new TableCell<Request, String>() {
            Request request;
            Button statusCell = new Button();
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                statusCell.setMouseTransparent(true);
                statusCell.setFocusTraversable(false);
                String buttonStyle = "-fx-border-radius: 3;"+
                        "-fx-text-alignment: center;" +
                        "-fx-cursor: none;" +
                        "-fx-pref-width: 234;" +
                        "-fx-pref-height: 35;";
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    request = getTableView().getItems().get(getIndex());
                    if (whichStatus.equals("now")) {
                        statusCell.setText(request.getStatusNow());
                    } else if (whichStatus.equals("next")) {
                        statusCell.setText(request.getStatusNext());
                    }
                    setGraphic(statusCell);
                    statusCell.setStyle("-fx-background-color: #F2FFF7;" +
                            "-fx-border-color: #00B448;" +
                            "-fx-text-fill: #00B448;" +
                            buttonStyle);
                    Pattern rejected = Pattern.compile(".*ปฏิเสธ.*");
                    Pattern newlyCreated = Pattern.compile(".*ใหม่.*");
                    Pattern inProgress = Pattern.compile(".*ต่อ.*");
                    if (newlyCreated.matcher(statusCell.getText()).matches()){
                        statusCell.setStyle(
                                "-fx-background-color: #EBEEFF; " +
                                        "-fx-border-color: #4E7FFF; " +
                                        "-fx-text-fill: #4E7FFF;"+
                                        buttonStyle
                        );
                    }
                    if (rejected.matcher(statusCell.getText()).matches()) {
                        statusCell.setStyle(
                                "-fx-background-color: #FFDEDE; " +
                                        "-fx-border-color: #FE6463; " +
                                        "-fx-text-fill: #FE6463;" +
                                        buttonStyle
                        );
                    }
                    if (inProgress.matcher(statusCell.getText()).matches()){
                        statusCell.setStyle(
                                "-fx-background-color: #FFF6E8; " +
                                        "-fx-border-color: #ED9B22; " +
                                        "-fx-text-fill: #ED9B22;" +
                                        buttonStyle
                        );

                    }

                    if (rejected.matcher(statusCell.getText()).matches()) {
                        statusCell.setStyle(
                                "-fx-background-color: #FFDEDE; " +
                                        "-fx-border-color: #FE6463; " +
                                        "-fx-text-fill: #FE6463;" +
                                        buttonStyle
                        );
                    }
                    statusCell.getStyleClass().add(".semi-medium-font-size");
                }
            }
        });
    }
}
