package ku.cs.models;

import java.util.UUID;

public class GeneralRequestForm extends Request{
    // เก็บข้อมูลเบอร์โทร
    String tel;

    //เก็บข้อมูลเความประสงค์ในการขอใบแทนปริญญาบัตร
    boolean degreeCertificateLost;
    boolean degreeCertificateDamage;

    // เก็บข้อมูลความประสงค์ในการเปลี่ยนชื่อ
    String oldThaiName;
    String newThaiName;
    String oldEngName;
    String newEngName;

    // เก็บข้อมูลความประสงค์ในการเปลี่ยนชื่อสกุล
    String oldThaiSurName;
    String newThaiSurName;
    String oldEngSurName;
    String newEngSurName;

    // เก็บข้อมูล ความประสงค์อื่นๆ
    String others;

    public GeneralRequestForm(String[] data) {
        super.setUuid(UUID.fromString(data[1]));
        super.setOwnerUUID(UUID.fromString(data[2]));
        super.setTimeStamp(data[3]);
        super.setDate(data[4]);
        super.setStatusNow(data[5]);
        super.setStatusNext(data[6]);
        this.tel = data[7];
        this.degreeCertificateLost = Boolean.parseBoolean(data[8]);
        this.degreeCertificateDamage = Boolean.parseBoolean(data[9]);
        this.oldThaiName = data[10];
        this.newThaiName = data[11];
        this.oldEngName = data[12];
        this.newEngName = data[13];
        this.oldThaiSurName = data[14];
        this.newThaiSurName = data[15];
        this.oldEngSurName = data[16];
        this.newEngSurName = data[17];
        this.others = data[18];
    }

    public GeneralRequestForm() {
    }

    public void setTel(String tel) {
        if (tel == null || tel.isEmpty()) {
            throw new IllegalArgumentException("คุณไม่ได้กรอกเบอร์โทร");
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
        if (oldThaiName == null || oldThaiName.isEmpty()) {
            throw new IllegalArgumentException("คุณไม่ได้กรอกชื่อเก่าภาษาไทย");
        }
        this.oldThaiName = oldThaiName;
    }

    public void setNewThaiName(String newThaiName) {
        if (newThaiName == null || newThaiName.isEmpty()) {
            throw new IllegalArgumentException("คุณไม่ได้กรอกชื่อใหม่ภาษาไทย");
        }
        this.newThaiName = newThaiName;
    }

    public void setOldEngName(String oldEngName) {
        if (oldEngName == null || oldEngName.isEmpty()) {
            throw new IllegalArgumentException("คุณไม่ได้กรอกชื่อเก่าภาษาอังกฤษ");
        }
        this.oldEngName = oldEngName;
    }

    public void setNewEngName(String newEngName) {
        if (newEngName == null || newEngName.isEmpty()) {
            throw new IllegalArgumentException("คุณไม่ได้กรอกชื่อใหม่ภาษาอังกฤษ");
        }
        this.newEngName = newEngName;
    }

    public void setOldThaiSurName(String oldThaiSurName) {
        if (oldThaiSurName == null || oldThaiSurName.isEmpty()) {
            throw new IllegalArgumentException("คุณไม่ได้กรอกชื่อสกุลเก่าภาษาไทย");
        }
        this.oldThaiSurName = oldThaiSurName;
    }

    public void setNewThaiSurName(String newThaiSurName) {
        if (newThaiSurName == null || newThaiSurName.isEmpty()) {
            throw new IllegalArgumentException("คุณไม่ได้กรอกชื่อสกุลใหม่ภาษาไทย");
        }
        this.newThaiSurName = newThaiSurName;
    }

    public void setOldEngSurName(String oldEngSurName) {
        if (oldEngSurName == null || oldEngSurName.isEmpty()) {
            throw new IllegalArgumentException("คุณไม่ได้กรอกชื่อสกุลเก่าภาษาอังกฤษ");
        }
        this.oldEngSurName = oldEngSurName;
    }

    public void setNewEngSurName(String newEngSurName) {
        if (newEngSurName == null || newEngSurName.isEmpty()) {
            throw new IllegalArgumentException("คุณไม่ได้กรอกชื่อสกุลใหม่ภาษาอังกฤษ");
        }
        this.newEngSurName = newEngSurName;
    }

    public void setOthers(String others) {
        if (others == null || others.isEmpty()) {
            throw new IllegalArgumentException("คุณไม่ได้กรอกความประสงค์อื่นๆ");
        }
        this.others = others;
    }

    @Override
    public String toString() {
        return "General" + "," +
                super.getUuid().toString() + "," +
                super.getOwnerUUID().toString() + "," +
                super.getTimeStamp() + "," +
                super.getDate() + "," +
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
                others;
    }
}