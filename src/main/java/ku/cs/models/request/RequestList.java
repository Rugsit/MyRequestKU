package ku.cs.models.request;

import java.util.ArrayList;
import java.util.Arrays;

public class RequestList {
    ArrayList<Request> requests;

    public RequestList() {
        requests = new ArrayList<>();
    }

    public void addRequest(String[] data) {
        Request request;
        switch (data[0]) {
            case "Register":
                request = new RegisterRequestForm(data); break;
            case "General":
                request = new GeneralRequestForm(data); break;
            case "AcademicLeave":
                String[] first = Arrays.copyOfRange(data, 0, 20);
                String[] second = Arrays.copyOfRange(data, 20, data.length);
                request = new AcademicLeaveRequestForm(first, second); break;
            case "KU1":
                String[] firstKU1 = Arrays.copyOfRange(data, 0, 14);
                String[] secondKU1 = Arrays.copyOfRange(data, 14, data.length);
                request = new Ku1AndKu3RequestForm(firstKU1, secondKU1, (byte)1); break;
            case "KU3":
                String[] firstKU3 = Arrays.copyOfRange(data, 0, 14);
                String[] secondKU3 = Arrays.copyOfRange(data, 14, data.length);
                request = new Ku1AndKu3RequestForm(firstKU3, secondKU3, (byte)3);break;
            default:
                request = null;
        }
        requests.add(request);
    }

    public ArrayList<Request> getRequests() {
        return this.requests;
    }


}
