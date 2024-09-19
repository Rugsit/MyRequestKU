package ku.cs.models.faculty;

import ku.cs.models.department.Department;
import ku.cs.models.department.DepartmentList;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.Datasource;
import ku.cs.services.DepartmentListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.util.HashSet;
import java.util.UUID;

public class Faculty {
    private String name;
    private String id;
    private final UUID uuid;

    public Faculty(String name, String id) {
        try {
            setName(name);
            setId(id);
            uuid = UUID.randomUUID();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    public Faculty(String name, String id, String uuid) {
        try {
            setName(name);
            setId(id);
            this.uuid = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void setName(String name) {
        name = name.trim();
        if (name.isEmpty()){
            throw new IllegalArgumentException("กรุณาใส่ชื่อคณะให้ถูกต้อง");
        }
        String oldName = this.name;
        this.name = name;
        updateUsers(oldName);
    }

    public void setId(String id) {
        id = id.trim();
        if (id.isEmpty()){
            throw new IllegalArgumentException("กรุณาใส่รหัสคณะให้ถูกต้อง");
        }
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public DepartmentList getDepartmentsByFaculty(){
        Datasource<DepartmentList> datasource = new DepartmentListFileDatasource("data");
        DepartmentList departments = datasource.readData();
        DepartmentList selectedDepartments = new DepartmentList();
        for (Department department : departments.getDepartments()) {
            if (department.getFacultyUuid().equals(uuid)) {
                String[] data = department.toString().split(",");
                selectedDepartments.addDepartment(data);
            }
        }
        return selectedDepartments;
    }

    @Override
    public String toString() {
        return name + "," + id + "," + uuid;
    }

    public void updateUsers(String name) {
        if (name == null || name.equals(this.name)){ return; }
        DepartmentList departments = getDepartmentsByFaculty();
        for (Department department : departments.getDepartments()) {
            department.updateUsers(this.name, "faculty");
        }

        Datasource<UserList> datasource = new UserListFileDatasource("data", "faculty-staff.csv");
        UserList facultyStaff = datasource.readData();
        for (User user : facultyStaff.getUsers()) {
            if ( ((FacultyUser) user).getFaculty().equals(name) ){
                try {
                    ((FacultyUser) user).setFaculty(this.name);
                } catch (UserException e) {
                    System.err.println("error changing users' faculty");
                }
            }
        }
        datasource.writeData(facultyStaff);

    }
}
