package ku.cs.models;

public class Request {
    private String name;
    private String timeStamp;
    private String date;
    private String nisitId;
    private String requestType;
    private String statusNow;
    private String statusNext;

    public Request(String name, String timeStamp, String date, String nisitId, String requestType, String statusNow, String statusNext){
        this.name = name;
        this.timeStamp = timeStamp;
        this.date = date;
        this.nisitId = nisitId;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
