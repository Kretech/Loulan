package net.romatic.com.carbon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.romatic.com.ShouldToJson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhrlnt@gmail.com
 */
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = CarbonSerializer.class)
@JsonDeserialize(using = CarbonUnSerializer.class)
public class Carbon implements ShouldToJson {

    protected static Pattern patternDate = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
    protected static Pattern patternTime = Pattern.compile("\\d{2}:\\d{2}(:\\d{2})?");
    private LocalDate date;
    private LocalTime time;

    public static Carbon of(LocalDateTime localDateTime) {
        return of(localDateTime.toLocalDate(), localDateTime.toLocalTime());
    }

    public static Carbon of(LocalDate localDate, LocalTime localTime) {
        Carbon carbon = new Carbon();
        carbon.date = localDate;
        carbon.time = localTime;
        return carbon;
    }

    public static Carbon of(String dateString) {

        String date = getStringByPattern(patternDate, dateString);
        String[] dates = date.split("-");

        String time = getStringByPattern(patternTime, dateString);

        //  LocalDate 只支持 01-02 ，需要兼容 1-2
        LocalDate localDate = LocalDate.of(Integer.valueOf(dates[0]), Integer.valueOf(dates[1]), Integer.valueOf(dates[2]));
        LocalTime localTime = LocalTime.of(0, 0, 0);
        if (time != null) {
            localTime = LocalTime.parse(time);
        }

        return Carbon.of(localDate, localTime);
    }

    public static Carbon of(int year, int month, int day) {
        return of(LocalDate.of(year, month, day), LocalTime.of(0, 0, 0));
    }

    public static Carbon of(int year, int month, int day, int hour, int minute) {
        return of(LocalDate.of(year, month, day), LocalTime.of(hour, minute));
    }

    public static Carbon of(int year, int month, int day, int hour, int minute, int sec) {
        return of(LocalDate.of(year, month, day), LocalTime.of(hour, minute, sec));
    }

    private static String getStringByPattern(Pattern pattern, String time) {
        Matcher matcher = pattern.matcher(time);
        if (matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }

    @Override
    public String toString() {
        return date.toString() + " " + time.toString();
    }

    public String toDateString() {
        return date.toString();
    }

    public LocalDateTime toDateTime() {
        return LocalDateTime.of(date, time);
    }

    public LocalDate toDate() {
        return date;
    }

    public Carbon copy() {
        return Carbon.of(date, time);
    }

    @Override
    public boolean equals(Object obj) {
        return date.equals(((Carbon) obj).date) && time.equals(((Carbon) obj).time);
    }
}
