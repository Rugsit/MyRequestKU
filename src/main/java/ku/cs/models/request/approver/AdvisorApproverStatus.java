package ku.cs.models.request.approver;

public enum AdvisorApproverStatuses {
    INIT("รออาจารย์ที่ปรึกษา"),
    FINISH("เรียบร้อย"),
    REJECTED("ไม่อนุมัติ");

    private final String text;

    AdvisorApproverStatuses(String text) {
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
        for (AdvisorApproverStatuses availableStatus : AdvisorApproverStatuses.values()) {
            if (availableStatus.toString().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
