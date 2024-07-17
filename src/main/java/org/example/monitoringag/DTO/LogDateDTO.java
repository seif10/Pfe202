package org.example.monitoringag.DTO;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class LogDateDTO {
    private Date date;

    // Getter and Setter
    public Date getDate() {
        return date;
    }

    public void setDate(String dateString) {
        this.date = date;
    }

    public String getTransformedDate() {
        SimpleDateFormat outputFormatter = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.FRENCH);
        return outputFormatter.format(date);
    }
}
