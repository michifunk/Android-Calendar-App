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

/**
 * @author MEF@google.com (Michael Funk)
 *
 */
public interface ITblTest {

  /** Name der Datenbanktabelle. */
  String TBL_TEST = "test";
  
  /** Primärschlüssel. */
  String COL_ID = "_id";
  
  /** Ungültige COL_ID */
  long ID_TEST_INVALID = -1;

  /** VL-Nummer */
   String COL_VL_NR = "vl_nr";

  /** Name der VL */  
  String COL_VL_NAME = "name";

  /** Studiengang.<br>
  *   TEXT */  
  String COL_STUDIENGANG = "studiengang";

   /** Masterstudent.<br>
    * Pflichtfeld<br>
    *  BOOLEAN */
   String COL_MASTERSTUDIENGANG = "masterstudiengang";

   /** Ein Datum.<br>
    *   DATE */  
    String COL_DATUM = "geb_dat";
    
    /** Google-Event-ID, wird von Google vergeben */  
    String COL_G_EVENT_ID = "G_eventID";

    /** Liste aller Attribute der Tabelle */
    String[] ALL_COLS_STUDENT = { COL_ID           , COL_VL_NR, 
                                      COL_VL_NAME         , COL_STUDIENGANG, 
                                      COL_MASTERSTUDIENGANG, COL_DATUM, COL_G_EVENT_ID };

    /** Create table statement */
    String CREATE_TBL_TEST = "CREATE TABLE " + TBL_TEST + "(" +
        COL_ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COL_VL_NR               + " INTEGER NOT NULL, " +
        COL_VL_NAME             + " TEXT NOT NULL, "    +
        COL_STUDIENGANG         + " TEXT, "             +
        COL_MASTERSTUDIENGANG   + " BOOLEAN NOT NULL, " +
        COL_DATUM               + " DATE, "  +
        COL_G_EVENT_ID          + " TEXT)";       
}