package ku.cs.models.department;

import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.services.Datasource;
import ku.cs.services.FacultyListFileDatasource;
import java.util.UUID;

public class Department {
    private String name;
    private String id;
    private UUID uuid;
    private String faculty;
    private UUID facultyUuid;

    public Department(String name, String id, String faculty) throws NoFacultyException, IllegalArgumentException{
        setFaculty(faculty);
        setName(name);
        setId(id);
        uuid = UUID.randomUUID();
    }

    public Department(String[] department) throws NoFacultyException, IllegalArgumentException{
        uuid = UUID.fromString(department[0]);
        setName(department[1]);
        setId(department[2]);
        setFaculty(department[3]);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getFaculty() {
        return faculty;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getFacultyUuid() {
        return facultyUuid;
    }


    public void setName(String name) {
        name = name.trim();
        if (!name.isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("กรุณากรอกชื่อภาควิชาให้ถูกต้อง");
        }
    }

    public void setId(String id) {
        id = id.trim();
        if (!id.isEmpty()) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("กรุณากรอกรหัสภาควิชาให้ถูกต้อง");
        }
    }

    @Override
    public String toString() {
        return uuid + "," + name + "," + id + "," + faculty;
    }

    public void setFaculty(String faculty) throws NoFacultyException {
        if (faculty == null) throw new IllegalArgumentException("กรุณาใส่ชื่อคณะให้ถูกต้อง");
        faculty = faculty.trim();
        if (faculty.isEmpty()) {
            throw new NoFacultyException("กรุณาใส่ชื่อคณะให้ถูกต้อง");
        }
        FacultyList facultyList = new FacultyList();
        Datasource<FacultyList> facultyListFileDatasource = new FacultyListFileDatasource("data");
        facultyList = facultyListFileDatasource.readData();
        if(facultyList.getFacultyByName(faculty) == null) {
            throw new NoFacultyException("ไม่สามารถสร้างได้ เนื่องจากไม่มีคณะดังกล่าวอยู่ในระบบ");
        }
        this.faculty = faculty;
        this.facultyUuid = facultyList.getFacultyByName(faculty).getUuid();
    }

    void setFaculty(Faculty faculty){
        this.faculty = faculty.getName();
        this.facultyUuid = faculty.getUuid();
    }

}
