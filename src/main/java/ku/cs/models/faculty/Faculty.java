package ku.cs.models.faculty;

public class Faculty {
    private String name;
    private String id;

    public Faculty(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        name = name.trim();
        if (name.isEmpty()){
            throw new IllegalArgumentException("กรุณาใส่ชื่อคณะให้ถูกต้อง");
        }
        this.name = name;
    }

    public void setId(String id) {
        id = id.trim();
        if (id.isEmpty()){
            throw new IllegalArgumentException("กรุณาใส่ชื่อคณะให้ถูกต้อง");
        }
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + "," + id;
    }
}
