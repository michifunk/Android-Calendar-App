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
 * @author MEF@google.com (Michael Funk)
 *
 *Klasse für DB-Interaktionen
 */
public class DBActions implements ITblTest{
  
  
  private static final boolean DBG = true;  
  private static final String TAG = "DBActions";
  
  
  
  void initialization(Context context)
  {
    if( DBG ) Log.v( TAG, "entering..." );
    
    
    //DB erstellen fals noch nicht vorhanden
    InitDB initDB = new InitDB (context);
    
    //Alle Datensätze lesen und in LogCat ausgeben:    
    Cursor cur = initDB.getReadableDatabase().rawQuery(
        "SELECT * FROM " + TBL_TEST, null); /* +
         " WHERE " + COL_ID + "= ?",
        new String[] { String.valueOf( 2 ) } ); */
    
    if (cur.isBeforeFirst()){
        while( cur.moveToNext() ){      
            Log.i( TAG, COL_VL_NR            + ": " + cur.getLong  ( 
                                             cur.getColumnIndex( COL_VL_NR )) );
            Log.i( TAG, COL_VL_NAME          + ": " + cur.getString(  
                                             cur.getColumnIndex( COL_VL_NAME  )) );
            Log.i( TAG, COL_DATUM            + ": " + cur.getString(  
                                             cur.getColumnIndex( COL_DATUM  )) );
            Log.i( TAG, COL_STUDIENGANG      + ": " + cur.getString(  
                                             cur.getColumnIndex( COL_STUDIENGANG  )) );
            long lMaster =  cur.getLong( cur.getColumnIndex( COL_MASTERSTUDIENGANG ));
      if( lMaster == 0 ) Log.i( TAG, COL_MASTERSTUDIENGANG  + ": " + false );
      else               Log.i( TAG, COL_MASTERSTUDIENGANG  + ": " + true  );
        }
    }
    else {
        Log.i( TAG, "DB leer." );
        }
    
cur.close();        
    initDB.close();
    
    if( DBG ) Log.v( TAG, "...exiting" );
    
  }

}
