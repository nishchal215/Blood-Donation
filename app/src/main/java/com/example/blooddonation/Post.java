package com.example.blooddonation;

import com.google.firebase.storage.StorageTask;

import java.util.HashMap;
import java.util.Map;

public class Post {

    private String bloodGroup,location,description,publisher;
    private String ts;
    Map<String,String> timeStamp;


    public Post(){

    }

    public Post(String bloodGroup, String location, String description, Map<String,String> timeStamp, String publisher) {
        this.bloodGroup = bloodGroup;
        this.location = location;
        this.description = description;
        this.timeStamp = timeStamp;
        this.publisher = publisher;
    }

    public Post(String bloodGroup, String description, String publisher, String timeStamp, String location) {
        this.bloodGroup = bloodGroup;
        this.location = location;
        this.description = description;
        this.ts = timeStamp;
        this.publisher = publisher;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Map<String,String> timeStamp) {
        this.timeStamp = timeStamp;
    }


    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Map<String, Object> toMap() {

        HashMap<String,Object> map = new HashMap<>();
        map.put("Blood group", bloodGroup);
        map.put("Location", location);
        map.put("Description", description);
        map.put("TimeStamp",timeStamp);
        map.put("Publisher",publisher);
        return map;

    }
}
