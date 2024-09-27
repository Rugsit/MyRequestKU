package ku.cs.models.user;

import ku.cs.models.user.exceptions.UserException;

import java.util.UUID;

public class Student extends DepartmentUser {
    private UUID advisor;

    public Student(String id,
                   String username,
                   String role,
                   String firstname,
                   String lastname,
                   String lastLogin,
                   String email,
                   String password,
                   String faculty,
                   String department,
                   String advisorUUID) throws UserException {
        super(id, username, role, firstname, lastname, lastLogin, email, password, faculty, department);
        setAdvisor(advisorUUID);
    }

    public Student(String uuid,
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
                   String department,
                   String advisorUUID) throws UserException {
        super(uuid, id, username, role, firstname, lastname, lastLogin, email, password, avatar, activeStatus, faculty, department);
        setAdvisor(advisorUUID);
    }

    public UUID getAdvisor(){
        return this.advisor;
    }
    public void setAdvisor(Identifiable object){
        if(object instanceof Advisor){
            this.advisor = object.getUUID();
        }
    }
    public void setAdvisor(String advisorUUID){
        this.advisor = (!advisorUUID.equalsIgnoreCase("no-advisor")) ? UUID.fromString(advisorUUID) : null;
    }
    public void deleteAdvisor(){
        this.advisor = null;
    }

    @Override
    public String toString() {
        String advisorUUID = (advisor != null) ? advisor.toString() : "no-advisor";
        return super.toString() +  "," + advisorUUID;
    }
}
