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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * @author MEF@google.com (Michael Funk)
 *
 */
public class InitDB extends SQLiteOpenHelper implements ITblTest{

  private static final boolean DBG = true;   //DBG for debugging
  private static final String CNAME = "InitDB.";
  
  /** Name der Datenbankdatei. */
private static final String DB_NAME = "dbtest.db";

/** Version des Schemas. */
private static final int DB_VERSION = 4;    //Erhöhen wenn Tabellenschema geändert wird!!

/**
 * Der Konstruktor benötigt als Input-Parameter den Context der Anwendung.
 * 
 * @param context Context der aufrufenden Anwendung.
 */
public InitDB( Context context ) {
  
  super( context, DB_NAME, null, DB_VERSION );  
  
  final String MNAME = "InitDB()";  
  final String TAG = CNAME + MNAME;
  if( DBG ) Log.v( TAG, "entering..." );
      
  if( DBG ) Log.v( TAG, "...exiting" );
}

/**
 * Wird aufgerufen, wenn das Datenbankschema neu angelegt werden soll.
 * 
 * @param db Aktuelle Datenbank-Verbindung
 */
@Override
public void onCreate( SQLiteDatabase db ) {
  
  final String MNAME = "onCreate()";  
      final String TAG = CNAME + MNAME;
      if( DBG ) Log.v( TAG, "entering..." );
                  
  db.execSQL( CREATE_TBL_TEST );     
  erzeugeTestdaten( db ); 
  
  
  if( DBG ) Log.v( TAG, "...exiting" );
}

/**
 * Wird aufgerufen, wenn sich die Version des Schemas geä¤ndert hat.
 * Im kontreten Fall wird die Datenbank gelälscht, mit neuem Schema wieder
 * aufgebaut und mit Testdaten gefüllt. In anderen FÃ¤llen kann es 
 * erforderlich sein, die bereits vorhanden Daten in die neuen Tabellen 
 * zu kopieren. 
 *  
 * @param db Aktuelle Datenbank-Verbindung
 * @param oldVersion bisherige Schemaversion
 * @param newVersion neue Schemaversion
 */
@Override
public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion) {
  
  final String MNAME = "onUpgrade()";  
      final String TAG = CNAME + MNAME;
      if( DBG ){
          Log.v( TAG, "entering..." );
    Log.v( TAG, "Upgrading database from version " + oldVersion +
               " to " + newVersion + ", which will destroy all old data" );
      }
      
  db.execSQL( "DROP TABLE IF EXISTS " + TBL_TEST );    
  onCreate( db );
  
  if( DBG ) Log.v( TAG, "...exiting" );
} 

/**
 * Erzeugt einige Testdatensätze.
 * 
 * @param db
 */
private void erzeugeTestdaten( SQLiteDatabase db ) {

  final String MNAME = "onUpgrade()";  
      final String TAG = CNAME + MNAME;
      if( DBG ) Log.v( TAG, "entering..." );      
      
  final SQLiteStatement stm = db.compileStatement( 
                 "INSERT INTO " + TBL_TEST      + "( " + 
                       COL_VL_NR                + ", " + 
                       COL_VL_NAME              + ", " +                      
                       COL_STUDIENGANG          + ", " + 
                       COL_MASTERSTUDIENGANG    + ", " +
                       COL_START_DATUM          + ", " +
                       COL_END_DATUM            + 
                  ") VALUES( ?, ?, ?, ?, ?, ? )" );
  db.beginTransaction();

  try {      
      stm.bindLong  ( 1, 111111                     );
      stm.bindString( 2, "Programmieren für Noobs"  );
      stm.bindString( 3, "Wirtschaftsinformatik"    );
      stm.bindLong  ( 4, 0                          );
      stm.bindString( 5, "2012-12-19T10:00:00+08:00");//date format based on RFC 3339
      stm.bindString( 6, "2012-12-12T14:00:00+08:00");
      stm.executeInsert();

      stm.bindLong  ( 1, 222222                     );
      stm.bindString( 2, "Angewandtes Prozess MGMT" );
      stm.bindString( 3, "Wirtschaftsinformatik"    );
      stm.bindLong  ( 4, 1                          );
      stm.bindString( 5, "2012-12-19T10:00:00+08:00");
      stm.bindString( 6, "2012-12-12T14:00:00+08:00");
      stm.executeInsert();
      
      stm.bindLong  ( 1, 333333                     );
      stm.bindString( 2, "Projekt Management"       );
      stm.bindString( 3, "Wirtschaftsinformatik"    );
      stm.bindLong  ( 4, 0                          );
      stm.bindString( 5, "2012-12-20T10:00:00+08:00");
      stm.bindString( 6, "2012-12-20T14:00:00+08:00");
      stm.executeInsert();
      
      stm.bindLong  ( 1, 444444                     );
      stm.bindString( 2, "Operations Management"       );
      stm.bindString( 3, "Wirtschaftsinformatik"    );
      stm.bindLong  ( 4, 0                          );
      stm.bindString( 5, "2012-12-26T10:00:00+00:00");
      stm.bindString( 6, "2012-12-28T14:00:00+08:00");
      stm.executeInsert();
      
      stm.bindLong  ( 1, 555555                     );
      stm.bindString( 2, "Schlafkurs für Michi"       );
      stm.bindString( 3, "Wirtschaftsinformatik"    );
      stm.bindLong  ( 4, 1                          );
      stm.bindString( 5, "2012-12-28T10:00:00+00:00");
      stm.bindString( 6, "2012-12-28T14:00:00+00:00");
      stm.executeInsert();
      
    db.setTransactionSuccessful();
  } catch( Throwable ex ) {
    Log.e( TAG, "Fehler beim Einfügen eines Testdatensatzes. " + ex );
  } finally { 
      db.endTransaction(); 
      stm.close();
  }    
  
  if( DBG ) Log.v( TAG, "...exiting" );
}

}
