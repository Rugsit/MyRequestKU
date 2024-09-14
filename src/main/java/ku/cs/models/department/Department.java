package ku.cs.models.department;

import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.services.Datasource;
import ku.cs.services.FacultyListFileDatasource;

public class Department {
    private String name;
    private String id;
    private String faculty;
    private String facultyId;

    public Department(String name, String id, String faculty) throws NoFacultyException {
        setFaculty(faculty);
        this.name = name;
        this.id = id;
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

    public String getFacultyId() {
        return facultyId;
    }

    public void setName(String name) {
        name = name.trim();
        if (!name.isEmpty()) {
            this.name = name;
        }
    }

    public void setId(String id) {
        id = id.trim();
        if (!id.isEmpty()) {
            this.id = id;
        }
    }

    @Override
    public String toString() {
        return name + "," + id + "," + faculty + "," + facultyId;
    }

    public void setFaculty(String faculty) throws NoFacultyException {
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
        this.facultyId = facultyList.getFacultyByName(faculty).getId();
    }

    void setFaculty(Faculty faculty){
        this.faculty = faculty.getName();
        this.facultyId = faculty.getId();
    }

}
