package ku.cs.models.user;

public enum UserRoles {
    ADMIN("admin"),
    ADVISOR("advisor"),
    FACULTY_STAFF("faculty-staff"),
    DEPARTMENT_STAFF("department-staff"),
    STUDENT("student");

    private final String text;

    UserRoles(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static boolean contains(String text) {
        for (UserRoles role : UserRoles.values()) {
            if (role.toString().equals(text)) {
                return true;
            }
        }
        return false;
    }

}
