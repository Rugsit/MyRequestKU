package ku.cs.models.request.approver;

import ku.cs.models.request.Request;
import ku.cs.models.request.approver.exception.ApproverException;
import ku.cs.models.request.approver.exception.ApproverRoleException;
import ku.cs.models.request.approver.exception.ApproverStatusException;
import ku.cs.models.request.approver.exception.ApproverTierException;
import java.util.UUID;

public class Approver implements Comparable<Approver> {
    protected final UUID uuid;
    protected UUID requestUUID;
    protected String firstname;
    protected String lastname;
    protected final String tier;
    protected String role;
    protected String status;
    protected String signatureFile;
    private boolean disableView = false;
    private UUID associateUUID;


    public Approver(String requestUUID, String tier, String associateUUID, String role, String firstname, String lastname) throws ApproverException {
        this(UUID.randomUUID().toString(), requestUUID, tier, associateUUID, role, "no-status", "no-image", firstname, lastname);
        setInitialStatus();
    }

    public Approver(String tier, String associateUUID, String role, String firstname, String lastname) throws ApproverException {
        this(UUID.randomUUID().toString(), null, tier, associateUUID,role, "no-status", "no-image", firstname, lastname);
        setInitialStatus();
    }

    public Approver(String uuid, String requestUUID, String tier, String associateUUID,String role, String status, String signatureFile, String firstname, String lastname) throws ApproverException {
        if (uuid == null) throw new ApproverException("uuid must not be null");
        if (uuid.isEmpty()) throw new ApproverException("uuid must not be empty");
        if (associateUUID == null) throw new ApproverException("associate uuid must not be null");
        if (associateUUID.isEmpty()) throw new ApproverException("associate uuid must not be null");
        this.uuid = UUID.fromString(uuid);
        this.associateUUID = UUID.fromString(associateUUID);
        if (requestUUID != null && !requestUUID.equals("no-request")) {
            if (requestUUID.isEmpty()) throw new ApproverException("requestUUID must not be empty");
            this.requestUUID = UUID.fromString(requestUUID);
        }
        //Tier Checker
        if (tier == null) throw new ApproverTierException("tier must not be null");
        if (tier.isEmpty()) throw new ApproverTierException("ระดับต้องไม่เป็นค่าว่าง");
        if (ApproverTiers.contains(tier)) this.tier = tier;
        else throw new ApproverTierException("Invalid approver tier : " + tier);

        if (role == null) throw new ApproverRoleException("role must not be null");
        if (role.isEmpty()) throw new ApproverRoleException("ตำแหน่งต้องไม่เป็นค่าว่าง");
        this.role = role;
        this.status = status;
        this.signatureFile = signatureFile;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public UUID getUUID() {
        return uuid;
    }

    public UUID getRequestUUID() {
        return requestUUID;
    }

    public String getTier() {
        return tier;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getSignatureFile() {
        return signatureFile;
    }

    public String getSignatureFilename() {
        return "signature-" + requestUUID.toString() + "-" + uuid.toString();
    }

    public UUID getAssociateUUID() {
        return associateUUID;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getName() {
        return getFirstname() + " " + getLastname();
    }

    public boolean getDisableView() {
        return disableView;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(String status) throws ApproverStatusException {
        if (
                (tier.equals(ApproverTiers.FACULTY.toString()) && FacultyApproverStatus.contains(status)) ||
                (tier.equals(ApproverTiers.DEPARTMENT.toString()) && DepartmentApproverStatus.contains(status)) ||
                (tier.equals(ApproverTiers.ADVISOR.toString()) && AdvisorApproverStatus.contains(status)) ||
                (tier.equals(ApproverTiers.OTHER.toString()) && OtherApproverStatus.contains(status))
        ) {
            this.status = status;
        } else {
            throw new ApproverStatusException("Invalid" + tier + "approver status : " + status);
        }

    }
    public void setDisableView(boolean disableView){
        this.disableView = disableView;
    }

    public void setSignatureFile(String signatureFile) {
        if(signatureFile!=null && !signatureFile.equals("no-image")){
            this.signatureFile = signatureFile;
        }else{
            this.signatureFile = "no-image";
        }
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setRequestUUID(Request request) {
        if (request == null) {
            return;
        }
        requestUUID = request.getUuid();
    }


    protected void setInitialStatus() {
        if (tier.equals(ApproverTiers.FACULTY.toString())) {
            this.status = FacultyApproverStatus.getFirst();
        } else if (tier.equals(ApproverTiers.DEPARTMENT.toString())) {
            this.status = DepartmentApproverStatus.getFirst();
        } else if (tier.equals(ApproverTiers.ADVISOR.toString())) {
            this.status = AdvisorApproverStatus.getFirst();
        } else {
            this.status = OtherApproverStatus.getFirst();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Approver){
            Approver approver = (Approver) obj;
            if (this.uuid.equals(approver.getUUID()))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return this.uuid.hashCode();
    }
    @Override
    public int compareTo(Approver approver) {
        return this.uuid.compareTo(approver.getUUID());
    }
    @Override
    public String toString() {
        String reqUUID = null;
        if (requestUUID == null) {
            reqUUID = "no-request";
        } else {
            reqUUID = requestUUID.toString();
        }
        return uuid.toString() + "," +
                reqUUID + "," +
                tier + "," +
                associateUUID + "," +
                role + "," +
                status + "," +
                signatureFile + "," +
                firstname + "," +
                lastname;
    }

}
