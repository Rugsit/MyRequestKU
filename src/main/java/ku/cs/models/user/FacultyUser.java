package ku.cs.models.user;

import ku.cs.models.faculty.Faculty;
import ku.cs.models.faculty.FacultyList;
import ku.cs.models.user.exceptions.UserException;
import ku.cs.services.FacultyListFileDatasource;

import java.util.UUID;

public class FacultyUser extends User{
    private UUID faculty;

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
                       String defaultPassword,
                       String faculty) throws UserException {
        super(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus, defaultPassword);
        setFaculty(UUID.fromString(faculty));
    }
    public UUID getFacultyUUID(){
        return this.faculty;
    }
    public String getFaculty(){
        if(this.faculty != null){
            try {
                FacultyListFileDatasource datasource = new FacultyListFileDatasource("data");
                FacultyList facultyList = datasource.readData();
                Faculty queryFaculty = facultyList.getFacultyByUuid(this.faculty.toString());
                if(queryFaculty != null){
                    return queryFaculty.getName();
                }else{
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public void setFaculty(UUID facultyUUID) throws UserException {
        if(facultyUUID != null){
            this.faculty = facultyUUID;
        }else{
            throw new UserException("Invalid faculty : null");
        }
    }
    public void setFaculty(String facultyName) throws UserException {
        try {
            FacultyListFileDatasource datasource = new FacultyListFileDatasource("data");
            FacultyList facultyList = datasource.readData();
            Faculty queryFaculty = facultyList.getFacultyByName(facultyName);
            if(queryFaculty != null){
                this.faculty = queryFaculty.getUuid();
            }else{
                throw new UserException("Invalid faculty name : not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return super.toString() + ","
                + faculty.toString();
    }
}
