package com.example.datatrap.core.data.locality_session

/*
* Create DAO and Entity for Locality and Session
* Every time new Combination is made num of sessions in a locality goes up and vice versa
* A Combination is created only if it does not exist
* The numSes in a Locality goes up only when the combination does not exist
*/

// DELETE LocSessCrossRef, po vymazani update Locality numSession - 1

//    "CREATE TRIGGER IF NOT EXISTS OnLocSessDeleteUpdateLocality
//    BEFORE DELETE ON LocalitySessionCrossRef
//    BEGIN UPDATE Locality
//    SET numSessions = numSessions - 1, localityDateTimeUpdated = (strftime('%s','now') * 1000) WHERE localityId = OLD.localityId;
//    END"

// INSERT LocSessCrossRef, po pridani update Locality numSession + 1

//    "CREATE TRIGGER IF NOT EXISTS OnLocSessInsertUpdateLocality
//    BEFORE INSERT ON LocalitySessionCrossRef
//    BEGIN UPDATE Locality
//    SET numSessions = numSessions + 1, localityDateTimeUpdated = (strftime('%s','now') * 1000) WHERE localityId = NEW.localityId;
//    END"