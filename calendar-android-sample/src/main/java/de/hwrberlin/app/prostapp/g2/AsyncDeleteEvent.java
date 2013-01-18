package de.hwrberlin.app.prostapp.g2;

import com.google.api.services.calendar.model.Event;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.io.IOException;

/**
 * Klasse zum L�schen eines Events im Kalender
 * 
 * @author M.Funk and P. K�hn
 *
 */
class AsyncDeleteEvent extends AsyncTask<Void, Void, Void> {
  private final GoogleCalendarConnection googleCalendarConnection;
  private final ProgressDialog dialog;
  private final int calendarIndex;
  private com.google.api.services.calendar.Calendar client;
  private final String sEventId;

  /**
 * @param googleCalendarConnection
 * @param calendarIndex
 * @param sEventId
 */
AsyncDeleteEvent(GoogleCalendarConnection googleCalendarConnection, int calendarIndex, String sEventId) {
    this.googleCalendarConnection = googleCalendarConnection;
    client = googleCalendarConnection.client;  
    this.calendarIndex = calendarIndex;
    this.sEventId = sEventId;
    dialog = new ProgressDialog(googleCalendarConnection);
  }

  @Override
  protected void onPreExecute() {
    dialog.setMessage("Event wird gel�scht...");
    dialog.show();
  }

  @Override
  protected Void doInBackground(Void... arg0) {
    String calendarId = googleCalendarConnection.calendars.get(calendarIndex).id;
    
    Event foundEvent = null;
    String sStatus = null;
    try {
    	foundEvent = client.events().get(calendarId, sEventId).execute();
  		sStatus = foundEvent.getStatus();
  		
    } catch (IOException exception) {
    	exception.printStackTrace();
    }
    if (sStatus.equals("confirmed")){    
	    try{
	        //Event �ber die API l�schen
	        client.events().delete(calendarId, sEventId).execute();
	        
	        //Event aus DB entfernen 
	        UpdateEvents updateEvents = new UpdateEvents();
	        updateEvents.deleteEvent(sEventId);
	        
	    } catch (IOException e) {
	      googleCalendarConnection.handleGoogleException(e);
	    } finally {
	      googleCalendarConnection.onRequestCompleted();
	    }
    }
    else{
    	googleCalendarConnection.setDialog("Status", "L�schen nicht m�glich da Status = "+sStatus);
    }
    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    dialog.dismiss();
    googleCalendarConnection.refresh();
  }
}
