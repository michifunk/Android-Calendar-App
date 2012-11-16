package com.google.api.services.samples.calendar.android;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

/**
 * Asynchronously insert an event to a calendar with a progress dialog.
 *
 * @author Michael Funk
 */
class AsyncAddEvent extends AsyncTask<Void, Void, Void> {
  private final CalendarSample calendarSample;
  private final ProgressDialog dialog;
  private final int calendarIndex;
  private com.google.api.services.calendar.Calendar client;
  private static final String TAG = "AsyncAddEvent Klasse";

  AsyncAddEvent(CalendarSample calendarSample, int calendarIndex) {
    this.calendarSample = calendarSample;
    client = calendarSample.client;
    this.calendarIndex = calendarIndex;
    dialog = new ProgressDialog(calendarSample);
  }

  @Override
  protected void onPreExecute() {
    dialog.setMessage("Adding event...");
    dialog.show();
  }

  @Override
  protected Void doInBackground(Void... arg0) {
    String calendarId = calendarSample.calendars.get(calendarIndex).id;
    
    Event event = new Event();    
    event.setSummary("Test-Event-Drei");
    event.setLocation("Berlin HWR");
    event.setColorId("11"); //11 -> rot
    
    
    Date startDate = new Date();
    Date endDate = new Date(startDate.getTime() + 3600000);
    DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
    event.setStart(new EventDateTime().setDateTime(start));
    DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
    event.setEnd(new EventDateTime().setDateTime(end));
    
    
    
       
    
    try {
      Event createdEvent = client.events().insert(calendarId, event).execute();
      
      Log.d(TAG, "Event-ID: " + createdEvent.getId());
  
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
