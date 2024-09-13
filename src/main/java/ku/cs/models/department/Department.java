package ku.cs.models.department;

import ku.cs.models.faculty.Faculty;

public class Department {
    private String name;
    private String id;
    private String faculty;
    private String facultyId;

    public Department(String name, String id, String faculty) {
        this.name = name;
        this.id = id;
        this.faculty = faculty;
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
