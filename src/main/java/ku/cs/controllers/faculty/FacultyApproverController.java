package ku.cs.controllers.faculty;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ku.cs.controllers.ChangePasswordController;
import ku.cs.controllers.advisor.AdvisorStudentRequestsController;
import ku.cs.models.request.Request;
import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.Student;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.FXRouter;
import ku.cs.views.components.CropImage;
import ku.cs.views.components.DefaultButton;
import ku.cs.views.components.DefaultLabel;
import ku.cs.views.components.RouteButton;

import java.io.IOException;
import java.time.LocalDateTime;

public class FacultyApproverController {
    @FXML
    private Label pageTitleLabel;

    @FXML
    private HBox tableHeaderHBox;
    @FXML
    private Label tableViewLabel;
    @FXML
    private Button addApproverButton;
    @FXML
    private TableView approverTableView;

    @FXML
    private VBox imageEditorVBox;
    @FXML
    private ImageView approverImageView;
    @FXML
    private Label approverNameLabel;
    @FXML
    private Label approverPositionLabel;

    @FXML
    private HBox uploadHBox;
    @FXML
    private ImageView iconPdfImageView;
    @FXML
    private Label fileNameLabel;
    @FXML
    private Button removeFileButton;
    @FXML
    private ImageView iconRemoveFileImageView;

    @FXML
    private Button uploadFileButton;

    @FXML
    private Button backButton;
    @FXML
    private Stage currentPopupStage;

    private FacultyUser loginUser;

    @FXML
    public void initialize() {
        loadApprover();
        showTable();
        approverEditPopUp();
        initLabel();
        initButton();
        initTableHeaderHBox();
        initImageEditorVBox();
        loginUser = (FacultyUser) FXRouter.getData();
        new CropImage(approverImageView).setClipImage(50, 50);
    }

    private void initLabel() {
        new DefaultLabel(pageTitleLabel);
        new DefaultLabel(tableViewLabel);
        new DefaultLabel(approverNameLabel);
        new DefaultLabel(approverPositionLabel);
        new DefaultLabel(fileNameLabel);
    }

    private void initButton() {
        new RouteButton(backButton, "faculty-page", "transparent", "#a6a6a6", "#000000");
        new DefaultButton(addApproverButton, "#FFE0A4", "#a6a6a6", "#000000").changeBackgroundRadius(15);
        new DefaultButton(removeFileButton, "transparent", "#a6a6a6", "#000000");
        new DefaultButton(uploadFileButton, "#ABFFA4", "#a6a6a6", "#000000").changeBackgroundRadius(15);
    }

    private void initTableHeaderHBox() {
        tableHeaderHBox.setSpacing(20);
    }

    private void initImageEditorVBox() {
//        imageEditorVBox.setStyle("-fx-spacing: 5px;");
//        imageEditorVBox.setPadding(new Insets(0,0,0,0));
        imageEditorVBox.setSpacing(5);
        approverNameLabel.setPadding(new Insets(15, 0, 0, 0));
    }

    private void showTable() {
        approverTableView.getColumns().clear();
        Label placeHolder = new Label("ไม่พบข้อมูล");
        approverTableView.setPlaceholder(placeHolder);
        approverTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Approver, String> nameColumn = new TableColumn<>("ชื่อ");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Firstname"));

        TableColumn<Approver, String> lastnameColumn = new TableColumn<>("นามสกุล");
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("Lastname"));

        TableColumn<Approver, String> tierColumn = new TableColumn<>("ตำแหน่ง");
        tierColumn.setCellValueFactory(new PropertyValueFactory<>("role"));


        approverTableView.getColumns().addAll(nameColumn, lastnameColumn, tierColumn);

    }
    private void approverEditPopUp() {
        approverTableView.setOnMouseClicked(e -> {
            Object selected = approverTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String popUpPath = "/ku/cs/views/faculty-edit-approver-pane.fxml";
                try {
                    if (currentPopupStage == null || !currentPopupStage.isShowing()) {
                        currentPopupStage = new Stage();
                    }

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(popUpPath));
                    Pane editPane = fxmlLoader.load();

                    Approver selectedApprover = (Approver) selected;
                    EditApproverController controller = fxmlLoader.getController();
                    controller.setApprover(selectedApprover);
                    controller.setStage(currentPopupStage);
                    controller.setApproverDetail(
                            selectedApprover.getFirstname(),
                            selectedApprover.getLastname(),
                            selectedApprover.getRole()
                    );

                    Scene scene = new Scene(editPane);
                    currentPopupStage.setScene(scene);
                    currentPopupStage.setTitle("Edit Approver");
                    currentPopupStage.initModality(Modality.APPLICATION_MODAL);

                    currentPopupStage.setOnHidden(event -> {
                        loadApprover();
                        approverTableView.refresh();
                    });

                    currentPopupStage.show();

                } catch (IOException ex) {
                    System.err.println("Error loading popup: " + ex.getMessage());
                }
            }
        });
    }

    private void loadApprover() {
        ApproverListFileDatasource approverListFileDatasource = new ApproverListFileDatasource("approver");
        String tier = "faculty";
        ApproverList approverList = approverListFileDatasource.query(tier, null);

        approverTableView.getItems().setAll(approverList.getApprovers());
    }

    @FXML
    private void onAddApproverButtonClicked() {
        try {
            if (currentPopupStage == null || !currentPopupStage.isShowing()) {
                currentPopupStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/views/faculty-add-approver-pane.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                AddApproverController controller = fxmlLoader.getController();
                controller.setLoginUser(loginUser);
                controller.setStage(currentPopupStage);

                currentPopupStage.setScene(scene);
                currentPopupStage.initModality(Modality.APPLICATION_MODAL);
                currentPopupStage.setTitle("Add approver");

                currentPopupStage.setOnHidden(event -> {
                    loadApprover();
                    approverTableView.refresh();
                });

                currentPopupStage.show();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

//
//
//
//    public static void main(String[] args) {
//        // Instantiate the datasource for the approver list
//        ApproverListFileDatasource approverListFileDatasource = new ApproverListFileDatasource("approver");
//
//        // Query approvers based on the "tier" value (e.g., faculty)
//        String tier = "faculty";
//
//        // If you're querying just based on the tier without a specific request, pass null for the request
//        ApproverList approverList = approverListFileDatasource.query(tier, null);
//
//        // Print out the approvers in the list
//        for (Approver approver : approverList.getApprovers()) {
//            //System.out.println(approver.toString());
//            System.out.println(approver.getName() + approver.getLastname() + approver.getRole());
//        }
//
//
//        System.out.println("Size of approver list: " + approverList.getApprovers().size());
//
//    }


}