package ku.cs.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.requests.ConfirmRequestFormController;
import ku.cs.controllers.requests.ErrorGeneralRequestFormController;
import ku.cs.models.request.Request;
import ku.cs.models.user.User;

import java.io.IOException;

public class ShowPopupRequest {

    static public void showConfirmPopup(BorderPane borderPane, Request request, Request requestPair,  User loginUser) {
        try {
            Stage currentConfirmStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ShowPopupRequest.class.getResource("/ku/cs/views/confirm-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            ConfirmRequestFormController controller = fxmlLoader.getController();
            controller.setStage(currentConfirmStage);
            controller.setBorderPane(borderPane);
            controller.setRequestForm(request);
            controller.setRequestPair(requestPair);
            controller.setLoginUser(loginUser);
            currentConfirmStage.setScene(scene);
            currentConfirmStage.initModality(Modality.APPLICATION_MODAL);
            currentConfirmStage.setTitle("ยืนยันการส่งคำร้อง");
            currentConfirmStage.setResizable(false);
            addImageToPopup(currentConfirmStage);
            currentConfirmStage.show();
        } catch (IOException ee) {
            System.err.println("Error: " + ee.getMessage());
        }
    }

    public static void showErrorPopup(String errorMessage) {
        try {
            Stage currentErrorStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ShowPopupRequest.class.getResource("/ku/cs/views/error-page.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ErrorGeneralRequestFormController errorController = fxmlLoader.getController();
            errorController.setErrorMessage(errorMessage);
            errorController.setStage(currentErrorStage);
            currentErrorStage.setScene(scene);
            currentErrorStage.initModality(Modality.APPLICATION_MODAL);
            currentErrorStage.setTitle("เกิดข้อผิดพลาด");
            currentErrorStage.setResizable(false);
            addImageToPopup(currentErrorStage);
            currentErrorStage.show();
        } catch (IOException ee) {
            System.err.println("Error: " + ee.getMessage());
        }
    }

    private static void addImageToPopup(Stage currentPopupStage) {
        Image logo16 = new Image(ShowPopupRequest.class.getResourceAsStream("/images/logos/application-logo16x16.png"));
        Image logo32 = new Image(ShowPopupRequest.class.getResourceAsStream("/images/logos/application-logo32x32.png"));
        Image logo48 = new Image(ShowPopupRequest.class.getResourceAsStream("/images/logos/application-logo48x48.png"));
        Image logo64 = new Image(ShowPopupRequest.class.getResourceAsStream("/images/logos/application-logo64x64.png"));
        Image logo128 = new Image(ShowPopupRequest.class.getResourceAsStream("/images/logos/application-logo128x128.png"));
        Image logo500 = new Image(ShowPopupRequest.class.getResourceAsStream("/images/logos/application-logo500x500.png"));
        currentPopupStage.getIcons().addAll(logo16, logo32, logo48, logo64, logo128, logo500);
    }
}
