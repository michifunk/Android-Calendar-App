package de.hwrberlin.app.prostapp.g2;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Klasse um die Events im Kalender und in der DB zu bearbeiten.
 * 
 * @author M. Funk and P. Köhn
 *
 */
public class UpdateEvents implements ITblStudentEvent {
  
  final static String TAG = "Klasse: UpdatEvents";
  static InitDB initDB; //Static, da für alle Instanzen

  
/**
 * Die Methode schaut in der DB nach Events, die noch keine Google-ID haben.
 * Diese werden dann über AsyncAddEvent in den Kalender eingefügt.
 * Außerdem wird das Löschen und Ändern eines Events anhand der dafür implementierten
 * Klassen beispielhaft dargestellt.
 * 
 * @param context
 * @param googleCalendarConnection
 * @param calIndex
 */
public void checkEvents(Context context, GoogleCalendarConnection googleCalendarConnection, int calIndex){
    UpdateEvents.initDB = new InitDB(context);     
    Cursor cur = initDB.getReadableDatabase().rawQuery("SELECT * FROM " + TBL_EVENTS, null);      
    Log.d(TAG, "SQL-Select done");
    
    if (cur.isBeforeFirst()){
        while( cur.moveToNext() ){  
          Log.d(TAG, "Event ID in DB = " + cur.getString(cur.getColumnIndex(COL_G_EVENT_ID)));         
            if (cur.isNull(cur.getColumnIndex(COL_G_EVENT_ID)) )             
            {
              //Event-Infos aus DB auslesen und übergeben                          
              String sVlName = cur.getString(cur.getColumnIndex(COL_EVENT_NAME));
              String sStartDate = cur.getString(cur.getColumnIndex(COL_START_DATE)); //Datumsübergabe für AddEvent
              String sEndDate = cur.getString(cur.getColumnIndex(COL_END_DATE));
              String sFrequence = cur.getString(cur.getColumnIndex(COL_REC_FREQUENCE)); 
              //Event einfügen
              new AsyncAddEvent(googleCalendarConnection, calIndex, sVlName, sStartDate, sEndDate, sFrequence).execute(); 
            }
            else Log.d(TAG, "Event bereits im Kalender. ID: " + cur.getString(cur.getColumnIndex(COL_G_EVENT_ID)));         
        }
    }
    else {
        Log.i( "DB", "DB leer." );
        } 
    
//    Löschen eines events
    new AsyncDeleteEvent(googleCalendarConnection, calIndex, "oc0ktnensmdr38d290cdosnq8k").execute();
//    new AsyncDeleteEvent(googleCalendarConnection, calIndex, "lk4prfipfralhlfhmh1o7mv520").execute(); 
//    new AsyncDeleteEvent(googleCalendarConnection, calIndex, "35lesclpuetvab7rahi3elol3g").execute(); 
    
//    Ändern eines events
    new AsyncChangeEvent(googleCalendarConnection, calIndex, "Geänderte Vorlesung", "2013-01-15T15:30:00+00:00", "2013-01-15T16:30:00+00:00", "", "3d97nt3g4phnps54raveutcdm4").execute();
    
cur.close();
initDB.close();
  
  }
/**
 * @param eventId
 * @param vlName
 */
public void addEventId(String eventId, String vlName){
  String sqlquery = "UPDATE " + TBL_EVENTS + 
                     " SET " + COL_G_EVENT_ID + " = '" + eventId +  
                     "' WHERE " + COL_EVENT_NAME + " = '" + vlName + "';"; 
  Log.d( "Query", sqlquery );
  initDB.getWritableDatabase().execSQL(sqlquery);
  }  

/**
 * @param eventId
 */
public void deleteEvent(String eventId){
  String sqlquery = "DELETE FROM " + TBL_EVENTS + 
                    " WHERE " + COL_G_EVENT_ID + " = '" + eventId + "';"; 
  Log.d( "Query", sqlquery );
  initDB.getWritableDatabase().execSQL(sqlquery);
  } 

/**
 * @param eventId
 * @param sVlName
 * @param sStartDate
 * @param sEndDate
 * @param sFrequency
 */
public void changeEvent(String eventId, String sVlName, String sStartDate, String sEndDate, String sFrequency){
  String sqlquery =  "UPDATE " + TBL_EVENTS + 
                    " SET " + COL_EVENT_NAME      + " = '" + sVlName +    "', "
                            + COL_START_DATE      + " = '" + sStartDate + "', "
                            + COL_END_DATE        + " = '" + sEndDate + "', " 
                            + COL_REC_FREQUENCE   + " = '" + sFrequency + "'"
                    + " WHERE " + COL_G_EVENT_ID  + " = '" + eventId + "';"; 
  Log.d( "Query", sqlquery );
  initDB.getWritableDatabase().execSQL(sqlquery);
  } 
}


