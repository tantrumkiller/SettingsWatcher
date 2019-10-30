package com.fourthlap.settingsscanner.userpreference;

import android.content.SharedPreferences;
import android.util.Log;
import com.fourthlap.settingsscanner.setting.Setting;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UserPreferencesStore {

  private static final String SLEEP_WINDOW_START_HOUR_KEY = "SleepWindowStartHour";
  private static final String SLEEP_WINDOW_START_MIN_KEY = "SleepWindowStartMinutes";
  private static final String SLEEP_WINDOW_END_HOUR_KEY = "SleepWindowEndHour";
  private static final String SLEEP_WINDOW_END_MIN_KEY = "SleepWindowEndMinutes";
  private static final String FREQUENCY_OF_SCAN_KEY = "frequencyOfScan";
  private static final String NEXT_SCAN_TIME_SET_KEY = "nextScanTime";
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
      "E MMM dd HH:mm:ss Z yyy");

  public Set<Setting> getSettingsToBeScanned(final SharedPreferences sharedPref) {
    final Set<Setting> settingsToBeScanned = new HashSet<>();

    for (Setting setting : Setting.values()) {
      boolean hasUserWhiteListed = sharedPref.getBoolean(setting.name(), true);
      if (hasUserWhiteListed) {
        settingsToBeScanned.add(setting);
      }
    }

    return Collections.unmodifiableSet(settingsToBeScanned);
  }

  public void updateSettingsToBeScannedPreferences(final SharedPreferences sharedPref,
      final Setting setting,
      boolean isWhitelistedForScan) {
    final SharedPreferences.Editor editor = sharedPref.edit();
    Log.i("UserPreferencesStore", setting + " isWhitelistedForScan: " + isWhitelistedForScan);
    editor.putBoolean(setting.name(), isWhitelistedForScan);
    editor.commit();
  }

  public void setSleepWindowStartTime(final SharedPreferences sharedPref, int hour, int minutes) {
    final SharedPreferences.Editor editor = sharedPref.edit();
    editor.putInt(SLEEP_WINDOW_START_HOUR_KEY, hour);
    editor.putInt(SLEEP_WINDOW_START_MIN_KEY, minutes);
    editor.commit();
  }

  public TimeOfTheDay getSleepWindowStartTime(final SharedPreferences sharedPref) {
    int hour = sharedPref.getInt(SLEEP_WINDOW_START_HOUR_KEY, 22);
    int minutes = sharedPref.getInt(SLEEP_WINDOW_START_MIN_KEY, 0);

    return new TimeOfTheDay(hour, minutes);
  }

  public void setSleepWindowEndTime(final SharedPreferences sharedPref, int hour, int minutes) {
    final SharedPreferences.Editor editor = sharedPref.edit();
    editor.putInt(SLEEP_WINDOW_END_HOUR_KEY, hour);
    editor.putInt(SLEEP_WINDOW_END_MIN_KEY, minutes);
    editor.commit();
  }

  public TimeOfTheDay getSleepWindowEndTime(final SharedPreferences sharedPref) {
    int hour = sharedPref.getInt(SLEEP_WINDOW_END_HOUR_KEY, 9);
    int minutes = sharedPref.getInt(SLEEP_WINDOW_END_MIN_KEY, 0);

    return new TimeOfTheDay(hour, minutes);
  }

  public void setFrequencyOfScan(final SharedPreferences sharedPref, int frequencyOfScan) {
    final SharedPreferences.Editor editor = sharedPref.edit();
    editor.putInt(FREQUENCY_OF_SCAN_KEY, frequencyOfScan);
    editor.commit();
  }

  public int getFrequencyOfScan(final SharedPreferences sharedPref) {
    return sharedPref.getInt(FREQUENCY_OF_SCAN_KEY, 3);
  }

  public void setNextScanTime(final SharedPreferences sharedPref, Date date) {
    final SharedPreferences.Editor editor = sharedPref.edit();
    editor.putString(NEXT_SCAN_TIME_SET_KEY, DATE_FORMAT.format(date));
    editor.commit();
  }

  public Date getNextScanTime(final SharedPreferences sharedPref) {
    String dateString = sharedPref.getString(NEXT_SCAN_TIME_SET_KEY, "NOT_SET");
    if("NOT_SET".equals(dateString)){
      return null;
    }

    try {
      return DATE_FORMAT.parse(dateString);
    } catch (ParseException e){
      Log.i("UserPreferencesStore", "Failed to load scan time, returning null");
      return null;
    }
  }
}
