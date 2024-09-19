package ku.cs.models.department;

import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.user.DepartmentUser;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.Datasource;
import ku.cs.services.FacultyListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.util.UUID;

public class Department {
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
            String oldName = this.name;
            this.name = name;
            updateUsers(oldName, "department");
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
        UserList students = studentDatasource.readData();
        UserList departmentStaff = departmentStaffDatasource.readData();
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
            studentDatasource.writeData(students);
            departmentStaffDatasource.writeData(departmentStaff);
        } catch (UserException e) {
            System.err.println("Department : void setFaculty(Faculty) : error changing users' faculty");
        }
    }

    void setFaculty(Faculty faculty){
        this.faculty = faculty.getName();
        this.facultyUuid = faculty.getUuid();
    }

    public void updateUsers(String name, String typeToUpdate) {
        if (!typeToUpdate.equals("department") && !typeToUpdate.equals("faculty")) {
            throw new IllegalArgumentException("Department error: Invalid type to update");
        }
        if (name == null || name.isEmpty() || name.equals(this.name) || name.equals(faculty)) { return; }
        Datasource<UserList> studentDatasource = new UserListFileDatasource("data", "student.csv");
        Datasource<UserList> departmentStaffDatasource = new UserListFileDatasource("data", "department-staff.csv");
        UserList students = studentDatasource.readData();
        UserList departmentStaff = departmentStaffDatasource.readData();
        try {
            if (typeToUpdate.equalsIgnoreCase("department")) {
                for (User student : students.getUsers()) {
                    if ( ((Student) student).getDepartment().equals(name) ) {
                        ((Student) student).setDepartment(this.name);
                    }
                }
                for (User staff : departmentStaff.getUsers()) {
                    if ( ((DepartmentUser) staff).getDepartment().equals(name) ) {
                        ((DepartmentUser) staff).setDepartment(this.name);
                    }
                }
            } else {
                for (User student : students.getUsers()) {
                    if ( ((Student) student).getFaculty().equals(faculty) ) {
                        ((Student) student).setFaculty(name);
                    }
                }
                for (User staff : departmentStaff.getUsers()) {
                    if ( ((DepartmentUser) staff).getFaculty().equals(faculty) ) {
                        ((DepartmentUser) staff).setFaculty(name);
                    }
                }
            }

            studentDatasource.writeData(students);
            departmentStaffDatasource.writeData(departmentStaff);
        } catch (UserException e) {
            System.err.println("Error in reading user list to edit department");
        }
    }


    public UserList getUsers(String userType) {
        Datasource<UserList> datasource = null;

        if (userType.equalsIgnoreCase("student")) {
             datasource = new UserListFileDatasource("data", "student.csv");

        } else if (userType.equalsIgnoreCase("staff")) {
            datasource = new UserListFileDatasource("data", "department-staff.csv");
        } else return null;

        UserList departmentUsers = new UserList();
        try {
            UserList allUsers = datasource.readData();
            if (userType.equals("student")) {
                for (User user : allUsers.getUsers()) {
                    Student departmentUser = (Student) user;
                    if (departmentUser.getDepartment().equals(name)) {
                        departmentUsers.addUser(departmentUser);
                    }
                }
            } else {
                for (User user : allUsers.getUsers()) {
                    DepartmentUser departmentUser = (DepartmentUser) user;
                    if (departmentUser.getDepartment().equals(name)) {
                        departmentUsers.addUser(departmentUser);
                    }
                }
            }
        } catch (UserException e) {
            System.err.println("error reading only this department's users");
        }
        return departmentUsers;
    }

}
