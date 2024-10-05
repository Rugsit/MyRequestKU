package ku.cs.models.user;

import ku.cs.models.department.Department;
import ku.cs.models.department.DepartmentList;
import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.DepartmentListFileDatasource;
import ku.cs.services.FacultyListFileDatasource;

import java.util.UUID;

public class DepartmentUser extends FacultyUser {
    private UUID department;

    public DepartmentUser(String id,
                          String username,
                          String role,
                          String firstname,
                          String lastname,
                          String lastLogin,
                          String email,
                          String password,
                          String faculty,
                          String department) throws UserException {
        super(id, username, role, firstname, lastname, lastLogin, email, password, faculty);
        setDepartment(department);
    }

    public DepartmentUser(String uuid,
                          String id,
                          String username,
                          String role,
                          String firstname,
                          String lastname,
                          String lastLogin,
                          String email,
                          String password,
                          String avatar,
                          String activeStatus,
                          String defaultPassword,
                          String faculty,
                          String department) throws UserException {
        super(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus, defaultPassword, faculty);
        setDepartment(UUID.fromString(department));

    }

    public UUID getDepartmentUUID(){
        return this.department;
    }
    public String getDepartment(){
        if(this.department != null){
            try {
                DepartmentListFileDatasource datasource = new DepartmentListFileDatasource("data");
                DepartmentList departmentList = datasource.readData();
                Department queryDepartment = departmentList.getDepartmentByUuid(this.department.toString());
                if(queryDepartment != null){
                    return queryDepartment.getName();
                }else{
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public void setDepartment(UUID departmentUUID) throws UserException {
        if(departmentUUID != null){
            this.department = departmentUUID;
        }else{
            throw new UserException("Invalid department : null");
        }
    }
    public void setDepartment(String facultyName) throws UserException {
        try {
            DepartmentListFileDatasource datasource = new DepartmentListFileDatasource("data");
            DepartmentList departmentList = datasource.readData();
            Department queryDepartment = departmentList.getDepartmentByName(facultyName);
            if(queryDepartment != null){
                this.department = queryDepartment.getUuid();
            }else{
                throw new UserException("Invalid department name : not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    private boolean isValidDepartment(String department){
//        return true; //WAITING...
//    }
    @Override
    public String toString() {
        return super.toString() + "," + department.toString();
    }

}
