package ku.cs.models.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class RequestList {
    private ArrayList<Request> requests;

    public RequestList() {
        requests = new ArrayList<>();
    }

    public void addRequest(String[] data) {
        Request request;
        switch (data[0]) {
            case "ลงทะเบียนเรียน":
                request = new RegisterRequestForm(data);
                break;
            case "ทั่วไป":
                request = new GeneralRequestForm(data); break;
            case "ลาพักการศึกษา":
                String[] first = Arrays.copyOfRange(data, 0, 21);
                String[] second = Arrays.copyOfRange(data, 21, data.length);
                request = new AcademicLeaveRequestForm(first, second); break;
            case "KU1":
                String[] firstKU1 = Arrays.copyOfRange(data, 0, 15);
                String[] secondKU1 = Arrays.copyOfRange(data, 15, data.length);
                request = new Ku1AndKu3RequestForm(firstKU1, secondKU1, (byte)1); break;
            case "KU3":
                String[] firstKU3 = Arrays.copyOfRange(data, 0, 15);
                String[] secondKU3 = Arrays.copyOfRange(data, 15, data.length);
                request = new Ku1AndKu3RequestForm(firstKU3, secondKU3, (byte)3);break;
            default:
                request = null;
        }
        requests.add(request);
    }

    public ArrayList<Request> getRequests() {
        return this.requests;
    }

    public void addRequest(Request request) {
        this.requests.add(request);
    }

    public Request findByRequestUUID(UUID requestUUID) {
     for (Request request : requests) {
         if (request.getUuid().equals(requestUUID)) {
             return request;
         }
     }
     return null;
    }


}
