package ku.cs.models.department;

public class Department {
    String name;
    String id;
    String facultyAffiliation;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getFacultyAffiliation() {
        return facultyAffiliation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFacultyAffiliation(String facultyAffiliation) {
        this.facultyAffiliation = facultyAffiliation;
    }

    @Override
    public String toString() {
        return this.name + "," +
                this.id + "," +
                this.facultyAffiliation;
    }
}
