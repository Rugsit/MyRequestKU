package ku.cs.models.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ku1AndKu3RequestForm extends Request{
    String curriculum;
    String tel;
    String semester;
    String year;
    String campus;
    ArrayList<ArrayList<String>> subjectList = new ArrayList<>();

    public Ku1AndKu3RequestForm(UUID uuid, UUID ownerUUID, String name, String nisitId, LocalDateTime timeStampLastUpdate,
                                LocalDateTime timeStampCreateForm, String requestType, String statusNow, String statusNext) {
        super(uuid, ownerUUID, name, nisitId, timeStampLastUpdate, timeStampCreateForm, requestType, statusNow, statusNext);
    }

    public Ku1AndKu3RequestForm(String[]data, String[]subject, byte type) {
        super.setUuid(UUID.fromString(data[1]));
        super.setOwnerUUID(UUID.fromString(data[2]));
        super.setName(data[3]);
        super.setNisitId(data[4]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        super.setTimeStamp(LocalDateTime.parse(data[5], formatter));
        super.setDate(LocalDateTime.parse(data[6], formatter));
        super.setStatusNow(data[7]);
        super.setStatusNext(data[8]);
        curriculum = data[9];
        tel = data[10];
        semester = data[11];
        year = data[12];
        campus = data[13];
        if (type == 1) {
            for (int i = 14; i < subject.length; i++) {
                ArrayList<String> newEachSubjectList = new ArrayList<>();
                for (int j = 0; j < 7; j++, i++) {
                    newEachSubjectList.add(subject[i]);
                }
                i--;
                subjectList.add(newEachSubjectList);
            }
        } else if (type == 3) {
            for (int i = 6; i < subject.length; i++) {
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
        Pattern pattern = Pattern.compile("^2[0-9]{3}$");
        Matcher matcher = pattern.matcher(year);
        if (!matcher.find()) {
            throw new IllegalArgumentException("กรุณากรอกปีการศึกษาให้ถูกต้อง");
        }
        this.year = year;
    }

    public void setCampus(String campus) {
        if (campus == null || campus.isBlank()) {
            throw new IllegalArgumentException("กรุณากรอกวิทยาเขต");
        }
        this.campus = campus;
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
                throw new IllegalArgumentException("คุณไม่ได้กรอกชื่อวิชาในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(subject.get(i));
            }
            if (idSubject.get(i) == null || idSubject.get(i).isEmpty()) {
                throw new IllegalArgumentException("คุณไม่ได้กรอกรหัสวิชาในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(idSubject.get(i));
            }
            if (registerType.get(i) == null || registerType.get(i).isEmpty()) {
                throw new IllegalArgumentException("คุณไม่ได้เลือกประเภทลงทะเบียนในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(registerType.get(i));
            }
            try {
                Integer.parseInt(credit.get(i));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("คุณต้องกรอกหน่วยกิตของวิชา และต้องเป็นตัวเลขในวิชาที่ " + (i + 1));
            }
            if (Integer.parseInt(credit.get(i)) <= 0) {
                throw new IllegalArgumentException("หน่วยกิตต้องมีค่ามากกว่า 0");
            } else {
                eachSuject.add(credit.get(i));
            }

            try {
                Integer.parseInt(section.get(i));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("คุณต้องกรอกหมู่เรียนของวิชา และต้องเป็นตัวเลขในวิชาที่ " + (i + 1));
            }
            if (Integer.parseInt(section.get(i)) <= 0) {
                throw new IllegalArgumentException("หมู่เรียนต้องมีค่ามากกว่า 0");
            } else {
                eachSuject.add(section.get(i));
            }
            if (sectionType.get(i) == null || sectionType.get(i).isEmpty()) {
                throw new IllegalArgumentException("คุณไม่ได้เลือกประเภทหมู่เรียน");
            } else {
                eachSuject.add(sectionType.get(i));
            }
            if (teacher.get(i) == null || teacher.get(i).isEmpty()) {
                throw new IllegalArgumentException("คุณไม่ได้กรอกชื่ออาจารย์ประจำวิชาในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(teacher.get(i));
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
                throw new IllegalArgumentException("คุณไม่ได้กรอกชื่อวิชาในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(subject.get(i));
            }
            if (idSubject.get(i) == null || idSubject.get(i).isEmpty()) {
                throw new IllegalArgumentException("คุณไม่ได้กรอกรหัสวิชาในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(idSubject.get(i));
            }
            if (registerType.get(i) == null || registerType.get(i).isEmpty()) {
                throw new IllegalArgumentException("คุณไม่ได้เลือกประเภทลงทะเบียนในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(registerType.get(i));
            }
            try {
                Integer.parseInt(credit.get(i));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("คุณต้องกรอกหน่วยกิตของวิชา และต้องเป็นตัวเลขในวิชาที่ " + (i + 1));
            }
            if (Integer.parseInt(credit.get(i)) <= 0) {
                throw new IllegalArgumentException("หน่วยกิตต้องมีค่ามากกว่า 0");
            } else {
                eachSuject.add(credit.get(i));
            }

            try {
                Integer.parseInt(section.get(i));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("คุณต้องกรอกหมู่เรียนของวิชา และต้องเป็นตัวเลขในวิชาที่ " + (i + 1));
            }
            if (Integer.parseInt(section.get(i)) <= 0) {
                throw new IllegalArgumentException("หมู่เรียนต้องมีค่ามากกว่า 0");
            } else {
                eachSuject.add(section.get(i));
            }
            if (sectionType.get(i) == null || sectionType.get(i).isEmpty()) {
                throw new IllegalArgumentException("คุณไม่ได้เลือกประเภทหมู่เรียน");
            } else {
                eachSuject.add(sectionType.get(i));
            }
            if (teacher.get(i) == null || teacher.get(i).isEmpty()) {
                throw new IllegalArgumentException("คุณไม่ได้กรอกชื่ออาจารย์ประจำวิชาในวิชาที่ " + (i + 1));
            } else {
                eachSuject.add(teacher.get(i));
            }
            if (type == 1) eachSuject.add("add");
            else eachSuject.add("drop");
            subjectList.add(eachSuject);
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
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
                + campus;
        for (ArrayList<String> eachSubject : subjectList) {
            for (String eachString : eachSubject) {
                text += "," + eachString;
            }
        }
        return text;
    }
}