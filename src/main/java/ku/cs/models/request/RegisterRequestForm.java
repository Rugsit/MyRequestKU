package ku.cs.models.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class RegisterRequestForm extends Request{
    // ประสงค์ลงทะเบียนล่าช้า
    boolean lateRegister;

    // ประสงค์เพิ่มถอน
    boolean addDrop;

    // ประสงค์ลงทะเบียนมากกว่า 22 หน่วยกิต
    boolean registerMoreThan22;
    String semester;
    int semesterYear;
    int oldCredit;
    int newCredit;

    // ประสงค์ลงทะเบียนต่ำกว่า 9 หน่วยกิต
    boolean registerLessThan9;

    // ประสงค์ผ่อนผันค่าธรรมเนียมการศึกษา
    boolean latePayment;
    String latePaymentSemester;
    int latePaymentYear;

    // ประสงค์ย้ายคณะหรือสาขาวิชา
    boolean transferFaculty;
    String oldFaculty;
    String newFaculty;

    // เหตุผล
    String since;
    public RegisterRequestForm(UUID uuid, UUID ownerUUID, String name, String nisitId, LocalDateTime timeStampLastUpdate,
                                LocalDateTime timeStampCreateForm, String requestType, String statusNow, String statusNext) {
        super(uuid, ownerUUID, name, nisitId, timeStampLastUpdate, timeStampCreateForm, requestType, statusNow, statusNext);
    }

    public RegisterRequestForm(String[] data) {
        super.setRequestType(data[0]);
        super.setUuid(UUID.fromString(data[1]));
        super.setOwnerUUID(UUID.fromString(data[2]));
        super.setName(data[3]);
        super.setNisitId(data[4]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");
        super.setTimeStamp(LocalDateTime.parse(data[5], formatter));
        super.setDate(LocalDateTime.parse(data[6], formatter));
        super.setStatusNow(data[7]);
        super.setStatusNext(data[8]);
        this.lateRegister = Boolean.parseBoolean(data[9]);
        this.addDrop = Boolean.parseBoolean(data[10]);
        this.registerMoreThan22 = Boolean.parseBoolean(data[11]);
        this.semester = data[12];
        this.semesterYear = Integer.parseInt(data[13]);
        this.oldCredit = Integer.parseInt(data[14]);
        this.newCredit = Integer.parseInt(data[15]);
        this.registerLessThan9 = Boolean.parseBoolean(data[16]);
        this.latePayment = Boolean.parseBoolean(data[17]);
        this.latePaymentSemester = data[18];
        this.latePaymentYear = Integer.parseInt(data[19]);
        this.transferFaculty = Boolean.parseBoolean(data[20]);
        this.oldFaculty = data[21];
        this.newFaculty = data[22];
        this.since = data[23];
    }

    public void setLateRegister(boolean lateRegister) {
        this.lateRegister = lateRegister;
    }

    public void setAddDrop(boolean addDrop) {
        this.addDrop = addDrop;
    }

    public void setRegisterMoreThan22(boolean registerMoreThan22) {
        this.registerMoreThan22 = registerMoreThan22;
    }

    public void setSemester(String semester) {
        semester = semester.trim();
        if (semester.isEmpty()) {
            throw new IllegalArgumentException("กรุณาบอกภาคการศึกษา");
        }
        this.semester = semester;
    }

    public void setSemesterYear(String semesterYear) {
        semesterYear = semesterYear.trim();
        LocalDate currentDate = LocalDate.now();
        int semesterYearInt;
        try {
            semesterYearInt = Integer.parseInt(semesterYear);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("กรุณากรอกปีการศึกษา และต้องเป็นตัวเลขเท่านั้น");
        }
        if (semesterYearInt <= 0 || semesterYearInt > currentDate.getYear() + 543) {
            System.out.println(currentDate.getYear());
            throw new IllegalArgumentException("ปีการศึกษาจะต้องมากกว่า 0 และไม่มากกว่าปีปัจจุบัน");
        }
        this.semesterYear = semesterYearInt;
    }

    public void setOldCredit(String oldCredit) {
        oldCredit = oldCredit.trim();
        int oldCreditInt;
        try {
            oldCreditInt = Integer.parseInt(oldCredit);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("กรุณากรอกหน่วยกิตเก่า และต้องเป็นตัวเลข");
        }
        if (oldCreditInt <= 0) {
            throw new IllegalArgumentException("หน่วยกิตเก่าจะต้องมากกว่า 0");
        }
        this.oldCredit = oldCreditInt;
    }

    public void setNewCredit(String newCredit) {
        newCredit = newCredit.trim();
        int newCreditInt;
        try {
            newCreditInt = Integer.parseInt(newCredit);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("กรุณากรอกหน่วยกิตใหม่ และต้องเป็นตัวเลข");
        }
        if (newCreditInt <= 0 ) {
            throw new IllegalArgumentException("หน่วยกิตใหม่จะต้องมากกว่า 0");
        }
        this.newCredit = newCreditInt;
    }

    public void setRegisterLessThan9(boolean registerLessThan9) {
        this.registerLessThan9 = registerLessThan9;
    }

    public void setLatePayment(boolean latePayment) {
        this.latePayment = latePayment;
    }

    public void setLatePaymentSemester(String latePaymentSemester) {
        latePaymentSemester = latePaymentSemester.trim();
        if (latePaymentSemester.isEmpty()) {
            throw new IllegalArgumentException("กรุณาบอกภาคการศึกษา");
        }
        this.latePaymentSemester = latePaymentSemester;
    }

    public void setLatePaymentYear(String latePaymentYear) {
        latePaymentYear = latePaymentYear.trim();
        int latePaymentYearInt;
        try {
            latePaymentYearInt = Integer.parseInt(latePaymentYear);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("กรุณากรอกปีการศึกษาในการผ่อนผันค่าธรรมเนียม และต้องเป็นตัวเลข");
        }
        LocalDate currentDate = LocalDate.now();
        if (latePaymentYearInt <= 0 || latePaymentYearInt > currentDate.getYear() + 543) {
            throw new IllegalArgumentException("ปีการศึกษาในการผ่อนผันค่าธรรมเนียมต้องมีค่ามากกว่า 0 และไม่เกินปีการศึกษาปัจจุบัน");
        }
        this.latePaymentYear = latePaymentYearInt;
    }

    public void setTransferFaculty(boolean transferFaculty) {
        this.transferFaculty = transferFaculty;
    }

    public void setOldFaculty(String oldFaculty) {
        oldFaculty = oldFaculty.trim();
        if (oldFaculty.isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกคณะหรือสาขาวิชาเก่า");
        }
        this.oldFaculty = oldFaculty;
    }

    public void setNewFaculty(String newFaculty) {
        newFaculty = newFaculty.trim();
        if (newFaculty.isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกคณะหรือสาขาวิชาใหม่");
        }
        this.newFaculty = newFaculty;
    }

    public void setSince(String since) {
        this.since = since.trim();
        if (since.isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกเหตุผล");
        }
        this.since = since;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");
        String timestamp = super.getTimeStamp().format(formatter);
        String date = super.getDate().format(formatter);
        return  "\"" + super.getRequestType() + "\",\"" +
                super.getUuid().toString() + "\",\"" +
                super.getOwnerUUID().toString() + "\",\"" +
                super.getName() + "\",\"" +
                super.getNisitId() + "\",\"" +
                timestamp + "\",\"" +
                date + "\",\"" +
                super.getStatusNow() + "\",\"" +
                super.getStatusNext() + "\",\"" +
                lateRegister + "\",\"" +
                addDrop + "\",\"" +
                registerMoreThan22 + "\",\"" +
                semester + "\",\"" +
                semesterYear + "\",\"" +
                oldCredit + "\",\"" +
                newCredit + "\",\"" +
                registerLessThan9 + "\",\"" +
                latePayment + "\",\"" +
                latePaymentSemester + "\",\"" +
                latePaymentYear + "\",\"" +
                transferFaculty + "\",\"" +
                oldFaculty + "\",\"" +
                newFaculty + "\",\"" +
                since + "\"";

    }
}
