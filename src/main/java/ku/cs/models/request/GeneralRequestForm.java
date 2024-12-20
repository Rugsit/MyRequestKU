package ku.cs.models.request;

import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralRequestForm extends Request{
    // เก็บข้อมูลเบอร์โทร
    private String tel;

    //เก็บข้อมูลเความประสงค์ในการขอใบแทนปริญญาบัตร
    private boolean degreeCertificateLost;
    private boolean degreeCertificateDamage;

    // เก็บข้อมูลความประสงค์ในการเปลี่ยนชื่อ
    private String oldThaiName;
    private String newThaiName;
    private String oldEngName;
    private String newEngName;

    // เก็บข้อมูลความประสงค์ในการเปลี่ยนชื่อสกุล
    private String oldThaiSurName;
    private String newThaiSurName;
    private String oldEngSurName;
    private String newEngSurName;

    // เก็บข้อมูล ความประสงค์อื่นๆ
    private String others;

    public GeneralRequestForm(String[] data) {
        super(data[1], data[2], data[3], data[4], data[5], data[6], data[0], data[7], data[8], data[21]);
        this.tel = data[9];
        this.degreeCertificateLost = Boolean.parseBoolean(data[10]);
        this.degreeCertificateDamage = Boolean.parseBoolean(data[11]);
        this.oldThaiName = data[12];
        this.newThaiName = data[13];
        this.oldEngName = data[14];
        this.newEngName = data[15];
        this.oldThaiSurName = data[16];
        this.newThaiSurName = data[17];
        this.oldEngSurName = data[18];
        this.newEngSurName = data[19];
        this.others = data[20];
    }

    public GeneralRequestForm(UUID ownerUUID, String name, String nisitId, String requestType) {
        super(ownerUUID, name, nisitId, requestType);
    }

    public String getTel() {
        return tel;
    }

    public boolean isDegreeCertificateLost() {
        return degreeCertificateLost;
    }

    public boolean isDegreeCertificateDamage() {
        return degreeCertificateDamage;
    }

    public String getOldThaiName() {
        return oldThaiName;
    }

    public String getNewThaiName() {
        return newThaiName;
    }

    public String getOldEngName() {
        return oldEngName;
    }

    public String getNewEngName() {
        return newEngName;
    }

    public String getOldThaiSurName() {
        return oldThaiSurName;
    }

    public String getNewThaiSurName() {
        return newThaiSurName;
    }

    public String getOldEngSurName() {
        return oldEngSurName;
    }

    public String getNewEngSurName() {
        return newEngSurName;
    }

    public String getOthers() {
        return others;
    }

    public void setTel(String tel) {
        tel = tel.trim();
        Pattern pattern = Pattern.compile("^[0-9]{10}$");
        Matcher matcher = pattern.matcher(tel);
        if (!matcher.find()) {
            throw new IllegalArgumentException("กรุณากรอกเบอร์โทรศัพท์ให้ถูกต้อง");
        }
        this.tel = tel;
    }

    public void setDegreeCertificateLost(boolean degreeCertificateLost) {
        this.degreeCertificateLost = degreeCertificateLost;
    }

    public void setDegreeCertificateDamage(boolean degreeCertificateDamage) {
        this.degreeCertificateDamage = degreeCertificateDamage;
    }

    public void setOldThaiName(String oldThaiName) {
        oldThaiName = oldThaiName.trim();
        if (oldThaiName.isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกชื่อเก่าภาษาไทย");
        }
        this.oldThaiName = oldThaiName;
    }

    public void setNewThaiName(String newThaiName) {
        newThaiName = newThaiName.trim();
        if (newThaiName.isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกชื่อใหม่ภาษาไทย");
        }
        this.newThaiName = newThaiName;
    }

    public void setOldEngName(String oldEngName) {
        oldEngName = oldEngName.trim();
        if (oldEngName.isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกชื่อเก่าภาษาอังกฤษ");
        }
        this.oldEngName = oldEngName;
    }

    public void setNewEngName(String newEngName) {
        newEngName = newEngName.trim();
        if (newEngName.isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกชื่อใหม่ภาษาอังกฤษ");
        }
        this.newEngName = newEngName;
    }

    public void setOldThaiSurName(String oldThaiSurName) {
        oldThaiSurName = oldThaiSurName.trim();
        if (oldThaiSurName.isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกชื่อสกุลเก่าภาษาไทย");
        }
        this.oldThaiSurName = oldThaiSurName;
    }

    public void setNewThaiSurName(String newThaiSurName) {
        newThaiSurName = newThaiSurName.trim();
        if (newThaiSurName.isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกชื่อสกุลใหม่ภาษาไทย");
        }
        this.newThaiSurName = newThaiSurName;
    }

    public void setOldEngSurName(String oldEngSurName) {
        oldEngSurName = oldEngSurName.trim();
        if (oldEngSurName.isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกชื่อสกุลเก่าภาษาอังกฤษ");
        }
        this.oldEngSurName = oldEngSurName;
    }

    public void setNewEngSurName(String newEngSurName) {
        newEngSurName = newEngSurName.trim();
        if (newEngSurName.isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกชื่อสกุลใหม่ภาษาอังกฤษ");
        }
        this.newEngSurName = newEngSurName;
    }

    public void setOthers(String others) {
        others = others.trim();
        if (others.isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกความประสงค์อื่นๆ");
        }
        this.others = others;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");
        String timestamp = super.getTimeStamp().format(formatter);
        String date = super.getDate().format(formatter);
        return  super.getRequestType() + "," +
                super.getUuid().toString() + "," +
                super.getOwnerUUID().toString() + "," +
                super.getName() + "," +
                super.getNisitId() + "," +
                timestamp + "," +
                date + "," +
                super.getStatusNow() + "," +
                super.getStatusNext() + "," +
                tel + "," +
                degreeCertificateLost + "," +
                degreeCertificateDamage + "," +
                oldThaiName + "," +
                newThaiName + "," +
                oldEngName + "," +
                newEngName + "," +
                oldThaiSurName + "," +
                newThaiSurName + "," +
                oldEngSurName + "," +
                newEngSurName + "," +
                others + "," +
                super.getReasonForNotApprove();
    }
}