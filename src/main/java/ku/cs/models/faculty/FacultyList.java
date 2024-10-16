package ku.cs.models.faculty;

import java.util.HashSet;
import java.util.UUID;

public class FacultyList {
    HashSet<Faculty> facultyList;
    public FacultyList() {
        facultyList = new HashSet<>();
    }

    public void addFaculty(String name, String id) {
        try {
            facultyList.add(new Faculty(name, id));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void addFaculty(String[] faculty) {
        try {
            facultyList.add(new Faculty(faculty[0], faculty[1], faculty[2]));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public HashSet<Faculty> getFacultyList() {
        return facultyList;
    }

    public Faculty getFacultyByName(String name) {
        for (Faculty faculty : facultyList) {
            if (faculty.getName().equals(name)) {
                return faculty;
            }
        }
        return null;
    }

    public Faculty getFacultyByUuid(String uuid) {
        for (Faculty faculty : facultyList) {
            if (faculty.getUuid().equals(UUID.fromString(uuid))) {
                return faculty;
            }
        }
        return null;
    }
}
