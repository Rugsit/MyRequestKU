package ku.cs.models.department;

import javafx.scene.chart.PieChart;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.user.*;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.*;

import java.util.UUID;

public class Department implements Comparable<Department>{
    private String name;
    private String id;
    private final UUID uuid;
    private String faculty;
    private UUID facultyUuid;

    public Department(String name, String id, String faculty) throws NoFacultyException, IllegalArgumentException{
        setFaculty(faculty);
        setName(name);
        setId(id);
        uuid = UUID.randomUUID();
    }

    public Department(String[] department) throws NoFacultyException, IllegalArgumentException{
        uuid = UUID.fromString(department[0]);
        setName(department[1]);
        setId(department[2]);
        setFaculty(department[3]);
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Department){
            Department department = (Department) obj;
            if(this.uuid.equals(department.getUuid()))
                return true;
        }
        return false;
    }
    @Override
    public int hashCode(){
        return this.uuid.hashCode();
    }
    @Override
    public int compareTo(Department department) {
        return this.uuid.compareTo(department.getUuid());
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

    public UUID getUuid() {
        return uuid;
    }

    public UUID getFacultyUuid() {
        return facultyUuid;
    }


    public void setName(String name) {
        name = name.trim();
        if (!name.isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("กรุณากรอกชื่อภาควิชาให้ถูกต้อง");
        }
    }

    public void setId(String id) {
        id = id.trim();
        if (!id.isEmpty()) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("กรุณากรอกรหัสภาควิชาให้ถูกต้อง");
        }
    }

    @Override
    public String toString() {
        return uuid + "," + name + "," + id + "," + faculty;
    }

    public void setFaculty(String faculty) throws NoFacultyException {
        if (faculty == null) throw new IllegalArgumentException("กรุณาใส่ชื่อคณะให้ถูกต้อง");
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
        if (this.faculty == null) {
            this.faculty = faculty;
            this.facultyUuid = facultyList.getFacultyByName(faculty).getUuid();
            return;
        }

        this.faculty = faculty;
        this.facultyUuid = facultyList.getFacultyByName(faculty).getUuid();

        Datasource<UserList> studentDatasource = new UserListFileDatasource("data", "student.csv");
        Datasource<UserList> departmentStaffDatasource = new UserListFileDatasource("data", "department-staff.csv");
        Datasource<UserList> advisorDatasource = new UserListFileDatasource("data", "advisor.csv");
        UserList students = studentDatasource.readData();
        UserList departmentStaff = departmentStaffDatasource.readData();
        UserList advisors = advisorDatasource.readData();
        try {
            for (User student : students.getUsers()) {
                if ( ((Student) student).getDepartment().equals(name) ) {
                    ((Student) student).setFaculty(this.faculty);
                }
            }
            for (User staff : departmentStaff.getUsers()) {
                if ( ((DepartmentUser) staff).getDepartment().equals(name) ) {
                    ((DepartmentUser) staff).setFaculty(this.faculty);
                }
            }
            for (User advisor : advisors.getUsers()) {
                if ( ((Advisor) advisor).getDepartment().equals(name) ) {
                    ((Advisor) advisor).setFaculty(this.faculty);
                }
            }
            studentDatasource.writeData(students);
            departmentStaffDatasource.writeData(departmentStaff);
            advisorDatasource.writeData(advisors);
        } catch (UserException e) {
            System.err.println("Department : void setFaculty(Faculty) : error changing users' faculty");
        }
    }

    void setFaculty(Faculty faculty){
        this.faculty = faculty.getName();
        this.facultyUuid = faculty.getUuid();
    }

    public UserList getUsers() {
        Datasource<UserList> datasource = new UserListFileDatasource("data", "department-staff.csv");
        UserList departmentUsers = new UserList();
        UserList allUsers = new UserList();
        allUsers.concatenate(datasource.readData());
        datasource = new UserListFileDatasource("data", "advisor.csv");
        allUsers.concatenate(datasource.readData());
        datasource = new UserListFileDatasource("data", "student.csv");
        allUsers.concatenate(datasource.readData());
        try {
            for (User user : allUsers.getUsers()) {
                if (user instanceof Student && ((Student)user).getDepartmentUUID().equals(uuid)) {
                    departmentUsers.addUser(user);
                } else if (user instanceof Advisor && ((Advisor)user).getDepartmentUUID().equals(uuid)) {
                    departmentUsers.addUser(user);
                } else if (user instanceof DepartmentUser && ((DepartmentUser)user).getDepartmentUUID().equals(uuid)) {
                    departmentUsers.addUser(user);
                }
            }
        } catch (UserException e) {
            System.err.println("error reading only this department's users");
        }
        return departmentUsers;
    }

    public int getUsersCount() {
        return getUsers().getUsers().size();
    }

    public RequestList getRequests() {
        Datasource<RequestList> datasource = new RequestListFileDatasource("data");
        RequestList departmentRequests = new RequestList();
        RequestList allRequests = datasource.readData();
        for (Request request : allRequests.getRequests()) {
            if (request.getDepartmentUUID().equals(uuid) && request.getStatusNext().equals("คำร้องดำเนินการครบถ้วน")) {
                departmentRequests.addRequest(request);
            }
        }
        return departmentRequests;
    }

    public int getRequestsCount() {
        return getRequests().getRequests().size();
    }
}
