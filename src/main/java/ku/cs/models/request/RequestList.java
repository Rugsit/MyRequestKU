package ku.cs.models.request;

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

    public void addRequest(String[] data, String[] subject, byte type) {
        Request request = switch (data[0]) {
            case "KU1", "KU3" -> new Ku1AndKu3RequestForm(data, subject, type);
            default -> null;
        };
        requests.add(request);
    }


}
