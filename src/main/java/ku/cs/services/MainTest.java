package ku.cs.services;

import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.approver.Approver;
import ku.cs.models.request.approver.ApproverList;
import ku.cs.models.request.approver.DepartmentApprover;
import ku.cs.models.request.approver.FacultyApprover;
import ku.cs.models.request.approver.exception.ApproverException;
import ku.cs.services.ApproverListFileDatasource;
import ku.cs.services.RequestListFileDatasource;

public class MainTest {
    public static void main(String[] args) throws ApproverException {
        ApproverListFileDatasource approverDatasource = new ApproverListFileDatasource("approver");
        ApproverListFileDatasource datasource = new ApproverListFileDatasource("request");
        ApproverList approverList = new ApproverList();
        RequestListFileDatasource requestDatasource = new RequestListFileDatasource("data");
        RequestList requestList = requestDatasource.readData();
        Request request = requestList.getRequests().get(1);
        System.out.println("Data is : " + request);
        FacultyApprover FApprover = new FacultyApprover("21eb3fb8-3a83-4353-a833-c39dd43f73a6","no-request","faculty","aea023cf-9e5d-4a5c-8d57-2cfbfb291900","หัวหน้าภาควิชาวิทยาการคอมพิวเตอร์","รออัพโหลด","no-signature","สมโชค","สมโชค");
        approverList.addApprover("department","aea023cf-9e5d-4a5c-8d57-2cfbfb291900", "หัวหน้าภาควิชาวิทยาการคอมพิวเตอร์", "สมโชค", "สมโชค");
        approverList.addApprover("department","aea023cf-9e5d-4a5c-8d57-2cfbfb291900", "อาจารย์ที่ปรึกษา", "อุษา", "สัมมาพันธ์");
        approverDatasource.writeData(approverList);
        approverDatasource.appendData(FApprover, "approver");
        DepartmentApprover departmentApprover = new DepartmentApprover("department",",aea023cf-9e5d-4a5c-8d57-2cfbfb291900", "อาจารย์ที่ปรึกษา", "ธรรมกร", "แซ่ตั้ง");
        approverDatasource.appendData(departmentApprover, "approver");
        approverDatasource.appendDataFromList(FApprover, request);

        approverList = approverDatasource.query("faculty", request);
        for (Approver approver : approverList.getApprovers()) {
            System.out.println(approver);
        }
    }
}