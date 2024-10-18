package ku.cs.models.request;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AcademicLeaveRequestForm extends Request{
    // เบอร์โทร ที่อยู่ เหตุผลที่ลา
    private String tel;
    private String address;
    private String reason;

    // จำนวนภาคการศึกษาที่ขอลา
    private int amountSemester;

    // ภาคการศึกษาและปีการศึกษาที่เริ่ม จนสิ้นสุดการลา
    private String semesterFrom;
    private String yearFrom;
    private String semesterTo;
    private String yearTo;

    // เคยลงทะเบียนเรียนไว้หรือไม่ ถ้าลงไว้ ลงไว้ภาคการศึกษาอะไร ปีอะไร วิชาอะไรบ้าง
    private boolean haveRegister;
    private String haveRegisterSemester;
    private String haveRegisterYear;
    private ArrayList<String> subject;

    public AcademicLeaveRequestForm(UUID ownerUUID, String name, String nisitId, String requestType) {
        super(ownerUUID, name, nisitId, requestType);
        subject = new ArrayList<>();
    }

    public AcademicLeaveRequestForm(String[] data, String[] subject) {
        super(data[1], data[2], data[3], data[4], data[5], data[6], data[0], data[7], data[8], data[20]);
        this.subject = new ArrayList<>();
        this.tel = data[9];
        this.address = data[10];
        this.reason = data[11];
        this.amountSemester = Integer.parseInt(data[12]);
        this.semesterFrom = data[13];
        this.yearFrom = data[14];
        this.semesterTo = data[15];
        this.yearTo = data[16];
        this.haveRegister = Boolean.parseBoolean(data[17]);
        this.haveRegisterSemester = data[18];
        this.haveRegisterYear = data[19];
        super.setReasonForNotApprove(data[20]);
        for (int i = 0; i < subject.length; i++) {
            for (int j = 0; j < 2; j++, i++) {
                 this.subject.add(subject[i]);
            }
            i--;
        }
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    public String getReason() {
        return reason;
    }

    public int getAmountSemester() {
        return amountSemester;
    }

    public String getSemesterFrom() {
        return semesterFrom;
    }

    public String getYearFrom() {
        return yearFrom;
    }

    public String getSemesterTo() {
        return semesterTo;
    }

    public String getYearTo() {
        return yearTo;
    }

    public boolean isHaveRegister() {
        return haveRegister;
    }

    public String getHaveRegisterSemester() {
        return haveRegisterSemester;
    }

    public String getHaveRegisterYear() {
        return haveRegisterYear;
    }

    public ArrayList<String> getSubject() {
        return subject;
    }

    public void setTel(String tel) {
        tel = tel.trim();
        Pattern pattern = Pattern.compile("^[0-9]{10}$");
        Matcher matcher = pattern.matcher(tel);
        if (!matcher.find()) {
            throw new IllegalArgumentException("กรุณากรอกเบอร์โทรให้ถูกต้อง");
        }
        this.tel = tel;
    }

    public void setAddress(String address) {
        address = address.trim();
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกที่อยู่ปัจจุบัน");
        }
        this.address = address;
    }

    public void setReason(String reason) {
        reason = reason.trim();
        if (reason.trim().isEmpty()) {
            throw new IllegalArgumentException("กรุณากรอกสาเหตุที่ลา");
        }
        this.reason = reason;
    }

    public void setAmountSemester(String amountSemester) {
        amountSemester = amountSemester.trim();
        int amount;
        try {
           amount = Integer.parseInt(amountSemester);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("กรุณากรอกจำนวนภาคการศึกษาให้ถูกต้อง");
        }
        if (amount < 0){
            throw new IllegalArgumentException("กรุณากรอกจำนวนภาคการศึกษามากกว่า 0");
        }
        this.amountSemester = amount;
    }

    public void setSemesterFrom(String semesterFrom) {
        semesterFrom = semesterFrom.trim();
        this.semesterFrom = semesterFrom;
    }

    public void setYearFrom(String yearFrom) {
        yearFrom = yearFrom.trim();
        Pattern pattern = Pattern.compile("^2[0-9]{3}$");
        Matcher matcher = pattern.matcher(yearFrom);
        if (!matcher.find()) {
            throw new IllegalArgumentException("กรุณากรอกปีการศึกษาให้ถูกต้อง");
        }
        this.yearFrom = yearFrom;
    }

    public void setSemesterTo(String semesterTo) {
        this.semesterTo = semesterTo;
    }

    public void setYearTo(String yearTo) {
        yearTo = yearTo.trim();
        Pattern pattern = Pattern.compile("^2[0-9]{3}$");
        Matcher matcher = pattern.matcher(yearTo);
        if (!matcher.find()) {
            throw new IllegalArgumentException("กรุณากรอกปีการศึกษาให้ถูกต้อง");
        }
        this.yearTo = yearTo;
    }

    public void setHaveRegister(boolean haveRegister) {
        this.haveRegister = haveRegister;
    }

    public void setHaveRegisterSemester(String haveRegisterSemester) {
        this.haveRegisterSemester = haveRegisterSemester;
    }

    public void setGetHaveRegisterYear(String getHaveRegisterYear) {
        getHaveRegisterYear = getHaveRegisterYear.trim();
        Pattern pattern = Pattern.compile("^2[0-9]{3}$");
        Matcher matcher = pattern.matcher(getHaveRegisterYear);
        if (!matcher.find()) {
            throw new IllegalArgumentException("กรุณากรอกปีการศึกษาให้ถูกต้อง");
        }
        this.haveRegisterYear = getHaveRegisterYear;
    }

    public void addSubject(ArrayList<String> subjectId, ArrayList<String> teacher) {
        for (int i = 0; i < subjectId.size(); i++) {
            if (subjectId.get(i).isEmpty()) {
                throw new IllegalArgumentException("กรุณากรอกรหัสวิชาให้ถูกต้องในวิชาที่ " + (i + 1));
            }
            subject.add(subjectId.get(i));
            Pattern pattern = Pattern.compile("^[ก-๙a-zA-Z]+[ \t]+[ก-๙a-zA-Z]+$");
            Matcher matcher = pattern.matcher(teacher.get(i).trim());
            if (teacher.get(i).isEmpty() || !matcher.find()) {
                throw new IllegalArgumentException("กรุณากรอกชื่ออาจารย์ประจำวิชาให้ถูกต้อง ในวิชาที่ " + (i + 1));
            }
            subject.add(teacher.get(i));
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");
        String timestamp = super.getTimeStamp().format(formatter);
        String date = super.getDate().format(formatter);
        String text = super.getRequestType() + "," +
                super.getUuid().toString() + "," +
                super.getOwnerUUID().toString() + "," +
                super.getName() + "," +
                super.getNisitId() + "," +
                timestamp + "," +
                date + "," +
                super.getStatusNow() + "," +
                super.getStatusNext() + "," +
                this.tel + "," +
                this.address + "," +
                this.reason + "," +
                this.amountSemester + "," +
                this.semesterFrom + "," +
                this.yearFrom + "," +
                this.semesterTo + "," +
                this.yearTo + "," +
                this.haveRegister + "," +
                this.haveRegisterSemester + "," +
                this.haveRegisterYear + "," +
                super.getReasonForNotApprove();
        if (haveRegister) {
            for (String eachSubject : subject) {
                text += "," + eachSubject;
            }
        }
        return text;
    }
}
