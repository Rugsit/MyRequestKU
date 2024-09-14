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
        FacultyList facultyList = new FacultyList();
        Datasource<FacultyList> facultyListFileDatasource = new FacultyListFileDatasource("data");
        facultyList = facultyListFileDatasource.readData();
        if(facultyList.getFacultyByName(faculty) == null) {
            throw new NoFacultyException("ไม่สามารถสร้างได้ เนื่องจากไม่มีคณะดังกล่าวอยู่ในระบบ");
        }
        this.name = name;
        this.id = id;
        this.faculty = faculty;
        this.facultyId = facultyList.getFacultyByName(faculty).getId();
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
}
