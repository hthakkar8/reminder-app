package com.example.suyog.locationtracker;

import java.util.Date;

/**
 * Created by SUYOG on 9/9/2017.
 */

public class Reminder
{
    String key;
    String reminderName;

    String reminderStartTime;
    String reminderEndTime;
    String placename;
    String placeaddress;
    Double latitude;
    Double logitude;

    public Reminder(String key,String reminderName, String reminderStartTime,String reminderEndTime ,String placename, String placeaddress, Double latitude, Double logitude) {
        this.key = key;
        this.reminderName = reminderName;
        this.reminderStartTime = reminderStartTime;
        this.reminderEndTime = reminderEndTime;
        this.placename = placename;
        this.placeaddress = placeaddress;
        this.latitude = latitude;
        this.logitude = logitude;
    }

    public Reminder() {
    }

    public String getKey() {
        return key;
    }

    public String getReminderName() {
        return reminderName;
    }



    public String getReminderStartTime() {
        return reminderStartTime;
    }

    public String getReminderEndTime() {
        return reminderEndTime;
    }

    public String getPlacename() {
        return placename;
    }

    public String getPlaceaddress() {
        return placeaddress;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLogitude() {
        return logitude;
    }
}
