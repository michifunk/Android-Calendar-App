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
  
  void checkEvents(Context context, CalendarSample calendarSample, int calIndex){
    
    InitDB initDB = new InitDB (context);
    Cursor cur = initDB.getReadableDatabase().rawQuery(
        "SELECT * FROM " + TBL_TEST, null); /* +
         " WHERE " + COL_ID + "= ?",
        new String[] { String.valueOf( 2 ) } ); */
    
    Log.d(TAG, "SQL-Select executed");
    
    if (cur.isBeforeFirst()){
        while( cur.moveToNext() ){  
          Log.d(TAG, "Event ID = " + cur.getString(cur.getColumnIndex(COL_G_EVENT_ID)));         
            if (cur.isNull(cur.getColumnIndex(COL_G_EVENT_ID)) )             
            {
              //Event anlegen in Kalender
              Log.d(TAG, "Entering IsNull-If-Block");              
              String sVlName = cur.getString(cur.getColumnIndex(COL_VL_NAME));
              new AsyncAddEvent2(calendarSample, calIndex, sVlName).execute(); 
            }
                      
        }
    }
    else {
        Log.i( "DB", "DB leer." );
        }
    
cur.close();        
initDB.close();    
  }

}
