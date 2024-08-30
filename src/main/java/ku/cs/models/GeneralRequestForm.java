package ku.cs.models;

public class GeneralRequestForm {
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

    public GeneralRequestForm() {
        this.oldThaiName = "";
        this.newThaiName = "";
        this.oldEngName = "";
        this.newEngName = "";
        this.oldThaiSurName = "";
        this.newThaiSurName = "";
        this.oldEngSurName = "";
        this.newEngSurName = "";
        this.others = "";
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
}
