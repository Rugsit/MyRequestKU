package ku.cs.models;

import java.util.ArrayList;

public class RequestList {
    ArrayList<Request> requests;

    public RequestList() {
        requests = new ArrayList<>();
    }

    public void addRequest(String[] data) {
        Request request = switch (data[0]) {
            case "Register" -> new RegisterRequestForm(data);
            case "General" -> new GeneralRequestForm(data);
            default -> null;
        };
        requests.add(request);
    }

    public void addRequest(String[] data, String[] subject) {
        Request request = switch (data[0]) {
            case "KU1" -> new Ku1RequestForm(data, subject);
            default -> null;
        };
        requests.add(request);
    }


}
