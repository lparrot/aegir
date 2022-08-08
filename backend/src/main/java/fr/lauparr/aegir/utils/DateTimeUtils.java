package fr.lauparr.aegir.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;

@UtilityClass
public class DateTimeUtils {

  public final String PATTERN_DATE_TIME = "dd/MM/yyyy HH:mm";
  public final String PATTERN_DATE = "dd/MM/yyyy";

  public LocalDate convertToLocalDate(final String date) {
    return DateTimeUtils.convertToLocalDate(date, DateTimeUtils.PATTERN_DATE);
  }

  public LocalDate convertToLocalDate(final String date, final String pattern) {
    if (StringUtils.isBlank(date)) {
      return null;
    }
    try {
      return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    } catch (final DateTimeParseException e) {
      return null;
    }
  }

  public LocalDateTime convertToLocalDateTime(String date) {
    if (StringUtils.isBlank(date)) {
      return null;
    }
    final boolean simpleDate = date.matches("\\d{2}/\\d{2}/\\d{4}");
    if (simpleDate) {
      date = date.concat(" 00:00:00");
    }
    return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DateTimeUtils.PATTERN_DATE_TIME));
  }

  public LocalDate convertToLocalDate(final Date date) {
    final LocalDateTime dateTime = DateTimeUtils.convertToLocalDateTime(date);
    if (dateTime == null) {
      return null;
    }
    return dateTime.toLocalDate();
  }

  public LocalDateTime convertToLocalDateTime(final Date date) {
    if (date != null) {
      final Instant instant = Instant.ofEpochMilli(date.getTime());
      return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
    return null;
  }

  public Date convertLocalDateToDate(final LocalDate date) {
    return DateTimeUtils.convertLocalDateTimeToDate(date.atTime(0, 0, 0));
  }

  public Date convertLocalDateTimeToDate(final LocalDateTime date) {
    if (date != null) {
      return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }
    return null;
  }

  public String formatDate(final Temporal temporal) {
    if (temporal != null) {
      if (temporal instanceof LocalDate) {
        return ((LocalDate) temporal).format(DateTimeFormatter.ofPattern(DateTimeUtils.PATTERN_DATE));
      }
      if (temporal instanceof LocalDateTime) {
        return ((LocalDateTime) temporal).format(DateTimeFormatter.ofPattern(DateTimeUtils.PATTERN_DATE_TIME));
      }
    }
    return null;
  }

  public String formattedLocalDateTimeDuration(final LocalDateTime time) {
    return DateTimeUtils.formatLocalDateTimeDuration(time);
  }

  private String formatLocalDateTimeDuration(final LocalDateTime time) {
    if (time != null) {
      final long heures = ChronoUnit.HOURS.between(LocalDateTime.now(), time);
      final long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), time) % 60;
      final long secondes = ChronoUnit.SECONDS.between(LocalDateTime.now(), time) % 60;

      return String.format("%s %s %s %s %s %s", heures, DateTimeUtils.getPlurialString(heures, " heure"), minutes, DateTimeUtils.getPlurialString(minutes, " minute"), secondes, DateTimeUtils.getPlurialString(secondes, " seconde"));
    }
    return null;
  }

  public String formattedEllapsedTimeDuration(final long time) {
    final Duration duration = Duration.ofMillis(time);
    return DateTimeUtils.formatEllapsedTimeDuration(duration);
  }

  public int getWeekNumber(final Temporal date) {
    final TemporalField field = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
    return date.get(field);
  }

  public String getDayOfWeekLiteral(final Temporal date, final boolean abrege) {
    if (abrege) {
      return DateTimeFormatter.ofPattern("EEE").format(date);
    }
    return DateTimeFormatter.ofPattern("EEEE").format(date);
  }

  private String formatEllapsedTimeDuration(final Duration duration) {
    if (duration != null) {
      final long heures = duration.toHours();
      final long minutes = duration.toMinutes();
      final long secondes = duration.getSeconds() % 60;

      final String formattedHeures = heures > 0 ? heures + DateTimeUtils.getPlurialString(heures, " heure") : "";
      final String formattedMinutes = minutes > 0 ? minutes + DateTimeUtils.getPlurialString(minutes, " minute") : "";
      final String formattedSecondes = secondes > 0 || heures <= 0 ? secondes + DateTimeUtils.getPlurialString(secondes, " seconde") : "";

      return String.format("%s %s %s", formattedHeures, formattedMinutes, formattedSecondes);
    }
    return null;
  }

  private String getPlurialString(final long number, final String word) {
    return number > 1 ? word + "s" : word;
  }

}
