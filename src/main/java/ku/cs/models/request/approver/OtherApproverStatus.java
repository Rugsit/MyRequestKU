package ku.cs.models.request.approver;

public enum OtherApproverStatuses {
    INIT("รออัปโหลด"),
    FINISH("เรียบร้อย"),
    REJECTED("ไม่อนุมัติ");

    private final String text;

    OtherApproverStatuses(String text) {
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
        for (OtherApproverStatuses availableStatus : OtherApproverStatuses.values()) {
            if (availableStatus.toString().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
