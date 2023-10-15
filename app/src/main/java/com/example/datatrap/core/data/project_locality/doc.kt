package com.example.datatrap.core.data.project_locality

/*
* Create DAO and Entity for Project and Locality
* Every time new Combination is made num of locals in a project goes up and vice versa
* A Combination is created only if it does not exist
* The numLocal in a Project goes up only when the combination does not exist
*
*     // Delete PrjLocCrossRef, po vymazani kombinacie sa updatne Project numLocal - 1

//    "CREATE TRIGGER IF NOT EXISTS OnProjectLocalityCrossRefDeleteUpdateProject
//    BEFORE DELETE ON ProjectLocalityCrossRef
//    BEGIN UPDATE Project
//    SET numLocal = (numLocal - 1), projectDateTimeUpdated = (strftime('%s','now') * 1000) WHERE projectId = OLD.projectId;
//    END"

// Insert PrjLocCrossRef, po pridani kombinacie sa updatne Project numLocal + 1

//    "CREATE TRIGGER IF NOT EXISTS OnProjectLocalityCrossRefInsertUpdateProject
//    BEFORE INSERT ON ProjectLocalityCrossRef
//    BEGIN UPDATE Project
//    SET numLocal = (numLocal + 1), projectDateTimeUpdated = (strftime('%s','now') * 1000) WHERE projectId = NEW.projectId;
//    END"*/

