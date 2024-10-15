package ku.cs.models.request.approver;

public enum ApproverTiers {
    ADVISOR("advisor"),
    DEPARTMENT("department"),
    FACULTY("faculty"),
    OTHER("other");

    private final String text;

    ApproverTiers(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static boolean contains(String text) {
        for (ApproverTiers availableTier : ApproverTiers.values()) {
            if (availableTier.toString().equals(text)) {
                return true;
            }
        }
        return false;
    }
}
