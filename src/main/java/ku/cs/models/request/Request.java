package ku.cs.models.request;

import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.request.approver.exception.ApproverException;
import ku.cs.models.user.Student;
import ku.cs.models.user.User;
import ku.cs.models.user.UserList;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.Datasource;
import ku.cs.services.RequestListFileDatasource;
import ku.cs.services.UserListFileDatasource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public Request(String uuid, String ownerUUID, String name, String nisitId, String timeStampLastUpdate, String timeStampCreateForm, String requestType, String statusNow, String statusNext, String reasonForNotApprove){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss");
        setUuid(UUID.fromString(uuid));
        setOwnerUUID(UUID.fromString(ownerUUID));
        setName(name);
        setNisitId(nisitId);
        if (timeStampCreateForm != null) {
            setDate(LocalDateTime.parse(timeStampCreateForm, formatter));
            setTimeStamp(LocalDateTime.parse(timeStampLastUpdate, formatter));
        } else {
            setTimeStamp(LocalDateTime.now());
            setDate(LocalDateTime.now());
        }
        setRequestType(requestType);
        setStatusNow(statusNow);
        setStatusNext(statusNext);
        setReasonForNotApprove(reasonForNotApprove);
        approvers = new ApproverList();
        requireTier = new HashMap<>();
        Datasource<UserList> datasource = new UserListFileDatasource("data", "student.csv");
        UserList userList = datasource.readData();
        Student owner = (Student)userList.findUserByUUID(this.ownerUUID);
        this.departmentUUID = owner.getDepartmentUUID();
        this.facultyUUID = owner.getFacultyUUID();
    }

    public Request(UUID ownerUUID, String name, String nisitId, String requestType){
        this(UUID.randomUUID().toString(), ownerUUID.toString(), name, nisitId, null, null, requestType, "ใบคำร้องใหม่", "คำร้องส่งต่อให้อาจารย์ที่ปรึกษา", "none");
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

    public void addApprover(String requestUUID, String tier, String associateUUID, String role, String firstname, String lastname) {
        try {
            approvers.addApprover(requestUUID, tier, associateUUID, role, firstname, lastname);
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
