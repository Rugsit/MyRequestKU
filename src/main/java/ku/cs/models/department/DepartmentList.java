package ku.cs.models.department;

import ku.cs.models.faculty.Faculty;
import java.util.HashSet;
import java.util.UUID;

public class DepartmentList {
    HashSet<Department> departments;
    public DepartmentList() {
        departments = new HashSet<>();
    };

    public void addDepartment(String name, String id, String faculty) {
        try {
            departments.add(new Department(name, id, faculty));
        } catch (NoFacultyException | IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void addDepartment(String[] department){
        try {
            departments.add(new Department(department));
        } catch (NoFacultyException | IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    public void addDepartment(Department department){
        if(department != null && !departments.contains(department)){
            departments.add(department);
        }
    }

    public HashSet<Department> getDepartments() {
        return departments;
    }

    public Department getDepartmentByName(String name){
        for (Department department : departments) {
            if (department.getName().equals(name)) {
                return department;
            }
        }
        return null;
    }

    public Department getDepartmentByUuid(String uuid) {
        for (Department department : departments) {
            if (department.getUuid().equals(UUID.fromString(uuid))) {
                return department;
            }
        }
        return null;
    }

    public void updateFaculty(Faculty faculty, String old) throws NoFacultyException {
        for (Department department : departments) {
            if (department.getFaculty().equals(old)) {
                department.setFaculty(faculty);
            }
        }
    }
}
