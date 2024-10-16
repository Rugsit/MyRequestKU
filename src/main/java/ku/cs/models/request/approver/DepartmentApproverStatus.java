package ku.cs.models.request.approver;

public enum DepartmentApproverStatus {
    INIT("รอภาควิชาดำเนินการ"),
    FINISH("เรียบร้อย"),
    REJECTED("ไม่อนุมัติ");

    private final String text;

    DepartmentApproverStatus(String text) {
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
        for (DepartmentApproverStatus availableStatus : DepartmentApproverStatus.values()) {
            if (availableStatus.toString().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
