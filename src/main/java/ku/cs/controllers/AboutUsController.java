package ku.cs.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import ku.cs.models.user.*;
import ku.cs.views.components.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static java.lang.Thread.sleep;

public class AboutUsController {
    @FXML private ImageView tanaananImageView;
    @FXML private ImageView rugsitImageView;
    @FXML private ImageView sirisukImageView;
    @FXML private ImageView narakornImageView;
    @FXML private Button backButton;
    @FXML private Label tipsLabel;

    private User loginUser;

    @FXML
    public void initialize() {
        showImage();
        RouteButton back = new RouteButton(backButton, "login", "transparent",
                               "#EBEBEB", "#FFFFFF");
        showTips();
    }

    private void showImage() {
        Image tanaanan = new Image(getClass().getResourceAsStream("/images/team-members/tanaanan.png"));
        Image rugsit = new Image(getClass().getResourceAsStream("/images/team-members/rugsit.png"));
        Image sirisuk = new Image(getClass().getResourceAsStream("/images/team-members/sirisuk.png"));
        Image narakorn = new Image(getClass().getResourceAsStream("/images/team-members/narakorn.png"));
        CircleImage tanaananImage = new CircleImage(tanaananImageView, tanaanan);
        CircleImage rugsitImage = new CircleImage(rugsitImageView, rugsit);
        CircleImage sirisukImage = new CircleImage(sirisukImageView, sirisuk);
        CircleImage narakornImage = new CircleImage(narakornImageView, narakorn);
    }

    private void showTips() {

        String tip1 = """
                ผู้ใช้สามารถกดเปลี่ยนภาพโปรไฟล์, email, และรหัสผ่านของตนเองได้ ที่หน้าโปรไฟล์การ์ดของตนเอง
                
                โดยสามารถเข้าถึงหน้าโปรไฟล์การ์ดได้โดยการกดที่รูปภาพโปรไฟล์ด้านซ้ายมือ เหนือชื่อ-นามสกุลของผู้ใช้
                """;

        String tip2 = """
                ผู้ใช้สามารถเปลี่ยน theme, ปรับขนาดตัวหนังสือ, หรือเปลี่ยน font ตัวหนังสือได้ 
                
                โดยกดไปที่ปุ่มรูปฟันเฟือง ที่เขียนว่า " ตั้งค่า " แล้วจะมีหน้าต่างเด้งขึ้นมาให้เลือกปรับได้
                
                กด Icon พระอาทิตย์ หรือพระจันทร์ เพื่อเปลี่ยน theme 
                
                กดเลือกขนาดตัวหนังสือกับ font จากตัวเลือกใต้ตัวหนังสือกำกับ
                """;

        ArrayList<String> tips = new ArrayList<>(Arrays.asList(tip1, tip2));

        if (loginUser instanceof FacultyUser && ! (loginUser instanceof Student) && ! (loginUser instanceof Advisor)) {
            String staffTip1 = """
                    เจ้าหน้าที่สามารถกดเพิ่มผู้อนุมัติที่จำเป็นต้องใช้บ่อย ๆ ไว้ก่อน และนำไปกดเพิ่มในใบคำร้องโดยไม่ต้องกรอกใหม่ได้ 
                    
                    เมื่อต้องการเพิ่มผู้อนุมัติไว้ล่วงหน้าสามารถไปที่หัวข้อ “ จัดการผู้อนุมัติ ” และกดเพิ่มผู้อนุมัติเอาไว้ 
                    
                    เมื่อต้องการจะเพิ่มผู้อนุมัติที่เคยเพิ่มไว้ ให้ไปที่คำร้องและกด “ เพิ่มผู้อนุมัติ ” 
                    
                    จากนั้นเลือก “ เพิ่มผู้อนุมัติจากรายชื่อ ” และกดที่ชื่อผู้อนุมัติที่ต้องการจะเพิ่ม
                    """;


            String staffTip2 = """
                    เจ้าหน้าที่จะต้องอัปโหลดไฟล์ PDF ซึ่งเป็นหลักฐานลายเซ็นของผู้อนุมัติก่อนจึงจะสามารถกดอนุมัติให้
                     
                    ผู้อนุมัติคนนั้น ๆ ได้ โดยกดที่ icon รูปสัญลักษณ์ถูกต้อง ของผู้อนุมัติคนนั้น ๆ จากนั้นสถานะของ
                    
                    ผู้อนุมัติคนนั้นจะเปลี่ยนไปเป็น " เรียบร้อย "
                    
                    เมื่อผู้อนุมัติทุกคนอนุมัติแล้ว และทุกคนขึ้นสถานะว่า " เรียบร้อย " เจ้าหน้าที่จึงจะสามารถกดอนุมัติคำร้องนั้นได้
                    """;
            tips.addAll(Arrays.asList(staffTip1, staffTip2));

        }

        if (loginUser instanceof DepartmentUser && ! (loginUser instanceof Student) && ! (loginUser instanceof Advisor)) {
            String departmentStaffTip1 = """
                    เมื่อผู้อนุมัติทุกคนอนุมัติแล้ว และสถานะของผู้อนุมัติทุกคนในระดับภาควิชาขึ้นว่า " เรียบร้อย "
                    
                    เจ้าหน้าที่ภาควิชาจะสามารถเลือกได้ว่า คำร้องนี้ควรจะสิ้นสุดที่ภาควิชา หรือต้องส่งคำร้องต่อให้คณะพิจารณา
                    
                    หากต้องการสิ้นสุดใบคำร้องที่ภาควิชา เจ้าหน้าที่สามารถกดได้ที่ปุ่ม " อนุมัติ "
                    
                    หากต้องการส่งให้คณะพิจารณา เจ้าหน้าที่จะต้องกดไปที่รายชื่อของผู้อนุมัติระดับคณะ แล้วกดปุ่มส่งหาคณะ
                    """;

            String departmentStaffTip2 = """
                    เจ้าหน้าที่ภาควิชาสามารถกดเพิ่มนิสิตหลาย ๆ คนพร้อมกันได้ ด้วยการอัปโหลดไฟล์ CSV ที่มีโครงสร้างต่อไปนี้
                    
                    รหัสนิสิต,ชื่อจริง,นามสกุล,อีเมล
                    """;

            tips.addAll(Arrays.asList(departmentStaffTip1, departmentStaffTip2));
        }


        Collections.shuffle(tips);
        tipsLabel.setText(tips.getFirst());
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
            int current = 1;
            public void handle(ActionEvent actionEvent) {
                tipsLabel.setText(tips.get(current));
                current++;
                if (current == tips.size()) {
                    current = 0;
                    Collections.shuffle(tips);
                }
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        tipsLabel.setWrapText(true);
    }

    public void setLoginUser(User user) {
        loginUser = user;
    }
}


