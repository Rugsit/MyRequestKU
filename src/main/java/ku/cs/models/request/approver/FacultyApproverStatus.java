package ku.cs.models.request.approver;

public enum FacultyApproverStatus {
    INIT("รอส่งคณะ"),
    IN_PROCESS("รอคณะดำเนินการ"),
    FINISH("เรียบร้อย"),
    REJECTED("ไม่อนุมัติ");

    private final String text;

    FacultyApproverStatus(String text) {
        this.text = text;
    }


    public static String getFirst() {
        return INIT.toString();
    }

    @Override
    public String toString(){
        return text;
    }

    public static boolean contains(String status) {
        for (FacultyApproverStatus availableStatus : FacultyApproverStatus.values()) {
            if (availableStatus.toString().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
