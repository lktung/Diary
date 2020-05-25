package com.example.diary.Model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class DiaryDetail implements Serializable {
    private String id_;
    private int color_;
    private String title_;
    private String content_;
    private Date dateOfCreation_;
    private String dateOccurrence_;
    private String timeOccurrence_;

    public String getDateOccurrence_() {
        return dateOccurrence_;
    }

    public void setDateOccurrence_(String dateOccurrence_) {
        this.dateOccurrence_ = dateOccurrence_;
    }

    public String getTimeOccurrence() {
        return timeOccurrence_;
    }

    public void setTimeOccurrence(String timeOccurrence) {
        this.timeOccurrence_ = timeOccurrence;
    }

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public int getColor_() {
        return color_;
    }

    public void setColor_(int color_) {
        this.color_ = color_;
    }

    public String getContent_() {
        return content_;
    }

    public void setContent_(String content_) {
        this.content_ = content_;
    }

    public Date getDateOfCreation_() {
        return dateOfCreation_;
    }

    public void setDateOfCreation_(Date dateOfCreation_) {
        this.dateOfCreation_ = dateOfCreation_;
    }

    public String getTitle_() {
        return title_;
    }

    public void setTitle_(String title_) {
        this.title_ = title_;
    }

    public DiaryDetail(){}
    public DiaryDetail(String id_, int color_, String title_, String content_, Date dateOfCreation_, String dateOccurrence_, String timeOccurrence_) {
        this.id_ = id_;
        this.color_ = color_;
        this.title_ = title_;
        this.content_ = content_;
        this.dateOfCreation_ = dateOfCreation_;
        this.dateOccurrence_ = dateOccurrence_;
        this.timeOccurrence_ = timeOccurrence_;
    }

    public static String countDate(Date date){
        String[] month = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};
        String datediff = "";
        Calendar currentCal = Calendar.getInstance();
        Calendar itemCal = Calendar.getInstance();
        itemCal.set(Calendar.HOUR_OF_DAY, 1);
        itemCal.set(Calendar.MINUTE, 1);
        itemCal.set(Calendar.SECOND, 1);
        Date currentDate = itemCal.getTime();
        itemCal.setTime(date);
        itemCal.set(Calendar.HOUR_OF_DAY, 1);
        itemCal.set(Calendar.MINUTE, 1);
        itemCal.set(Calendar.SECOND, 1);
        Date thatdate = itemCal.getTime();
        long timediff = currentDate.getTime() - thatdate.getTime();
        float daydiff = Math.round(((float) timediff / (1000 * 60 * 60 * 24)) * 10) / 10;
        if (daydiff == 0.0) {
            datediff += "Today";
        } else if (daydiff > 0 && daydiff < 7) {
            datediff += (int) Math.ceil(daydiff) + " days ago";
        } else {
            datediff += month[itemCal.get(Calendar.MONTH)] + " " + itemCal.get(Calendar.DATE);
            if (!(currentCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR))) {
                datediff += " " + itemCal.get(Calendar.YEAR);
            }
        }
        return datediff;
    }
}
