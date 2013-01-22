package de.hwrberlin.app.prostapp.g2;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;



/**
 * Ist dafür Zuständig, dass über die Calendar-API Events in den Kalender
 * eingefügt werden.
 * 
 * @author M.Funk and P. Köhn
 *
 */

class AsyncAddEvent extends AsyncTask<Void, Void, Void> {
  private final GoogleCalendarConnection googleCalendarConnection;
  private final ProgressDialog dialog;
  private final int calendarIndex;
  private final String sVlName;
  private final String sFrequency;
  private final String sStartDate;
  private final String sEndDate;  
  private com.google.api.services.calendar.Calendar client;
  private static final String TAG = "AsyncAddEvent-Klasse";

  /**
 * @param googleCalendarConnection
 * @param calendarIndex
 * @param sVlName
 * @param sStartDate
 * @param sEndDate
 * @param sFrequency
 */
AsyncAddEvent(GoogleCalendarConnection googleCalendarConnection, int calendarIndex, String sVlName, String sStartDate, String sEndDate, String sFrequency) {
    this.googleCalendarConnection = googleCalendarConnection;
    client = googleCalendarConnection.client;
    this.calendarIndex = calendarIndex;
    dialog = new ProgressDialog(googleCalendarConnection);
    this.sVlName = sVlName;
    this.sFrequency = sFrequency;
    this.sStartDate = sStartDate;
    this.sEndDate = sEndDate; 
  }

  @Override
  protected void onPreExecute() {
    dialog.setMessage("Adding event...");
//    dialog.show();
  }

  @Override
  protected Void doInBackground(Void... arg0) {
    String calendarId = googleCalendarConnection.calendars.get(calendarIndex).id;
    Date dateStart = null;
    Date dateEnd= null; 
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");//Format für RFC3339  
    
    //Datum-Formatierung: Date -> DateTime -> EventTime
    try 
    {
      dateStart = sdf.parse(sStartDate);
      dateEnd = sdf.parse(sEndDate);
      
    } catch (ParseException exception) 
    {
      exception.printStackTrace();
      Log.d(TAG, "Fehler beim parsen eines Datums");
    }    
    DateTime dtStart = new DateTime(dateStart, TimeZone.getTimeZone("UTC"));
    DateTime dtEnd = new DateTime(dateEnd, TimeZone.getTimeZone("UTC"));
    
    Event event = new Event();    
    event.setStart( new EventDateTime().setDateTime(dtStart).setTimeZone("UTC"));
    event.setEnd( new EventDateTime().setDateTime(dtEnd).setTimeZone("UTC"));
    event.setSummary(sVlName);
    event.setLocation("HWR Schöneberg, Berlin");
    event.setColorId("11"); //11 => rot
    event.setDescription("Hier könnte eine sinnvolle Beschreibung stehen");
    
    if( sFrequency != null  )
    {
        if( sFrequency.equals("WEEKLY") || sFrequency.equals("DAILY")  )
        {
          String sRecurrenceContent = "RRULE:FREQ=" + sFrequency;
//          String sRecurrenceContent = "RRULE:FREQ=" + sFrequency + ";UNTIL=20130303T100000+00:00";
          Log.d(TAG, "sRecContent: " + sRecurrenceContent);
          event.setRecurrence(Arrays.asList(sRecurrenceContent));
        }
     }     
     try {
      //Event eintragen
      Event createdEvent = client.events().insert(calendarId, event).execute();
      Log.d(TAG, "Event-ID: " + createdEvent.getId());
      
      //Google-Event-ID zum speichern in der DB übergeben. 
      UpdateEvents updateEvents = new UpdateEvents();
      updateEvents.addEventId(createdEvent.getId(), sVlName);
      
    } catch (IOException e) {
      googleCalendarConnection.handleGoogleException(e);
    } finally {
      googleCalendarConnection.onRequestCompleted();
    }
    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
//    dialog.dismiss();
    googleCalendarConnection.refresh();
  }
  
  
  /**
   * Kann bei bedarf eine Liste aller Events im Kalender erstellen.
   * Wird im Moment nicht verwendet.
   * 
 * @param calendarId
 */
@SuppressWarnings("unused")
  private void listEvents(String calendarId)
  {
    Events eventsList = null;
    try {
      eventsList = client.events().list(calendarId).execute();
    } catch (IOException exception) {
      // TODO Auto-generated catch block
      exception.printStackTrace();
    }
    while (true) 
    {
      Log.d(TAG, "Eventliste:");
      for (Event eItem : eventsList.getItems()) 
      {
        Log.d(TAG, "Event-Name: " + eItem.getSummary());
      }
      String pageToken = eventsList.getNextPageToken();
      if (pageToken != null && !pageToken.isEmpty()) 
      { 
        try {
          eventsList = client.events().list("primary").setPageToken(pageToken).execute();
        } catch (IOException exception) {
          exception.printStackTrace();
        }
      } else 
      {
        break;
      }
    }
  }
}
