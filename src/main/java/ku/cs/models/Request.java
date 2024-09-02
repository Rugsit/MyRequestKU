package ku.cs.models;

import java.util.UUID;

public class Request {
    private UUID uuid;
    private UUID ownerUUID;
    private String name;
    private String nisitId;
    private String timeStamp;
    private String date;
    private String requestType;
    private String statusNow;
    private String statusNext;

    public Request() {}

    public Request(String name, String timeStamp, String date, String nisitId, String requestType, String statusNow, String statusNext){
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
        this.timeStamp = timeStamp;
        this.date = date;
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

    public UUID getUuid() {return uuid;}

    public UUID getOwnerUUID() {return ownerUUID;}

    public void setUuid(UUID uuid) {this.uuid = uuid;}

    public void setOwnerUUID(UUID ownerUUID) {this.ownerUUID = ownerUUID;}
}
