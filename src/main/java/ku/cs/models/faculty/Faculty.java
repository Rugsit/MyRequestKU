package ku.cs.models.faculty;

import ku.cs.models.department.Department;
import ku.cs.models.department.DepartmentList;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.FacultyUser;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.*;

import java.util.HashSet;
import java.util.UUID;

public class Faculty implements Comparable<Faculty>{
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
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Faculty){
            Faculty faculty = (Faculty) obj;
            if(this.uuid.equals(faculty.getUuid()))
                return true;
        }
        return false;
    }
    @Override
    public int hashCode(){
        return this.uuid.hashCode();
    }
    @Override
    public int compareTo(Faculty faculty) {
        return this.uuid.compareTo(faculty.getUuid());
    }
    public void setName(String name) {
        name = name.trim();
        if (name.isEmpty()){
            throw new IllegalArgumentException("กรุณาใส่ชื่อคณะให้ถูกต้อง");
        }
        this.name = name;
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

    public UserList getUsers() {
        DepartmentList departments = getDepartmentsByFaculty();
        UserList facultyUsers = new UserList();
        for (Department department : departments.getDepartments()) {
            facultyUsers.concatenate(department.getUsers());
        }

        Datasource<UserList> datasource = new UserListFileDatasource("data", "faculty-staff.csv");
        UserList allFacultyStaff = datasource.readData();
        try {
            for (User user : allFacultyStaff.getUsers()) {
                if ( ((FacultyUser) user).getFacultyUUID().equals(uuid) ){
                        facultyUsers.addUser(user);
                }
            }
        } catch (UserException e) {
            System.err.println("error reading specific faculty's users");;
        }
        return facultyUsers;
    }

    public int getUsersCount() {
        return getUsers().getUsers().size();
    }


    public RequestList getRequests() {
        DepartmentList departmentList = getDepartmentsByFaculty();
        RequestList requests = new RequestList();
        for (Department department : departmentList.getDepartments()) {
            if (department.getFacultyUuid().equals(uuid)) {
                requests.concatenate(department.getRequests());
            }
        }
        return requests;
    }

    public int getRequestsCount() {
        return getRequests().getRequests().size();
    }
}
