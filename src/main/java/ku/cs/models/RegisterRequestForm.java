package ku.cs.models;

import java.time.LocalDate;

public class RegisterRequestForm {
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
        this.semester = "";
        this.latePaymentSemester = "";
        this.oldFaculty = "";
        this.newFaculty = "";
        this.since = "";
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
        if (newCreditInt <= 0 || newCreditInt > 100) {
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
}
