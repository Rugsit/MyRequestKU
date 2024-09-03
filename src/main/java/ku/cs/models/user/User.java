package ku.cs.models.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ku.cs.models.user.exceptions.*;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static ku.cs.services.utils.StringCompare.*;

public class User implements Identifiable, Serializable {
    private final UUID uuid;
    private String id;
    private String username;
    private String role;

    private String firstname;
    private String lastname;
    private Date lastLogin;
    private String email;
    private String avatar;
    private String faculty;
    private String department;

    private String advisor;
    private boolean approver;
    private boolean active = true;

    private String password;
    private static String DATE_FORMAT = "yyyy-MM-dd:HH:mm:ss:Z";
    private final String[] AVAILABLE_ROLES = new String[]{
            "admin",
            "advisor",
            "faculty-staff",
            "faculty-approver",
            "department-staff",
            "department-approver",
            "student",
    };

    public User(String id,
                String username,
                String role,
                String firstname,
                String lastname,
                String lastLogin,
                String email,
                String faculty,
                String department,
                String password) throws UserException {
        //Constructor for New User
        this(UUID.randomUUID().toString(), id, username, role, firstname, lastname, lastLogin, email, faculty, department, null, "no-image");
        setPassword(password);
    }
    public User(String uuid,
                String id,
                String username,
                String role,
                String firstname,
                String lastname,
                String lastLogin,
                String email,
                String faculty,
                String department,
                String password,
                String avatar) throws UserException{
        //Contructor for DataSource Reader
        if(uuid == null) throw new UUIDException("UUID must not be null");
        this.uuid = UUID.fromString(uuid);
        setId(id);
        setUsername(username);
        setRole(role);
        setFirstname(firstname);
        setLastname(lastname);
        setLastLogin(lastLogin);
        setEmail(email);
        setFaculty(faculty);
        setDepartment(department);
        setAvatar(avatar);
        this.password = password;
    }

    //GETTER

    @Override
    public UUID getUUID() {
        return this.uuid;
    }
    @Override
    public String getId() {
        return this.id;
    }
    @Override
    public String getUsername() {
        return this.username;
    }
    @Override
    public String getRole() {
        return this.role;
    }

    @Override
    public String getName() {
        return this.firstname + " " + this.lastname;
    }
    @Override
    public String getFirstname() {
        return this.firstname;
    }
    @Override
    public String getLastname() {
        return this.lastname;
    }
    @Override
    public Date getLastLogin() {
        return this.lastLogin;
    }
    @Override
    public String getEmail(){
        return this.email;
    }
    public String getAvatar(){
        return this.avatar;
    }
    public String getActiveStatus(){
        return this.active?"Active":"Inactive";
    }

    public String getFaculty(){return this.faculty;}
    public String getDepartment(){return this.department;}
    //SETTER


    public void setId(String id) throws IDException{
        if(id == null) throw new IDException("ID must not be null");
        if(!isDigit(id)) throw new IDException("ID must be a number");
        if(haveSpace(id)) throw new IDException("ID must not contain spaces");
        if(id.length() != 10) throw new IDException("ID must be 10 characters");
        this.id = id.trim();
    }

    public void setUsername(String username) throws UsernameException{
        if(username == null) throw new UsernameException("Username must not be null");
        if(!isAlphaNumberic(username)) throw new UsernameException("Username must be alphanumeric");
        if(haveSpace(username))throw new UsernameException("Username must not contain spaces");
        if(username.length() > 30) throw new UsernameException("Username must be equal or less than 30 characters");
        this.username = username.trim();
    }

    public void setRole(String role) throws RoleException{
        if(role == null) throw new RoleException("Role must not be null");
        if(!isAplha(role)) throw new RoleException("Role must be alphabet");
        if(haveSpace(role))throw new RoleException("Role must not contain spaces");
        Boolean valid = false;
        role = role.trim().toLowerCase();
        for(String r : AVAILABLE_ROLES){
            if(r.equals(role.trim())) valid = true;
        }
        if(!valid) throw new RoleException("Role is not valid");
        this.role = role;
    }

    public void setFirstname(String firstname) throws NameException{
        if(firstname == null) throw new NameException("Firstname must not be null");
        if(!isAplha(firstname)) throw new NameException("Firstname must be alphabet");
        if(haveSpace(firstname)) throw new NameException("Firstname must not contain spaces");
        this.firstname = firstname.trim().toLowerCase();
    }

    public void setLastname(String lastname) throws NameException{
        if(lastname == null) throw new NameException("Lastname must not be null");
        if(!isAplha(lastname)) throw new NameException("Lastname must be alphabet");
        if(haveSpace(lastname)) throw new NameException("Lastname must not contain spaces");
        this.lastname = lastname.trim().toLowerCase();
    }

    public void setLastLogin(String dateString) throws DateException {
        if(dateString == null) throw new DateException("dateString must not be null");
        Date date = formatToDate(DATE_FORMAT,dateString);
//        if(date == null) throw new DateException("Invalid " + DATE_FORMAT + "format dateString");
        this.lastLogin = date;
    }

    public void setEmail(String email) throws EmailException{
        if(email == null) throw new EmailException("Email must not be null");
        if(haveSpace(email)) throw new EmailException("Email must not contain spaces");
//        if(isValidEmailPattern(email)) throw new EmailException("Invalid email pattern");
        this.email = email.trim();
    }

    public void setPassword(String password) throws PasswordException {
        if(password == null)
            throw new PasswordException("Password cannot be null");
        if(password.length() <= 8)
            throw new PasswordException("password must be more than 8 characters");

        String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        this.password = bcryptHashString;

    }

    //JUST ADD
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setFaculty(String faculty) { this.faculty = faculty; }
    public void setDepartment(String department) { this.department = department; }
    //VALIDATION

    @Override
    public Boolean isId(String id) {
        return this.id.equals(id);
    }
    @Override
    public Boolean isUUID(UUID uuid) {
        return uuid.equals(this.uuid);
    }
    @Override
    public Boolean isUsername(String username) {
        return this.username.equals(username);
    }
    @Override
    public Boolean isRole(String role) {
        return this.role.equalsIgnoreCase(role);
    }
    @Override
    public Boolean validatePassword(String password) throws PasswordException{
        if(password == null)
            throw new PasswordException("Password cannot be null");

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), this.password);
        return result.verified;
    }

    //MORE

    private static Date formatToDate(String format,String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            System.err.println("Error : ParseException\n\tformatToDate method returns null!");
        }
        return date;
    }
    private static String dateToFormatString(String format,Date date) {
        String dateString = "NO_DATE";
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            dateString = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User){
            User user = (User) obj;
            if(user.uuid.equals(this.uuid))
                return true;
        }
        return false;
    }
    @Override
    public int hashCode(){
        return this.uuid.hashCode();
    }

    @Override
    public String toString() {
        String dateString = dateToFormatString(DATE_FORMAT, lastLogin);
        return uuid.toString() + "," +
                id + "," +
                username + "," +
                role + "," +
                firstname + "," +
                lastname + "," +
                dateString + "," +
                email + "," +
                faculty + "," +
                department + "," +
                password + "," +
                avatar;
    }
}
