package map.social_network.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
public class Utils {
    public java.sql.Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        Date utilDate = Date.from(instant);
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        return sqlDate;
    }

    public LocalDateTime dateToLocalDateTime(Date date) {

        Instant instant = Instant.ofEpochMilli(date.getTime());

        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime;
    }

    public LocalDateTime stringToLocalDateTime(String month) {
        month = "2000-" + month + "-01";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        YearMonth yearMonth = YearMonth.parse(month, formatter);

        return yearMonth.atDay(1).atStartOfDay();
    }
}
