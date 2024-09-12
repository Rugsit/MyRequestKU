package ku.cs.models.user;

import ku.cs.models.user.exceptions.UserException;

public class FacultyUser extends User{
    private String faculty;

    public FacultyUser(String id,
                       String username,
                       String role,
                       String firstname,
                       String lastname,
                       String lastLogin,
                       String email,
                       String password,
                       String faculty) throws UserException {
        super(id, username, role, firstname, lastname, lastLogin, email, password);
        setFaculty(faculty);
    }

    public FacultyUser(String uuid,
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
                       String faculty) throws UserException {
        super(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus);
        setFaculty(faculty);
    }

    public String getFaculty(){return this.faculty;}
    public void setFaculty(String faculty) throws UserException {
        if(isValidFaculty(faculty)){
            this.faculty = faculty;
        }else{
            throw new UserException("Invalid faculty");
        }
    }

    private boolean isValidFaculty(String faculty){
        return true; //WAITING...
    }

    @Override
    public String toString() {
        return super.toString() + ","
                + faculty;
    }
}
