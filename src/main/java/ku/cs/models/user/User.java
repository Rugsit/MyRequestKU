package ku.cs.models.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ku.cs.models.user.exceptions.*;
import ku.cs.services.UserListFileDatasource;

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
    private String defaultPassword;
    public static String DATE_FORMAT = "yyyy-MM-dd:HH:mm:ss";
    private UserListFileDatasource checkUserListFileDatasource;
    private UserList checkUserList;

    public User(String id,
                String username,
                String role,
                String firstname,
                String lastname,
                String lastLogin,
                String email,
                String password) throws UserException {
        //Constructor for New User
        this(UUID.randomUUID().toString(), id, username, role, firstname, lastname, lastLogin, email, password, "no-image","active","DEFAULT");
        setId(id,true);//try to check when new user
        setUsername(username,true);//try to check when new user
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
                String activeStatus,
                String defaultPassword) throws UserException{
        //Contructor for DataSource Reader
        if(uuid == null) throw new UUIDException("UUID must not be null");
        this.uuid = UUID.fromString(uuid);
        setRole(role);
        setId(id,false);//not check when use datasource - TRUST!
        setUsername(username,false);//not check when use datasource - TRUST!
        setFirstname(firstname);
        setLastname(lastname);
        setLastLogin(lastLogin);
        setEmail(email);
        setAvatar(avatar);
        this.password = password;
        this.active = activeStatus.equalsIgnoreCase("active");
        initDefaultPassword(defaultPassword);
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
    public String generatePassword(){
        long seed = System.currentTimeMillis();
        Random random = new Random();
        random.setSeed(seed);
        int max=999999999,min=100000000;
        String generatePassword = "" + (random.nextInt(max - min + 1) + min);
        return generatePassword;
    }
    public String getDefaultPassword() {
        return this.defaultPassword;
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
        setId(id, true);//for normal use, must check
    }
    public void setId(String id,boolean check) throws IDException{
        if(id == null) throw new IDException("ID must not be null");
        if(this.id != null && this.id.equalsIgnoreCase(id))return;
        if(id.isEmpty()) throw new IDException("รหัสหรือไอดีต้องไม่เป็นค่าว่าง");
        if(haveSpace(id)) throw new IDException("รหัสหรือไอดีต้องไม่มีช่องว่าง");
        if(role.equalsIgnoreCase("student")){
            if(!isDigit(id)) throw new IDException("รหัสนิสิตต้องเป็นตัวเลข");
            if(id.length() != 10) throw new IDException("รหัสนิสิตต้องมี 10 ตัวอักษร");
        }
        if(!isAlphaNumberic(id)) throw new IDException("รหัสหรือไอดีต้องเป็นตัวอักษรและตัวเลข");

        if(check){
            checkUserListFileDatasource = new UserListFileDatasource("data","student.csv");
            checkUserList = checkUserListFileDatasource.readAllUser();
            if(id != "0000000000" && id !=  "no-id"){
                User exitUser = checkUserList.findUserById(id);
                if(exitUser != null) {
                    throw new IDException("มีไอดีผู้ใช้นงานี้อยู่แล้ว");
                }
            }
        }
        this.id = id.trim();
    }

    public void setUsername(String username) throws UsernameException{
        setUsername(username, true);//for normal use, must check
    }
    public void setUsername(String username,boolean check) throws UsernameException{
        if(username == null) throw new UsernameException("Username must not be null");
        if(this.username != null && this.username.equalsIgnoreCase(username))return;
        if(username.isEmpty()) throw new UsernameException("ชื่อผู้ใช้งานต้องไม่เป็นค่าว่าง");
        if(haveSpace(username))throw new UsernameException("ชื่อผู้ใช้งานต้องไม่มีช่องว่าง");
        if(!isAlphaNumberic(username)) throw new UsernameException("ชื่อผู้ใช้งานต้องเป็นตัวอักษรและตัวเลข");
        if(username.length() > 30) throw new UsernameException("ชื่อผู้ใช้งานต้องมีน้อยกว่าหรือเท่ากับ 30 ตัวอักษร");

        if(check){
            checkUserListFileDatasource = new UserListFileDatasource("data","student.csv");
            checkUserList = checkUserListFileDatasource.readAllUser();
            if(username != "no-username"){
                User exitUser = checkUserList.findUserByUsername(username);
                if(exitUser != null) {
                    throw new UsernameException("มีชื่อผู้ใช้งานนี้อยู่แล้ว");
                }
            }
        }
        this.username = username.trim();
    }

    public void setRole(String role) throws RoleException{
        if(role == null) throw new RoleException("Role must not be null");
        if(role.isEmpty()) throw new RoleException("บทบาทต้องไม่เป็นค่าว่าง");
        if(haveSpace(role))throw new RoleException("บทบาทต้องไม่มีช่องว่าง");
        if(!isAlphaNumberic(role)) throw new RoleException("บทบาทต้องเป็นตัวอักษรและตัวเลข");
        Boolean valid = false;
        role = role.trim().toLowerCase();
        for(UserRoles r : UserRoles.values()){
            if(r.toString().equals(role.trim())) valid = true;
        }
        if(!valid) throw new RoleException("บทบาทไม่ถูกต้อง");
        this.role = role;
    }

    public void setFirstname(String firstname) throws NameException{
        if(firstname == null) throw new NameException("Firstname must not be null");
        if(firstname.isEmpty()) throw new NameException("ชื่อจริงต้องไม่เป็นค่าว่าง");
        if(startWithSpace(firstname)) throw new NameException("ชื่อจริงต้องไม่เริ่มต้นด้วยช่องว่าง");
        if(endWithSpace(firstname)) throw new NameException("ชื่อจริงต้องไม่ลงท้ายด้วยช่องว่าง");
        if(haveDuplicateSpace(firstname)) throw new NameException("ชื่อจริงต้องไม่มีช่องว่างซ้ำกัน");
        if(!isAplha(firstname)) throw new NameException("ชื่อจริงต้องเป็นตัวอักษร");
        this.firstname = firstname.trim().toLowerCase();
    }

    public void setLastname(String lastname) throws NameException{
        if(lastname == null) throw new NameException("Lastname must not be null");
        if(lastname.isEmpty()) throw new NameException("นามสกุลต้องไม่เป็นค่าว่าง");
        if(startWithSpace(lastname)) throw new NameException("นามสกุลต้องไม่เริ่มต้นด้วยช่องว่าง");
        if(endWithSpace(lastname)) throw new NameException("นามสกุลต้องไม่ลงท้ายด้วยช่องว่าง");
        if(haveDuplicateSpace(lastname)) throw new NameException("นามสกุลต้องไม่มีช่องว่างซ้ำกัน");
        if(!isAplha(lastname)) throw new NameException("นามสกุลต้องเป็นตัวอักษร");
        this.lastname = lastname.trim().toLowerCase();
    }

    public void setLastLogin(String dateString) throws DateException {
        if(dateString == null) throw new DateException("dateString must not be null");
        if(dateString.isEmpty()) throw new DateException("dateString must not be empty");
        LocalDateTime date = formatToLocalDateTime(DATE_FORMAT,dateString);
        this.lastLogin = date;
    }

    public void setEmail(String email) throws EmailException{
        if(email == null) throw new EmailException("Email must not be null");
        if(email.isEmpty()) throw new EmailException("อีเมลต้องไม่เป็นค่าว่าง");
        if(haveSpace(email)) throw new EmailException("อีเมลต้องไม่มีช่องว่าง");
        if(!isValidEmailPattern(email)) throw new EmailException("รูปแบบอีเมลไม่ถูกต้อง");
        this.email = email.trim();
    }
    private void initDefaultPassword(String password) throws PasswordException {
        if(password == null) throw new PasswordException("DefaultPassword cannot be null");
        if(password.isEmpty()) throw new PasswordException("รหัสผ่านเริ่มต้นต้องไม่เป็นค่าว่าง");
        if(haveSpace(password)) throw new PasswordException("รหัสผ่านเริ่มต้นต้องไม่มีช่องว่าง");
        if(password.equalsIgnoreCase("DEFAULT")) {
            password = generatePassword();
        }
        if(password.length() <= 8)
            throw new PasswordException("รหัสผ่านเริ่มต้นต้องมีมากกว่า 8 ตัวอักษร");
        this.defaultPassword = password;

    }
    public void setDefaultPassword(String password) throws PasswordException {
        if(password == null) throw new PasswordException("DefaultPassword cannot be null");
        if(password.isEmpty()) throw new PasswordException("รหัสผ่านเริ่มต้นไม่ควรเป็นค่าว่าง");
        if(haveSpace(password)) throw new PasswordException("รหัสผ่านเริ่มต้นไม่ควรมีช่องว่าง");
        if(password.equalsIgnoreCase("DEFAULT")) {
            password = generatePassword();
        }
        if(password.length() <= 8)
            throw new PasswordException("รหัสผ่านเริ่มต้นต้องมีมากกว่า 8 ตัวอักษร");
        boolean samePassword = validatePassword(this.defaultPassword);//take long time, use only for manual UI
        this.defaultPassword = password;

        if(samePassword){
            setPassword("DEFAULT");
        }

    }
    public void setPassword(String password) throws PasswordException {
        if(password == null) throw new PasswordException("Password cannot be null");
        if(password.isEmpty()) throw new PasswordException("รหัสผ่านต้องไม่เป็นค่าว่าง");
        if(haveSpace(password)) throw new PasswordException("รหัสผ่านต้องไม่มีช่องว่าง");
        if(password.equalsIgnoreCase("DEFAULT")) {
            password = getDefaultPassword();
        }
        if(password.length() <= 8)
            throw new PasswordException("รหัสผ่านต้องมีมากกว่า 8 ตัวอักษร");


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
                activeStatus + "," +
                defaultPassword;
    }
}
