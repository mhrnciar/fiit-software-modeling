package auction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Datetime {
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;
    String timezone;

    /**
     * Constructor from separate parts with timezone
     * @param year year
     * @param month month
     * @param day day
     * @param hour hour
     * @param minute minute
     * @param second second
     * @param timezone timezone
     */
    public Datetime(int year, int month, int day, int hour, int minute, int second, String timezone) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.timezone = timezone;
    }

    /**
     * Constructor from separate parts without timezone
     * @param year year
     * @param month month
     * @param day day
     * @param hour hour
     * @param minute minute
     * @param second second
     */
    public Datetime(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    /**
     * Constructor from string
     * @param datetime string in format "yyyy-mm-dd HH:MM:SS"
     * @throws ParseException parsing error
     */
    public Datetime(String datetime) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);
        this.day = cal.get(Calendar.DAY_OF_MONTH);
        this.hour = cal.get(Calendar.HOUR);
        this.minute = cal.get(Calendar.MINUTE);
        this.second = cal.get(Calendar.SECOND);
    }

    /**
     * Constructor from java Date object
     * @param datetime date object
     */
    public Datetime(Date datetime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(datetime);
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH);
        this.day = cal.get(Calendar.DAY_OF_MONTH);
        this.hour = cal.get(Calendar.HOUR);
        this.minute = cal.get(Calendar.MINUTE);
        this.second = cal.get(Calendar.SECOND);
    }

    public String getDateString() {
        String datetime = "" + year;
        if (month < 10) {
            datetime += "-0" + month;
        }
        else {
            datetime += "-" + month;
        }
        if (day < 10) {
            datetime += "-0" + day;
        }
        else {
            datetime += "-" + day;
        }
        if (hour < 10) {
            datetime += " 0" + hour;
        }
        else {
            datetime += " " + hour;
        }
        if (minute < 10) {
            datetime += ":0" + minute;
        }
        else {
            datetime += ":" + minute;
        }
        if (second < 10) {
            datetime += ":0" + second;
        }
        else {
            datetime += ":" + second;
        }
        return datetime;
    }

    public boolean isLower(Datetime d) {
        if (this.year < d.year) {
            return true;
        } else if (this.year > d.year || this.month < d.month) {
            return true;
        } else if (this.month > d.month || this.day < d.day) {
            return true;
        }
        return false;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public String getTimezone() {
        return timezone;
    }
}
