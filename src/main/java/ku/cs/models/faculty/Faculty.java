package ku.cs.models.faculty;

import ku.cs.models.department.Department;
import ku.cs.models.department.DepartmentList;

public class Faculty {
    private String name;
    private String id;

    public Faculty(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        name = name.trim();
        if (!name.isEmpty()){
            this.name = name;
        }
    }

    public void setId(String id) {
        id = id.trim();
        if (!id.isEmpty()){
            this.id = id;
        }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + "," + id;
    }
}
