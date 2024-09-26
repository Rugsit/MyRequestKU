package ku.cs.models.request.approver;

import ku.cs.models.request.approver.exception.ApproverException;
import ku.cs.models.request.approver.exception.ApproverTierException;

import java.util.*;

public class ApproverList {
    private HashMap<String, ArrayList<Approver>> approvers;
    public ApproverList() {
        approvers = new HashMap<>();
        for(String tier : Approver.AVAILABLE_TIER){
            approvers.put(tier, new ArrayList<>());
        }
    }
    //CONSTRUCTOR FOR NEW APPROVER
    public void addApprover(String requestUUID,String tier, String associateUUID, String role, String firstname, String lastname) throws ApproverException {
        Approver approver;
        switch (tier){
            case "advisor":
                approver = new AdvisorApprover(requestUUID,tier,associateUUID,role,firstname,lastname);
                break;
            case "department":
                approver = new DepartmentApprover(requestUUID,tier,associateUUID,role,firstname,lastname);
                break;
            case "faculty":
                approver = new FacultyApprover(requestUUID,tier,associateUUID,role,firstname,lastname);
                break;
            case "other":
                approver = new OtherApprover(requestUUID,tier,associateUUID,role,firstname,lastname);
                break;
            default:
                throw new ApproverTierException("Invalid approver tier");
        }
        approvers.get(tier).add(approver);
    }
    //CONSTRUCTOR FOR DATASOURCE
    public void addApprover(String uuid, String requestUUID, String tier, String associateUUID, String role, String status,String signatureFile, String firstname, String lastname) throws ApproverException {
        Approver approver;
        switch (tier){
            case "advisor":
                approver = new AdvisorApprover(uuid,requestUUID,tier,associateUUID,role,status,signatureFile,firstname,lastname);
                break;
            case "department":
                approver = new DepartmentApprover(uuid,requestUUID,tier,associateUUID,role,status,signatureFile,firstname,lastname);
                break;
            case "faculty":
                approver = new FacultyApprover(uuid,requestUUID,tier,associateUUID,role,status,signatureFile,firstname,lastname);
                break;
            case "other":
                approver = new OtherApprover(uuid,requestUUID,tier,associateUUID,role,status,signatureFile,firstname,lastname);
                break;
            default:
                throw new ApproverTierException("Invalid approver tier");
        }
        approvers.get(tier).add(approver);
    }

    // CONSTRUCTOR FOR ADDING NEW APPROVER TO APPROVER-LIST
    public void addApprover(String tier, String associateUUID, String role, String firstname, String lastname) throws ApproverException {
        Approver approver;
        switch (tier){
            case "advisor":
                approver = new AdvisorApprover(tier,associateUUID,role,firstname,lastname);
                break;
            case "department":
                approver = new DepartmentApprover(tier,associateUUID,role,firstname,lastname);
                break;
            case "faculty":
                approver = new FacultyApprover(tier,associateUUID,role,firstname,lastname);
                break;
            case "other":
                approver = new OtherApprover(tier,associateUUID,role,firstname,lastname);
                break;
            default:
                throw new ApproverTierException("Invalid approver tier");
        }
        approvers.get(tier).add(approver);
    }
    public void addApprover(Approver approver){
        if(approver != null){
            if(!haveApprover(approver)){
                approvers.get(approver.getTier()).add(approver);
            }
        }
    }

    public boolean haveApprover(Approver approver){
        boolean found = false;
        for(ArrayList<Approver> list : approvers.values()){
            if(list.contains(approver)){
                found = true;
            }
        }
        return found;
    }
    public Approver findApproverByObject(Approver queryApprover){
        if(queryApprover != null){
            for(ArrayList<Approver> list : approvers.values()){
                Collections.sort(list);
                int low = 0;
                int high = list.size()-1;
                while(low <= high){
                    int mid = low + (high - low) / 2;
                    if(list.get(mid).compareTo(queryApprover) == 0){
                        return list.get(mid);
                    }
                    // If query greater, ignore left half
                    if (list.get(mid).compareTo(queryApprover) < 0)
                        low = mid + 1;

                        // If query is smaller, ignore right half
                    else
                        high = mid - 1;
                }
            }
        }
        return null;

    }

    public void deleteApproverByObject(Approver approver){
        if(approver != null && haveApprover(approver)){
            try{
                approvers.get(approver.getRole()).remove(approver);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public Collection<Approver> getApprovers(){
        ArrayList<Approver> list = new ArrayList<>();
        for(ArrayList<Approver> tierList : approvers.values()){
            list.addAll(tierList);
        }
        return list;
    }
    public Collection<Approver> getApprovers(String tier){
        if(!Approver.AVAILABLE_TIER.contains(tier))return  null;
        return approvers.get(tier);
    }
    public ApproverList getApproverList(String tier){
        if(!Approver.AVAILABLE_TIER.contains(tier))return  null;
        ApproverList newApproverList = new ApproverList();
        newApproverList.getApprovers(tier).addAll(approvers.get(tier));
        return newApproverList;
    }
    public ApproverList getApproverList(UUID requestUUID){
        ApproverList newApproverList = new ApproverList();
        for(ArrayList<Approver> list : approvers.values()){
            for(Approver approver : list){
                if(approver.getRequestUUID().compareTo(requestUUID) == 0){
                    newApproverList.addApprover(approver);
                }
            }
        }
        return newApproverList;
    }
    public void concatenate(ApproverList approverList){
        for(String tier : approvers.keySet()){
            approvers.get(tier).addAll(approverList.approvers.get(tier));
        }
    }

    @Override
    public String toString() {
        return approvers.toString();
    }
}
