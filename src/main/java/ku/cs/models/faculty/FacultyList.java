package ku.cs.models.faculty;

import java.util.HashSet;

public class FacultyList {
    private HashSet<Faculty> facultyList;
    public FacultyList() {facultyList = new HashSet<>();}

    public void addFaculty(Faculty fac) {
        facultyList.add(fac);
    }

    public void addFaculty(String[] data) {
        Faculty fac = new Faculty();
        fac.setName(data[0]);
        fac.setId(data[1]);
        facultyList.add(fac);
    }

    public HashSet<Faculty> getFaculty() {
        return facultyList;
    }
}
