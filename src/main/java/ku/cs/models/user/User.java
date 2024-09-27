package ku.cs.models.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ku.cs.models.user.exceptions.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Random;
import java.util.UUID;

import static ku.cs.services.utils.DateTools.localDateTimeToFormatString;
import static ku.cs.services.utils.DateTools.formatToLocalDateTime;
import static ku.cs.services.utils.StringCompare.*;

public abstract class User implements Identifiable, Comparable<User> {
    private final UUID uuid;
    private String id;
    private String username;
    private String role;

    private String firstname;
    private String lastname;
    private LocalDateTime lastLogin;
    private String email;
    private String avatar;
    private boolean active;
    private String password;
    public static String DATE_FORMAT = "yyyy-MM-dd:HH:mm:ss";

    public User(String id,
                String username,
                String role,
                String firstname,
                String lastname,
                String lastLogin,
                String email,
                String password) throws UserException {
        //Constructor for New User
        this(UUID.randomUUID().toString(), id, username, role, firstname, lastname, lastLogin, email, null, "no-image","active");
        setPassword(password);
        this.active = !role.equalsIgnoreCase("student");
    }
    public User(String uuid,
                String id,
                String username,
                String role,
                String firstname,
                String lastname,
                String lastLogin,
                String email,
                String password,
                String avatar,
                String activeStatus) throws UserException{
        //Contructor for DataSource Reader
        if(uuid == null) throw new UUIDException("UUID must not be null");
        this.uuid = UUID.fromString(uuid);
        setRole(role);
        setId(id);
        setUsername(username);
        setFirstname(firstname);
        setLastname(lastname);
        setLastLogin(lastLogin);
        setEmail(email);
        setAvatar(avatar);
        this.password = password;
        this.active = activeStatus.equalsIgnoreCase("active");
    }
    //Comparator
    public static Comparator<User> userIdComparator = new Comparator<>() {
        public int compare(User u1, User u2) {
            return u1.getId().compareTo(u2.getId());
        }
    };
    public static Comparator<User> usernameComparator = new Comparator<>() {
        public int compare(User u1, User u2) {
            return u1.getUsername().compareTo(u2.getUsername());
        }
    };

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
    public LocalDateTime getLastLogin() {
        return this.lastLogin;
    }
    @Override
    public String getEmail(){
        return this.email;
    }
    public String getDefaultPassword() {
        String strUUID = this.uuid.toString();
        int sum = 0;
        for(char c : strUUID.toCharArray()) {
            sum += c;
        }
        Random random = new Random();
        random.setSeed(sum);
        int max=999999999,min=100000000;
        String defaultPassword = "" + (random.nextInt(max - min + 1) + min);
        return defaultPassword;
    }
    public String getAvatar(){
        return this.avatar;
    }
    public String getActiveStatus(){
        return this.active?"Active":"Inactive";
    }
    public String getDefaultAvatarName(){
        return getUUID().toString() +"-avatar";
    }
    //SETTER

    public void setId(String id) throws IDException{
        if(id == null) throw new IDException("ID must not be null");
        if(id.isEmpty()) throw new IDException("ID must not be empty");
        if(haveSpace(id)) throw new IDException("ID must not contain spaces");
        if(role.equalsIgnoreCase("student")){
            if(!isDigit(id)) throw new IDException("Nisit ID must be digits");
            if(id.length() != 10) throw new IDException("Nisit ID must be 10 characters");
        }
        if(!isAlphaNumberic(id)) throw new IDException("ID must be a alphanumeric");
        this.id = id.trim();
    }

    public void setUsername(String username) throws UsernameException{
        if(username == null) throw new UsernameException("Username must not be null");
        if(username.isEmpty()) throw new UsernameException("Username must not be empty");
        if(haveSpace(username))throw new UsernameException("Username must not contain spaces");
        if(!isAlphaNumberic(username)) throw new UsernameException("Username must be alphanumeric");
        if(username.length() > 30) throw new UsernameException("Username must be equal or less than 30 characters");
        this.username = username.trim();
    }

