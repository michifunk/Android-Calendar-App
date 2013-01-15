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
public class AsyncChangeEvent extends AsyncTask<Void, Void, Void> {
  private final CalendarSample calendarSample;
  private final ProgressDialog dialog;
  private final int calendarIndex;
  private final String sVlName;
  private final String sFrequency;
  private final String sStartDate;
  private final String sEndDate;  
  private com.google.api.services.calendar.Calendar client;
  private static final String TAG = "AsyncAddEvent-Klasse";
  private final String sEventId;

  AsyncChangeEvent(CalendarSample calendarSample, int calendarIndex, String sVlName, String sStartDate, String sEndDate, String sFrequency, String sEventId) {
    this.calendarSample = calendarSample;
    client = calendarSample.client;
    this.calendarIndex = calendarIndex;
    dialog = new ProgressDialog(calendarSample);
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
    String calendarId = calendarSample.calendars.get(calendarIndex).id;
    
    
  //In Events nach bestimmter ID suchen
    Event foundEvent = null;
    try {
      foundEvent = client.events().get(calendarId, sEventId).execute();
    } catch (IOException exception) {
      // TODO Auto-generated catch block
      exception.printStackTrace();
    }
        
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
    
    
    foundEvent.setStart( new EventDateTime().setDateTime(dtStart).setTimeZone("UTC"));
    foundEvent.setEnd( new EventDateTime().setDateTime(dtEnd).setTimeZone("UTC"));
    foundEvent.setSummary(sVlName);
    
    Log.d(TAG, "sFrequence BEFORE IF = " + sFrequency);
    if( sFrequency != null  )
    {
        if( sFrequency.equals("WEEKLY") || sFrequency.equals("DAILY")  )
        {
          String sRecurrenceContent = "RRULE:FREQ=" + sFrequency;
          //String sRecurrenceContent = "RRULE:FREQ=" + sFrequence + ";UNTIL=20140101T100000-07:00";
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
