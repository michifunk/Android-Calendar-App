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

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * @author MEF@google.com (Your Name Here)
 *
 */
public class UpdateEvents implements ITblTest {
  
  final static String TAG = "Klasse: UpdatEvents";
  static InitDB initDB; //Static, da für alle Instanzen
//SQLiteDatabase initDB;

  
public void checkEvents(Context context, CalendarSample calendarSample, int calIndex){
    UpdateEvents.initDB = new InitDB(context);     
    Cursor cur = initDB.getReadableDatabase().rawQuery("SELECT * FROM " + TBL_TEST, null);      
    Log.d(TAG, "SQL-Select done");
    
    if (cur.isBeforeFirst()){
        while( cur.moveToNext() ){  
          Log.d(TAG, "Event ID in DB = " + cur.getString(cur.getColumnIndex(COL_G_EVENT_ID)));         
            if (cur.isNull(cur.getColumnIndex(COL_G_EVENT_ID)) )             
            {
              //Event anlegen in Kalender                           
              String sVlName = cur.getString(cur.getColumnIndex(COL_VL_NAME));
              String sDatum = cur.getString(cur.getColumnIndex(COL_START_DATUM)); //Datumsübergabe für AddEvent
              new AsyncAddEvent2(calendarSample, calIndex, sVlName, sDatum).execute(); 
            }
            else Log.d(TAG, "Event bereits im Kalender. ID: " + cur.getString(cur.getColumnIndex(COL_G_EVENT_ID)));         
        }
    }
    else {
        Log.i( "DB", "DB leer." );
        }    
cur.close();
initDB.close();
  
  }
public void addEventId(String eventId, String vlName){

    String sqlquery= "UPDATE " + TBL_TEST + 
                     " SET " + COL_G_EVENT_ID + " = '" + eventId +  
                     "' WHERE " + COL_VL_NAME + " = '" + vlName + "';"; 
    Log.d( "Query", sqlquery );
    initDB.getWritableDatabase().execSQL(sqlquery);
    }  
}


