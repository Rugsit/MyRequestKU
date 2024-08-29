package ku.cs.models;

public class GeneralRequestForm {
    String tel;
    boolean degreeCertificateLost;
    boolean degreeCertificateDamage;
    String oldThaiName;
    String newThaiName;
    String oldEngName;
    String newEngName;
    String oldThaiSurName;
    String newThaiSurName;
    String oldEngSurName;
    String newEngSurName;
    String others;

    public GeneralRequestForm() {
        this.degreeCertificateLost = false;
        this.degreeCertificateDamage = false;
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
            throw new IllegalArgumentException("tel is null or empty");
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
            throw new IllegalArgumentException("oldThaiName is null or empty");
        }
        this.oldThaiName = oldThaiName;
    }

    public void setNewThaiName(String newThaiName) {
        if (newThaiName == null || newThaiName.isEmpty()) {
            throw new IllegalArgumentException("newThaiName is null or empty");
        }
        this.newThaiName = newThaiName;
    }

    public void setOldEngName(String oldEngName) {
        if (oldEngName == null || oldEngName.isEmpty()) {
            throw new IllegalArgumentException("oldEngName is null or empty");
        }
        this.oldEngName = oldEngName;
    }

    public void setNewEngName(String newEngName) {
        if (newEngName == null || newEngName.isEmpty()) {
            throw new IllegalArgumentException("newEngName is null or empty");
        }
        this.newEngName = newEngName;
    }

    public void setOldThaiSurName(String oldThaiSurName) {
        if (oldThaiSurName == null || oldThaiSurName.isEmpty()) {
            throw new IllegalArgumentException("oldThaiSurName is null or empty");
        }
        this.oldThaiSurName = oldThaiSurName;
    }

    public void setNewThaiSurName(String newThaiSurName) {
        if (newThaiSurName == null || newThaiSurName.isEmpty()) {
            throw new IllegalArgumentException("newThaiSurName is null or empty");
        }
        this.newThaiSurName = newThaiSurName;
    }

    public void setOldEngSurName(String oldEngSurName) {
        if (oldEngSurName == null || oldEngSurName.isEmpty()) {
            throw new IllegalArgumentException("oldEngSurName is null or empty");
        }
        this.oldEngSurName = oldEngSurName;
    }

    public void setNewEngSurName(String newEngSurName) {
        if (newEngSurName == null || newEngSurName.isEmpty()) {
            throw new IllegalArgumentException("newEngSurName is null or empty");
        }
        this.newEngSurName = newEngSurName;
    }

    public void setOthers(String others) {
        if (others == null || others.isEmpty()) {
            throw new IllegalArgumentException("others is null or empty");
        }
        this.others = others;
    }
}
