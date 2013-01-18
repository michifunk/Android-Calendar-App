package de.hwrberlin.app.prostapp.g2;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

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
 * @author M.Funk and P. Köhn
 * 
 *
 */
public class AsyncChangeEvent extends AsyncTask<Void, Void, Void> {
  private final GoogleCalendarConnection googleCalendarConnection;
  private final ProgressDialog dialog;
  private final int calendarIndex;
  private final String sVlName;
  private final String sFrequency;
  private final String sStartDate;
  private final String sEndDate;  
  private com.google.api.services.calendar.Calendar client;
  private static final String TAG = "AsyncAddEvent-Klasse";
  private final String sEventId;
  
 
  
  AsyncChangeEvent(GoogleCalendarConnection googleCalendarConnection, int calendarIndex, String sVlName, String sStartDate, String sEndDate, String sFrequency, String sEventId) {
    this.googleCalendarConnection = googleCalendarConnection;
    client = googleCalendarConnection.client;
    this.calendarIndex = calendarIndex;
    dialog = new ProgressDialog(googleCalendarConnection);
    this.sVlName = sVlName;
    this.sFrequency = sFrequency;
    this.sStartDate = sStartDate;
    this.sEndDate = sEndDate;
    this.sEventId = sEventId;

  }

  @Override
  protected void onPreExecute() {
    dialog.setMessage("Changing event...");
    dialog.show();
  }

  @Override
  protected Void doInBackground(Void... arg0) {
    String calendarId = googleCalendarConnection.calendars.get(calendarIndex).id;
    
    
  //In Events nach bestimmter ID suchen
    Event foundEvent = null;
    try {
      foundEvent = client.events().get(calendarId, sEventId).execute();
    } catch (IOException exception) {
    	exception.printStackTrace();
    }
    if (foundEvent != null){
	    Date dateStart = null;
	    Date dateEnd= null; 
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");//spec for RFC3339  
	    
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
	       
	    foundEvent.setStart( new EventDateTime().setDateTime(dtStart).setTimeZone("UTC"));
	    foundEvent.setEnd( new EventDateTime().setDateTime(dtEnd).setTimeZone("UTC"));
	    foundEvent.setSummary(sVlName);
	    
	    if( sFrequency != null  )
	    {
	        if( sFrequency.equals("WEEKLY") || sFrequency.equals("DAILY")  )
	        {
	          String sRecurrenceContent = "RRULE:FREQ=" + sFrequency;
	          Log.d(TAG, "sRecContent: " + sRecurrenceContent);
	          foundEvent.setRecurrence(Arrays.asList(sRecurrenceContent));
	        }
	     }     
	     try {
	      //Event im Kalender updaten
	      Event updatedEvent = client.events().update(calendarId, sEventId, foundEvent).execute();
	      Log.d(TAG, "Event-ID: " + updatedEvent.getId());
	      
	      //Event in der DB ändern 
	      UpdateEvents updateEvents = new UpdateEvents();
	      updateEvents.changeEvent(sEventId, sVlName, sStartDate, sEndDate, sFrequency);	      
	      
	    } catch (IOException e) {
	      googleCalendarConnection.handleGoogleException(e);
	    } finally {
	      googleCalendarConnection.onRequestCompleted();
	    }
	   googleCalendarConnection.setDialog(sVlName, "Event wurde erfolgreich geändert.");
	 }
    else {
    	googleCalendarConnection.setDialog(sVlName, "Event nicht im Kalender gefunden.");
    }
	return null;
	    
  }

  @Override
  protected void onPostExecute(Void result) {
    dialog.dismiss();
    googleCalendarConnection.refresh(); 
  }
}
