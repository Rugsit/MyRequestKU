package ku.cs.models.request.approver;

import ku.cs.models.request.Request;
import ku.cs.models.request.approver.exception.ApproverException;
import ku.cs.models.request.approver.exception.ApproverRoleException;
import ku.cs.models.request.approver.exception.ApproverStatusException;
import ku.cs.models.request.approver.exception.ApproverTierException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;

public abstract class Approver implements Comparable<Approver> {
    public static final ArrayList<String> AVAILABLE_TIER = new ArrayList<>(Arrays.asList(
            "advisor",
            "department",
            "faculty",
            "other"
    ));
    protected final UUID uuid;
    protected UUID requestUUID;
    protected String firstname;
    protected String lastname;
    protected final String tier;
    protected String role;
    protected String status;
    protected String signatureFile;
    private boolean disableView = false;


    public Approver(String requestUUID, String tier, String role, String firstname, String lastname) throws ApproverException {
        this(UUID.randomUUID().toString(), requestUUID, tier, role, "no-status", "no-signature", firstname, lastname);
        setInitialStatus();
    }

    public Approver(String tier, String role, String firstname, String lastname) throws ApproverException {
        this(UUID.randomUUID().toString(), null, tier, role, "no-status", "no-signature", firstname, lastname);
        setInitialStatus();
    }

    public Approver(String uuid, String requestUUID, String tier, String role, String status, String signatureFile, String firstname, String lastname) throws ApproverException {
        if (uuid == null) throw new ApproverException("uuid must not be null");
        if (uuid.isEmpty()) throw new ApproverException("uuid must not be empty");
        this.uuid = UUID.fromString(uuid);
//        if (requestUUID == null) throw new ApproverException("requestUUID must not be null");
        if (requestUUID != null && !requestUUID.equals("no-request")) {
            if (requestUUID.isEmpty()) throw new ApproverException("requestUUID must not be empty");
            this.requestUUID = UUID.fromString(requestUUID);
        }
        //Tier Checker
        if (tier == null) throw new ApproverTierException("tier must not be null");
        if (tier.isEmpty()) throw new ApproverTierException("tier must not be empty");
        if (AVAILABLE_TIER.contains(tier)) this.tier = tier;
        else throw new ApproverTierException("Not Available Tier : " + tier);

        if (role == null) throw new ApproverRoleException("role must not be null");
        if (role.isEmpty()) throw new ApproverRoleException("role must not be empty");
        this.role = role;
        this.status = status;
        this.signatureFile = signatureFile;
        this.firstname = firstname;
        this.lastname = lastname;
    }
    //Comparator
    public static Comparator<Approver> requestUUIDComparator = new Comparator<>() {
        public int compare(Approver a1, Approver a2) {
            return a1.getRequestUUID().compareTo(a2.getRequestUUID());
        }
    };

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
        this.status = status;
    }
    public void setDisableView(boolean disableView){
        this.disableView = disableView;
    }

    public void setSignatureFile(String signatureFile) {
        if(signatureFile!=null){
            this.signatureFile = signatureFile;
        }else{
            this.signatureFile = "no-signature";
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


    protected abstract void setInitialStatus();

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
                role + "," +
                status + "," +
                signatureFile + "," +
                firstname + "," +
                lastname;
    }

}
