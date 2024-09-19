package ku.cs.models.request.approver;

import ku.cs.models.request.approver.exception.ApproverException;
import ku.cs.models.request.approver.exception.ApproverStatusException;

import java.util.ArrayList;
import java.util.Arrays;

public class OtherApprover extends Approver{
    public static final ArrayList<String> AVAILABLE_STATUS = new ArrayList<>(Arrays.asList(
            "รออัพโหลด",
            "เรียบร้อย",
            "ไม่อนุมัติ"
    ));

    public OtherApprover(String requestUUID, String tier, String role, String firstname, String lastname) throws ApproverException {
        super(requestUUID, tier, role, firstname, lastname);
    }

    public OtherApprover(String uuid, String requestUUID, String tier, String role, String status, String signatureFile, String firstname, String lastname) throws ApproverException {
        super(uuid, requestUUID, tier, role, status, signatureFile, firstname, lastname);
    }


    @Override
    public void setStatus(String status) throws ApproverStatusException {
        if(AVAILABLE_STATUS.contains(status)){
            super.setStatus(status);
        }else{
            throw new ApproverStatusException("Other approver status not valid");
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
