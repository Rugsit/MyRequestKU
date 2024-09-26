package ku.cs.models.request.approver;

import ku.cs.models.request.approver.exception.ApproverException;
import ku.cs.models.request.approver.exception.ApproverStatusException;

import java.util.ArrayList;
import java.util.Arrays;

public class AdvisorApprover extends Approver{
    public static final ArrayList<String> AVAILABLE_STATUS = new ArrayList<>(Arrays.asList(
            "รออาจารย์ที่ปรึกษา",
            "เรียบร้อย",
            "ไม่อนุมัติ"
    ));

    public AdvisorApprover(String requestUUID, String tier, String associateUUID, String role, String firstname, String lastname) throws ApproverException {
        super(requestUUID, tier, associateUUID, role, firstname, lastname);
    }

    public AdvisorApprover(String tier, String associateUUID, String role, String firstname, String lastname) throws ApproverException {
        super(tier, associateUUID, role, firstname, lastname);
    }

    public AdvisorApprover(String uuid, String requestUUID, String tier, String associateUUID, String role, String status, String signatureFile, String firstname, String lastname) throws ApproverException {
        super(uuid, requestUUID, tier, associateUUID, role, status, signatureFile, firstname, lastname);
    }

    @Override
    public void setStatus(String status) throws ApproverStatusException {
        if(AVAILABLE_STATUS.contains(status)){
           super.setStatus(status);
        }else{
            throw new ApproverStatusException("Advisor approver status node valid");
        }
    }
    @Override
    protected void setInitialStatus() {
        try {
            setStatus(AVAILABLE_STATUS.get(0));
        } catch (ApproverStatusException e) {
            throw new RuntimeException(e);
        }
    }


}



