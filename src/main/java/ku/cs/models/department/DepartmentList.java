package ku.cs.models.department;

import java.util.HashMap;
import java.util.HashSet;

public class DepartmentList {
    private HashSet<Department> departmentList;

    public DepartmentList() {
        departmentList = new HashSet<>();
    }

    public void addDepartment(Department department) {
        departmentList.add(department);
    }

    public void addDepartment(String[] data) {
        Department department = new Department();
        department.setName(data[0]);
        department.setId(data[1]);
        department.setFacultyAffiliation(data[2]);
        departmentList.add(department);
    }

    public HashSet<Department> getDepartment() {
        return departmentList;
    }
}
