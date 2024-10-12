package ku.cs.models.request;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ku1AndKu3RequestForm extends Request{
    private String curriculum;
    private String tel;
    private String semester;
    private String year;
    private String campus;
    private ArrayList<ArrayList<String>> subjectList;

    public Ku1AndKu3RequestForm(UUID ownerUUID, String name, String nisitId, String requestType) {
        super(ownerUUID, name, nisitId, requestType);
        subjectList = new ArrayList<>();
    }

    public Ku1AndKu3RequestForm(String[]data, String[]subject, byte type) {
        super(data[1], data[2], data[3], data[4], data[5], data[6], data[0], data[7], data[8], data[14]);
        this.subjectList = new ArrayList<>();
        curriculum = data[9];
        tel = data[10];
        semester = data[11];
        year = data[12];
        campus = data[13];
        if (type == 1) {
            for (int i = 0; i < subject.length; i++) {
                ArrayList<String> newEachSubjectList = new ArrayList<>();
                for (int j = 0; j < 7; j++, i++) {
                    newEachSubjectList.add(subject[i]);
                }
                i--;
                subjectList.add(newEachSubjectList);
            }
        } else if (type == 3) {
            for (int i = 0; i < subject.length; i++) {
                ArrayList<String> newEachSubjectList = new ArrayList<>();
                for (int j = 0; j < 8; j++, i++) {
                    newEachSubjectList.add(subject[i]);
                }
                i--;
                subjectList.add(newEachSubjectList);
            }
        }
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public void setTel(String tel) {
        tel = tel.strip();
        Pattern pattern = Pattern.compile("^[0-9]{10}$");
        Matcher matcher = pattern.matcher(tel);
        if (!matcher.find()) {
            throw new IllegalArgumentException("กรุณากรอกเบอร์โทรให้ถูกต้อง");
        }
        this.tel = tel;
    }

    public void setYear(String year) {
        year = year.trim();
        Pattern pattern = Pattern.compile("^2[0-9]{3}$");
        Matcher matcher = pattern.matcher(year);
        if (!matcher.find()) {
            throw new IllegalArgumentException("กรุณากรอกปีการศึกษาให้ถูกต้อง");
        }
        this.year = year;
    }

    public void setCampus(String campus) {
        campus = campus.trim();
        if (campus.isBlank()) {
            throw new IllegalArgumentException("กรุณากรอกวิทยาเขตให้ถูกต้อง");
        }
        this.campus = campus;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public String getTel() {
        return tel;
    }

    public String getSemester() {
        return semester;
    }

    public String getYear() {
        return year;
    }

    public String getCampus() {
        return campus;
    }

    public ArrayList<ArrayList<String>> getSubjectList() {
        return subjectList;
    }

    public void addSubject(ArrayList<String> subject, ArrayList<String> idSubject,
                            ArrayList<String> registerType, ArrayList<String> credit,
                            ArrayList<String> section, ArrayList<String> sectionType,
                           ArrayList<String> teacher) {
        for(int i = 0; i < subject.size(); i++) {
            ArrayList<String> eachSuject = new ArrayList<>();
            if (subject.get(i) == null || subject.get(i).isEmpty()) {
                throw new IllegalArgumentException("กรุณากรอกชื่อวิชาในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(subject.get(i).trim());
            }
            if (idSubject.get(i) == null || idSubject.get(i).isEmpty()) {
                throw new IllegalArgumentException("กรุณากรอกรหัสวิชาในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(idSubject.get(i).trim());
            }
            if (registerType.get(i) == null || registerType.get(i).isEmpty()) {
                throw new IllegalArgumentException("กรุณาเลือกประเภทลงทะเบียนในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(registerType.get(i).trim());
            }
            try {
                Integer.parseInt(credit.get(i).trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("กรุณากรอกหน่วยกิตของวิชา และต้องเป็นตัวเลขในวิชาที่ " + (i + 1));
            }
            if (Integer.parseInt(credit.get(i).trim()) <= 0) {
                throw new IllegalArgumentException("หน่วยกิตต้องมีค่ามากกว่า 0");
            } else {
                eachSuject.add(credit.get(i).trim());
            }

            try {
                Integer.parseInt(section.get(i).trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("กรุณากรอกหมู่เรียนของวิชา และต้องเป็นตัวเลขในวิชาที่ " + (i + 1));
            }
            if (Integer.parseInt(section.get(i).trim()) <= 0) {
                throw new IllegalArgumentException("หมู่เรียนต้องมีค่ามากกว่า 0");
            } else {
                eachSuject.add(section.get(i).trim());
            }
            if (sectionType.get(i).isEmpty()) {
                throw new IllegalArgumentException("กรุณาเลือกประเภทหมู่เรียน");
            } else {
                eachSuject.add(sectionType.get(i).trim());
            }
            Pattern pattern = Pattern.compile("^[ก-๙a-zA-Z]+[ \t]+[ก-๙a-zA-Z]+$");
            Matcher matcher = pattern.matcher(teacher.get(i).trim());
            if (teacher.get(i) == null || teacher.get(i).isEmpty() || !matcher.find()) {
                throw new IllegalArgumentException("กรุณากรอกชื่ออาจารย์ประจำวิชาให้ถูกต้อง ในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(teacher.get(i).trim());
            }

            subjectList.add(eachSuject);
        }
    }

    public void addSubject(ArrayList<String> subject, ArrayList<String> idSubject,
                           ArrayList<String> registerType, ArrayList<String> credit,
                           ArrayList<String> section, ArrayList<String> sectionType,
                           ArrayList<String> teacher, byte type) {
        for(int i = 0; i < subject.size(); i++) {
            ArrayList<String> eachSuject = new ArrayList<>();
            if (subject.get(i) == null || subject.get(i).isEmpty()) {
                throw new IllegalArgumentException("กรุณากรอกชื่อวิชาในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(subject.get(i).trim());
            }
            if (idSubject.get(i) == null || idSubject.get(i).isEmpty()) {
                throw new IllegalArgumentException("กรุณากรอกรหัสวิชาในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(idSubject.get(i).trim());
            }
            if (registerType.get(i) == null || registerType.get(i).isEmpty()) {
                throw new IllegalArgumentException("กรุณาเลือกประเภทลงทะเบียนในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(registerType.get(i).trim());
            }
            try {
                Integer.parseInt(credit.get(i).trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("กรุณากรอกหน่วยกิตของวิชา และต้องเป็นตัวเลขในวิชาที่ " + (i + 1));
            }
            if (Integer.parseInt(credit.get(i).trim()) <= 0) {
                throw new IllegalArgumentException("หน่วยกิตต้องมีค่ามากกว่า 0");
            } else {
                eachSuject.add(credit.get(i).trim());
            }

            try {
                Integer.parseInt(section.get(i).trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("กรุณากรอกหมู่เรียนของวิชา และต้องเป็นตัวเลขในวิชาที่ " + (i + 1));
            }
            if (Integer.parseInt(section.get(i).trim()) <= 0) {
                throw new IllegalArgumentException("หมู่เรียนต้องมีค่ามากกว่า 0");
            } else {
                eachSuject.add(section.get(i).trim());
            }
            if (sectionType.get(i) == null || sectionType.get(i).isEmpty()) {
                throw new IllegalArgumentException("กรุณาเลือกประเภทหมู่เรียน");
            } else {
                eachSuject.add(sectionType.get(i).trim());
            }
            Pattern pattern = Pattern.compile("^[ก-๙a-zA-Z]+[ \t]+[ก-๙a-zA-Z]+$");
            Matcher matcher = pattern.matcher(teacher.get(i).trim());
            if (teacher.get(i) == null || teacher.get(i).isEmpty() || !matcher.find()) {
                throw new IllegalArgumentException("กรุณากรอกชื่ออาจารย์ประจำวิชาให้ถูกต้อง ในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(teacher.get(i).trim());
            }

            if (type == 1) eachSuject.add("add");
            else eachSuject.add("drop");

            subjectList.add(eachSuject);
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
                super.getStatusNext() + ","
                + curriculum + ","
                + tel + ","
                + semester + ","
                + year + ","
                + campus + "," +
                super.getReasonForNotApprove();
        for (ArrayList<String> eachSubject : subjectList) {
            for (String eachString : eachSubject) {
                text += "," + eachString;
            }
        }
        return text;
    }
}