    public void setRole(String role) throws RoleException{
        if(role == null) throw new RoleException("Role must not be null");
        if(role.isEmpty()) throw new RoleException("Role must not be empty");
        if(haveSpace(role))throw new RoleException("Role must not contain spaces");
        if(!isAlphaNumberic(role)) throw new RoleException("Role must be alphanumeric");
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
        if(firstname.isEmpty()) throw new NameException("Firstname must not be empty");
        if(startWithSpace(firstname)) throw new NameException("Firstname must not start with spaces");
        if(endWithSpace(firstname)) throw new NameException("Firstname must not end with spaces");
        if(haveDuplicateSpace(firstname)) throw new NameException("Firstname must not contain duplicate spaces");
        if(!isAplha(firstname)) throw new NameException("Firstname must be alphabet");
        this.firstname = firstname.trim().toLowerCase();
    }

    public void setLastname(String lastname) throws NameException{
        if(lastname == null) throw new NameException("Lastname must not be null");
        if(lastname.isEmpty()) throw new NameException("Lastname must not be empty");
        if(startWithSpace(lastname)) throw new NameException("Lastname must not start with spaces");
        if(endWithSpace(lastname)) throw new NameException("Lastname must not end with spaces");
        if(haveDuplicateSpace(lastname)) throw new NameException("Lastname must not contain duplicate spaces");
        if(!isAplha(lastname)) throw new NameException("Lastname must be alphabet");
        this.lastname = lastname.trim().toLowerCase();
    }

    public void setLastLogin(String dateString) throws DateException {
        if(dateString == null) throw new DateException("dateString must not be null");
        if(dateString.isEmpty()) throw new DateException("dateString must not be empty");
        LocalDateTime date = formatToLocalDateTime(DATE_FORMAT,dateString);
//        if(date == null) throw new DateException("Invalid " + DATE_FORMAT + "format dateString");
        this.lastLogin = date;
    }

    public void setEmail(String email) throws EmailException{
        if(email == null) throw new EmailException("Email must not be null");
        if(email.isEmpty()) throw new EmailException("Email must not be empty");
        if(haveSpace(email)) throw new EmailException("Email must not contain spaces");
//        if(isValidEmailPattern(email)) throw new EmailException("Invalid email pattern");
        this.email = email.trim();
    }

    public void setPassword(String password) throws PasswordException {
        if(password == null) throw new PasswordException("Password cannot be null");
        if(password.isEmpty()) throw new PasswordException("Password must not be empty");
        if(haveSpace(password)) throw new PasswordException("Password must not contain spaces");
        if(password.equalsIgnoreCase("DEFAULT")) {
            password = getDefaultPassword();
        }
        if(password.length() <= 8)
            throw new PasswordException("password must be more than 8 characters");


        String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        this.password = bcryptHashString;

    }

    //JUST ADD
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public void setActive(boolean active) {
        this.active = active;
    }
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
    public boolean isActive(){
        return this.active;
    }

    //MORE


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Identifiable){
            Identifiable user = (Identifiable)obj;
            if(this.uuid.equals(user.getUUID()))
                return true;
        }
        return false;
    }
    @Override
    public int hashCode(){
        return this.uuid.hashCode();
    }
    @Override
    public int compareTo(User user) {
        return this.uuid.compareTo(user.getUUID());
    }

    @Override
    public String toString() {
        String lastLogin = localDateTimeToFormatString(DATE_FORMAT, this.lastLogin);
        String activeStatus = (active ? "active" : "inactive");
        return uuid.toString() + "," +
                id + "," +
                username + "," +
                role + "," +
                firstname + "," +
                lastname + "," +
                lastLogin + "," +
                email + "," +
                password + "," +
                avatar + "," +
                activeStatus;
    }
}
