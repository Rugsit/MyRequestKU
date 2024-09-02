package ku.cs.models;

import java.time.LocalDate;
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
    public RegisterRequestForm() {
        super();
    }

    public RegisterRequestForm(String[] data) {
        super.setUuid(UUID.fromString(data[1]));
        super.setOwnerUUID(UUID.fromString(data[2]));
        super.setTimeStamp(data[3]);
        super.setDate(data[4]);
        super.setStatusNow(data[5]);
        super.setStatusNext(data[6]);
        this.lateRegister = Boolean.parseBoolean(data[7]);
        this.addDrop = Boolean.parseBoolean(data[8]);
        this.registerMoreThan22 = Boolean.parseBoolean(data[9]);
        this.semester = data[10];
        this.semesterYear = Integer.parseInt(data[11]);
        this.oldCredit = Integer.parseInt(data[12]);
        this.newCredit = Integer.parseInt(data[13]);
        this.registerLessThan9 = Boolean.parseBoolean(data[14]);
        this.latePayment = Boolean.parseBoolean(data[15]);
        this.latePaymentSemester = data[16];
        this.latePaymentYear = Integer.parseInt(data[17]);
        this.transferFaculty = Boolean.parseBoolean(data[18]);
        this.oldFaculty = data[19];
        this.newFaculty = data[20];
        this.since = data[21];
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
        if (semester == null || semester.isEmpty()) {
            throw new IllegalArgumentException("คุณไม่ได้บอกภาคการศึกษา");
        }
        this.semester = semester;
    }

    public void setSemesterYear(String semesterYear) {
        LocalDate currentDate = LocalDate.now();
        int semesterYearInt;
        try {
            semesterYearInt = Integer.parseInt(semesterYear);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("คุณต้องกรอกปีการศึกษา และต้องเป็นตัวเลขเท่านั้น");
        }
        if (semesterYearInt <= 0 || semesterYearInt > currentDate.getYear() + 543) {
            System.out.println(currentDate.getYear());
            throw new IllegalArgumentException("ปีการศึกษาจะต้องมากกว่า 0 และไม่มากกว่าปีปัจจุบัน");
        }
        this.semesterYear = semesterYearInt;
    }

    public void setOldCredit(String oldCredit) {
        int oldCreditInt;
        try {
            oldCreditInt = Integer.parseInt(oldCredit);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("คุณต้องกรอกหน่วยกิตเก่า และต้องเป็นตัวเลข");
        }
        if (oldCreditInt <= 0) {
            throw new IllegalArgumentException("หน่วยกิตเก่าจะต้องมากกว่า 0");
        }
        this.oldCredit = oldCreditInt;
    }

    public void setNewCredit(String newCredit) {
        int newCreditInt;
        try {
            newCreditInt = Integer.parseInt(newCredit);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("คุณต้องกรอกหน่วยกิตใหม่ และต้องเป็นตัวเลข");
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
        if (latePaymentSemester == null || latePaymentSemester.isEmpty()) {
            throw new IllegalArgumentException("คุณไม่ได้บอกภาคการศึกษา");
        }
        this.latePaymentSemester = latePaymentSemester;
    }

    public void setLatePaymentYear(String latePaymentYear) {
        int latePaymentYearInt;
        try {
            latePaymentYearInt = Integer.parseInt(latePaymentYear);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("คุณต้องกรอกปีการศึกษาในการผ่อนผันค่าธรรมเนียม และต้องเป็นตัวเลข");
        }
        LocalDate currentDate = LocalDate.now();
        if (latePaymentYearInt <= 0 || latePaymentYearInt > currentDate.getYear() + 543) {
            throw new IllegalArgumentException("ปีการศึกษาในการผ่อนผันค่าธรรมเนียมต้องมีค่ามากกว่า 0");
        }
        this.latePaymentYear = latePaymentYearInt;
    }

    public void setTransferFaculty(boolean transferFaculty) {
        this.transferFaculty = transferFaculty;
    }

    public void setOldFaculty(String oldFaculty) {
        if (oldFaculty == null || oldFaculty.isEmpty()) {
            throw new IllegalArgumentException("คุณต้องกรอกคณะหรือสาขาวิชาเก่า");
        }
        this.oldFaculty = oldFaculty;
    }

    public void setNewFaculty(String newFaculty) {
        if (newFaculty == null || newFaculty.isEmpty()) {
            throw new IllegalArgumentException("คุณต้องกรอกคณะหรือสาขาวิชาใหม่");
        }
        this.newFaculty = newFaculty;
    }

    public void setSince(String since) {
        if (since == null || since.isEmpty()) {
            throw new IllegalArgumentException("คุณต้องกรอกเหตุผล");
        }
        this.since = since;
    }

    @Override
    public String toString() {
        return "Register" + "," +
                super.getUuid().toString() + "," +
                super.getOwnerUUID().toString() + "," +
                super.getTimeStamp() + "," +
                super.getDate() + "," +
                super.getStatusNow() + "," +
                super.getStatusNext() + "," +
                lateRegister + "," +
                addDrop + "," +
                registerMoreThan22 + "," +
                semester + "," +
                semesterYear + "," +
                oldCredit + "," +
                newCredit + "," +
                registerLessThan9 + "," +
                latePayment + "," +
                latePaymentSemester + "," +
                latePaymentYear + "," +
                transferFaculty + "," +
                oldFaculty + "," +
                newFaculty + "," +
                since;

    }
}
