package ku.cs.models.department;

import java.util.HashSet;

public class DepartmentList {
    HashSet<Department> departments;
    public DepartmentList() {
        departments = new HashSet<>();
    };


    public void addDepartment(String[] department){
        departments.add(new Department(department[0], department[1], department[2]));
    }

    public HashSet<Department> getDepartments() {
        return departments;
    }

    public Department getDepartmentById(String id){
        for (Department department : departments) {
            if (department.getId().equals(id)) {
                return department;
            }
        }
        return null;
    }

    public Department getDepartmentByName(String name){
        for (Department department : departments) {
            if (department.getName().equals(name)) {
                return department;
            }
        }
        return null;
    }


    public void removeDepartmentById(String id) {
        for (Department department : departments) {
            if (department.getId().equals(id)) {
                departments.remove(department);
            }
        }
    }

    public void removeDepartmentByName(String name) {
        for (Department department : departments) {
            if (department.getName().equals(name)) {
                departments.remove(department);
            }
        }
    }
}
