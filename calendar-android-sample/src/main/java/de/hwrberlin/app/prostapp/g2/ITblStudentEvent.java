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

package de.hwrberlin.app.prostapp.g2;

/**
 * Gibt das Schema der Datenbank und Tabelle vor
 * 
 * @author M. Funk and P. Köhn
 *
 */
public interface ITblStudentEvent {

  /** Name der Datenbanktabelle. */
  String TBL_EVENTS = "events";
  
  /** Primärschlüssel. */
  String COL_ID = "_id";
  
  /** Ungültige COL_ID */
  long ID_TEST_INVALID = -1;

  /** Event-Name 
   * TEXT */
  String COL_EVENT_NAME = "name";

  /** Nummer des Events zB VL-Nummer 
   * TEXT */  
  String COL_EVENT_NUMBER = "nummer";
  
  /** Zugeordnete Person zB Dozent
   * TEXT */  
  String COL_EVENT_PERSON = "person";
   
  /** Das Beginndatum.<br>
  *   DATE */  
  String COL_START_DATE = "datum_beginn";
    
  /** Das Enddatum.<br>
  *   DATE */  
  String COL_END_DATE = "datum_ende";
     
  /** Die Wiederholungsfrequenz des Events<br>
  *   TEXT */  
  String COL_REC_FREQUENCE = "frequenz";
    
  /** Google-Event-ID, wird von Google vergeben <br> 
  *   TEXT */
  String COL_G_EVENT_ID = "google_calendar_id";

  /** Liste aller Attribute der Tabelle */
  String[] ALL_COLS_STUDENT = { COL_ID, 
                                COL_EVENT_NAME,
                                COL_EVENT_NUMBER,
                                COL_EVENT_PERSON,
                                COL_START_DATE, 
                                COL_END_DATE, 
                                COL_REC_FREQUENCE, 
                                COL_G_EVENT_ID};

  /** Create table statement */
  String CREATE_TBL_TEST = "CREATE TABLE " + TBL_EVENTS + "(" +
        COL_ID              + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COL_EVENT_NAME      + " TEXT NOT NULL, " +
        COL_EVENT_NUMBER    + " TEXT, "    +
        COL_EVENT_PERSON    + " TEXT, "             +
        COL_START_DATE      + " DATE NOT NULL, " +
        COL_END_DATE        + " DATE NOT NULL, "  +
        COL_REC_FREQUENCE   + " TEXT, "  +
        COL_G_EVENT_ID      + " TEXT )";       
}