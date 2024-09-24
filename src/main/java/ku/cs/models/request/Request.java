package ku.cs.models.request;

import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.request.approver.exception.ApproverException;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.Datasource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class Request {
    private UUID uuid;
    private UUID ownerUUID;
    private UUID departmentUUID;
    private UUID facultyUUID;
    private String name;
    private String nisitId;
    private LocalDateTime timeStampLastUpdate;
    private LocalDateTime timeStampCreateForm;
    private String requestType;
    private String statusNow;
    private String statusNext;
    private ApproverList approvers;
    private HashMap<String, HashMap<String, Integer>> requireTier;
    private String reasonForNotApprove;


    public Request() {}

    public Request(UUID uuid, UUID ownerUUID, String name, String nisitId, LocalDateTime timeStampLastUpdate, LocalDateTime timeStampCreateForm, String requestType, String statusNow, String statusNext, String reasonForNotApprove){
//        User user = null;
//        String[] nameArray = name.split(" ");
//        try {
////            System.out.println(nameArray[0]+nameArray[1]);
//            user = new User("6610402230","b6610402230","student",nameArray[0],nameArray[1],"2004-11-29","sirisuk.t@ku.th","123456789");
//            this.name = user.gettName();
//            this.nisitId = user.getID();
//        } catch (UserException e){
//            e.printStackTrace();
//        }
        this.uuid = uuid;
        this.ownerUUID = ownerUUID;

        this.name = name;
        this.nisitId = nisitId;
        this.timeStampLastUpdate = timeStampLastUpdate;
        this.timeStampCreateForm = timeStampCreateForm;
        this.requestType = requestType;
        this.statusNow = statusNow;
        this.statusNext = statusNext;
        this.reasonForNotApprove = reasonForNotApprove;
        approvers = new ApproverList();
        requireTier = new HashMap<>();
    }

    public Request(String name, LocalDateTime timeStamp, LocalDateTime date, String nisitId, String requestType, String statusNow, String statusNext){
//        User user = null;
//        String[] nameArray = name.split(" ");
//        try {
////            System.out.println(nameArray[0]+nameArray[1]);
//            user = new User("6610402230","b6610402230","student",nameArray[0],nameArray[1],"2004-11-29","sirisuk.t@ku.th","123456789");
//            this.name = user.getName();
//            this.nisitId = user.getID();
//        } catch (UserException e){
//            e.printStackTrace();
//        }

        this.name = name;
        this.nisitId = nisitId;
        this.timeStampLastUpdate = timeStamp;
        this.timeStampCreateForm = date;
        this.requestType = requestType;
        this.statusNow = statusNow;
        this.statusNext = statusNext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimeStamp() {
        return timeStampLastUpdate;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStampLastUpdate = timeStamp;
    }

    public LocalDateTime getDate() {
        return timeStampCreateForm;
    }

    public void setDate(LocalDateTime date) {
        this.timeStampCreateForm = date;
    }

    public String getNisitId() {
        return nisitId;
    }

    public void setNisitId(String nisitId) {
        this.nisitId = nisitId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getStatusNow() {
        return statusNow;
    }

    public void setStatusNow(String statusNow) {
        this.statusNow = statusNow;
    }

    public String getStatusNext() {
        return statusNext;
    }

    public void setStatusNext(String statusNext) {
        this.statusNext = statusNext;
    }

    public UUID getUuid() {return uuid;}

    public UUID getOwnerUUID() {return ownerUUID;}

    public void setUuid(UUID uuid) {this.uuid = uuid;}

    public void setOwnerUUID(UUID ownerUUID) {this.ownerUUID = ownerUUID;}

    public UUID getDepartmentUUID() {
        return departmentUUID;
    }

    public void setDepartmentUUID(UUID departmentUUID) {
        this.departmentUUID = departmentUUID;
    }

    public UUID getFacultyUUID() {
        return facultyUUID;
    }

    public void setFacultyUUID(UUID facultyUUID) {
        this.facultyUUID = facultyUUID;
    }

    public String getReasonForNotApprove() {
        return reasonForNotApprove;
    }

    public void setReasonForNotApprove(String reasonForNotApprove) {
        reasonForNotApprove = reasonForNotApprove.trim();
        if (reasonForNotApprove.isEmpty()) {
            throw new IllegalArgumentException("กรุณาระบุเหตุผลที่ไม่อนุมัติคำร้อง");
        }
        this.reasonForNotApprove = reasonForNotApprove;
    }

    public HashMap<String, HashMap<String, Integer>> getRequireTier() {
        Datasource<ApproverList> approverListDatasource = new ApproverListFileDatasource("request");
        ApproverList approverList = approverListDatasource.readData();
        for (Approver approver : approverList.getApprovers()) {
            if (approver.getRequestUUID().equals(uuid)) {
                updateCountInRequireTier(approver.getTier(), approver.getRole());
            }
        }
        return requireTier;
    }

    private void updateCountInRequireTier(String tier, String role) {
        if (!requireTier.containsKey(tier)) {
            HashMap<String, Integer> newTier = new HashMap<>();
            newTier.put(role, 1);
            requireTier.put(tier, newTier);
        } else {
            if (requireTier.get(tier).containsKey(role)) {
                requireTier.get(tier).put(role, requireTier.get(tier).get(role) + 1);
            } else {
                requireTier.get(tier).put(role, 1);
            }
        }
    }

    public void addApprover(String requestUUID, String tier, String role, String firstname, String lastname) {
        try {
            approvers.addApprover(requestUUID, tier, role, firstname, lastname);
        } catch (ApproverException e) {
            throw new RuntimeException(e);
        }
    }

    public ApproverList getApprovers() {
        return approvers;
    }

    public ApproverList getApproverList() {
        return approvers.getApproverList(uuid);
    }
}
