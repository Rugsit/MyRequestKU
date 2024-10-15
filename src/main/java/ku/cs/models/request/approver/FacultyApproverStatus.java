package ku.cs.models.request.approver;

public enum FacultyApproverStatuses {
    INIT("รอส่งคณะ"),
    IN_PROCESS("รอคณะดำเนินการ"),
    FINISH("เรียบร้อย"),
    REJECTED("ไม่อนุมัติ");

    private final String text;

    FacultyApproverStatuses(String text) {
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
        for (FacultyApproverStatuses availableStatus : FacultyApproverStatuses.values()) {
            if (availableStatus.toString().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
