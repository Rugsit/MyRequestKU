package ku.cs.models.department;

import java.io.IOException;

public class NoFacultyException extends IOException {
    public NoFacultyException() {
        super();
    }
    public NoFacultyException(String message) {
        super(message);
    }
}
