import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;

public class TimeClock {
    public void TimeClock() {
        // Set the formatter for date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (true) {
            // Get the current date and time
            LocalDate currentDate = LocalDate.now();
            LocalDateTime currentDateTime = LocalDateTime.now();
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            // Mapping the days of the week to names
            String[] weekdays = {"", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

            // Format the current date and time
            String formattedDateTime = currentDateTime.format(formatter);

            // Clear the previous output and reprint
            System.out.print("\r" + formattedDateTime + " (" + weekdays[dayOfWeek.getValue()] + ") (GMT)");

            try {
                // Wait for 1 second before updating the time again
                Thread.sleep(1000); // 1000 milliseconds (1 second)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
