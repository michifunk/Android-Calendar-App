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
  private com.google.api.services.calendar.Calendar client;
  private static final String TAG = "AsyncAddEvent Klasse";

  AsyncAddEvent2(CalendarSample calendarSample, int calendarIndex, String sVlName) {
    this.calendarSample = calendarSample;
    client = calendarSample.client;
    this.calendarIndex = calendarIndex;
    dialog = new ProgressDialog(calendarSample);
    this.sVlName = sVlName;
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
    event.setSummary(sVlName);
    event.setLocation("Berlin HWR Schöneberg");
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
      
      
      //In Events nach bestimmter ID suchen
      String sEvent = createdEvent.getId();
      //String sTestId = "123456";
      Event foundEvent = client.events().get(calendarId, sEvent).execute();
      Log.d(TAG, "foundEvent-ID: " + foundEvent.getId());
      
      
      //Liste aller events eines Kalenders ausgeben
      Events eventsList= client.events().list(calendarId).execute();
      while (true) {
        Log.d(TAG, "Eventliste:");
        for (Event eItem : eventsList.getItems()) 
        {
          Log.d(TAG, "Event-Name: " + eItem.getSummary());
        }
        String pageToken = eventsList.getNextPageToken();
        if (pageToken != null && !pageToken.isEmpty()) {    //Achtung, 'isEmpty() benötigt min API9. Hab ich im Manifest geändert.
          eventsList = client.events().list("primary").setPageToken(pageToken).execute();
        } else {
          break;
        }
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

