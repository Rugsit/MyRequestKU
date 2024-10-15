package ku.cs.models.request.approver;

import ku.cs.models.user.UserRoles;

public enum ApproverTier {
    ADVISOR("advisor"),
    DEPARTMENT("department"),
    FACULTY("faculty"),
    OTHER("other");

    private final String text;

    ApproverTier(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static boolean contains(String text) {
        for (ApproverTier role : ApproverTier.values()) {
            if (role.toString().equals(text)) {
                return true;
            }
        }
        return false;
    }
}
