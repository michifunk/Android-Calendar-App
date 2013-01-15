/*
 * Copyright (c) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.services.samples.calendar.android;

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
 * @author MEF@google.com (Your Name Here)
 *
 */
class AsyncAddEvent2 extends AsyncTask<Void, Void, Void> {
  private final CalendarSample calendarSample;
  private final ProgressDialog dialog;
  private final int calendarIndex;
  private final String sVlName;
  private final String sFrequency;
  private final String sStartDate;
  private final String sEndDate;  
  private com.google.api.services.calendar.Calendar client;
  private static final String TAG = "AsyncAddEvent-Klasse";

  AsyncAddEvent2(CalendarSample calendarSample, int calendarIndex, String sVlName, String sStartDate, String sEndDate, String sFrequency) {
    this.calendarSample = calendarSample;
    client = calendarSample.client;
    this.calendarIndex = calendarIndex;
    dialog = new ProgressDialog(calendarSample);
    this.sVlName = sVlName;
    this.sFrequency = sFrequency;
    this.sStartDate = sStartDate;
    this.sEndDate = sEndDate;
    
  }

  @Override
  protected void onPreExecute() {
    dialog.setMessage("Adding event...");
    dialog.show();
  }

  @Override
  protected Void doInBackground(Void... arg0) {
    String calendarId = calendarSample.calendars.get(calendarIndex).id;
    Date dateStart = null;
    Date dateEnd= null; 
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");//spec for RFC3339  
    
    //Datumberechnung von Date zu DateTime und dann EventTime ;)
    try 
    {
      dateStart = sdf.parse(sStartDate);
      dateEnd = sdf.parse(sEndDate);
      
    } catch (ParseException exception) 
    {
      exception.printStackTrace();
      Log.d(TAG, "Fehler beim parsen");
    }    
    DateTime dtStart = new DateTime(dateStart, TimeZone.getTimeZone("UTC"));
    DateTime dtEnd = new DateTime(dateEnd, TimeZone.getTimeZone("UTC"));
    
    Event event = new Event();    
    event.setStart( new EventDateTime().setDateTime(dtStart).setTimeZone("UTC"));
    event.setEnd( new EventDateTime().setDateTime(dtEnd).setTimeZone("UTC"));
    event.setSummary(sVlName);
    event.setLocation("Berlin HWR Schöneberg");
    event.setColorId("11"); //11 -> rot
    event.setDescription("Hier könnte Ihre Beschreibung stehen");
    
    Log.d(TAG, "sFrequence BEFORE IF = " + sFrequency);
    if( sFrequency != null  )
    {
        if( sFrequency.equals("WEEKLY") || sFrequency.equals("DAILY")  )
        {
          String sRecurrenceContent = "RRULE:FREQ=" + sFrequency;
          //String sRecurrenceContent = "RRULE:FREQ=" + sFrequence + ";UNTIL=20140101T100000-07:00";
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
  @SuppressWarnings("unused")
  private String getEvent(String calendarId, String eventId)
  {
    //In Events nach bestimmter ID suchen
    Event foundEvent = null;
    try {
      foundEvent = client.events().get(calendarId, eventId).execute();
    } catch (IOException exception) {
      // TODO Auto-generated catch block
      exception.printStackTrace();
    }
    return foundEvent.getId();
  }
  
  @SuppressWarnings("unused")
  private void listEvents(String calendarId)
  {
  //Liste aller events eines Kalenders ausgeben
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
      { //Achtung, 'isEmpty() benötigt min API9. Hab ich im Manifest geändert.
        try {
          eventsList = client.events().list("primary").setPageToken(pageToken).execute();
        } catch (IOException exception) {
          // TODO Auto-generated catch block
          exception.printStackTrace();
        }
      } else 
      {
        break;
      }
    }
  }
}

