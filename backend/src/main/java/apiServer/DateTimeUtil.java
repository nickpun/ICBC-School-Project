package apiServer;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    // parse the string date from JSON request

    public static Timestamp getTimestamp(String utcDateTime) {
        System.out.println(utcDateTime);
        LocalDateTime localDateTime = LocalDateTime.parse(utcDateTime, DateTimeFormatter.ISO_DATE_TIME);
        System.out.println(Timestamp.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant()));
        return Timestamp.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
    }


    // used to send response in JSON

    public static String getUTCString(Timestamp dbTimestamp) {
        return dbTimestamp.toInstant().atZone(ZoneId.of("UTC")).toString();
    }

}

//https://stackoverflow.com/questions/58772226/saving-java-sql-timestamp-to-mysql-datetime-column-changes-the-time