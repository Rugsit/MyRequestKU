package ku.cs.controllers.requests;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ku.cs.controllers.student.StudentRequestsController;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.services.Datasource;
import ku.cs.services.RequestListFileDatasource;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ConfirmRequestFormController {
    private Datasource<RequestList> datasource;
    private Stage stage;
    private User loginUser;

    private Request request;
    private Request requestPair;

    @FXML
    public BorderPane borderPane;

    @FXML
    private void initialize() {
        datasource = new RequestListFileDatasource("data");
    }

    @FXML
    public void onEditClick() {
        stage.close();
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void setRequestForm(Request request) {
        this.request = request;
    }

    public void setRequestPair(Request request) {
        this.requestPair = request;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLoginUser(User loginUser) { this.loginUser = loginUser;}

    @FXML
    public void onConfirmClick() {
        RequestList requestList = datasource.readData();
        try {
            String viewPath = "/ku/cs/views/student-requests-pane.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(viewPath));
            Pane pane = fxmlLoader.load();
            StudentRequestsController controller = fxmlLoader.getController();
            controller.setBorderPane(this.borderPane);
            controller.setLoginUser((Student) loginUser);

            if (this.request != null) {
                requestList.addRequest(request);
            }
            if (this.requestPair != null) {
                requestList.addRequest(requestPair);
            }
            datasource.writeData(requestList);
            controller.showTable();
            borderPane.setCenter(pane);
            stage.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
