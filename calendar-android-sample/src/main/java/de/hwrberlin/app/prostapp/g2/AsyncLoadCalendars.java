package de.hwrberlin.app.prostapp.g2;

import com.google.api.services.calendar.model.Calendar;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Originally asynchronously loads all calendars with a progress dialog.
 * Now only primary calendar will be loaded.
 * 
 * Customized by Michael and Pascal
 * 
 */
class AsyncLoadCalendars extends AsyncTask<Void, Void, Void> {

  private final CalendarSample calendarSample;
  private final ProgressDialog dialog;
  private com.google.api.services.calendar.Calendar client;

  AsyncLoadCalendars(CalendarSample calendarSample) {
    this.calendarSample = calendarSample;
    client = calendarSample.client;
    dialog = new ProgressDialog(calendarSample);
  }

  @Override
  protected void onPreExecute() {
    dialog.setMessage("Loading calendars...");
    dialog.show();
  }

  @Override
  protected Void doInBackground(Void... arg0) {
    try {
      calendarSample.calendars.clear();
      
//      com.google.api.services.calendar.Calendar.CalendarList.List list =
//          client.calendarList().list();
//      list.setFields("items");
//      CalendarList feed = list.execute();
//      if (feed.getItems() != null) {
//        for (CalendarListEntry calendar : feed.getItems()) {
//          CalendarInfo info = new CalendarInfo(calendar.getId(), calendar.getSummary());
//          calendarSample.calendars.add(info);
//        }
      
      
      //Gibt den Standart-Unterkalender zurück, der dann als Listen-Item dargestellt wird
      Calendar primCalendar = client.calendars().get("primary").execute();     
      if (primCalendar != null) {
	      CalendarInfo info = new CalendarInfo(primCalendar.getId(), primCalendar.getSummary());
	      calendarSample.calendars.add(info);
      }     
    } catch (IOException e) {
      calendarSample.handleGoogleException(e);
    } finally {
      calendarSample.onRequestCompleted();
    }
    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    dialog.dismiss();
    calendarSample.refresh();
  }
}
