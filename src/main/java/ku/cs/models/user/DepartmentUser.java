package ku.cs.models.user;

import ku.cs.models.user.exceptions.UserException;

public class DepartmentUser extends FacultyUser {
    private String department;

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
                          String faculty,
                          String department) throws UserException {
        super(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus, faculty);
        setDepartment(department);

    }

    public String getDepartment(){
        return this.department;
    }
    public void setDepartment(String department) throws UserException {
        if(isValidDepartment(department)){
            this.department = department;
        }else{
            throw new UserException("Invalid Department");
        }
    }
    private boolean isValidDepartment(String department){
        return true; //WAITING...
    }
    @Override
    public String toString() {
        return super.toString() + "," + department;
    }

}
