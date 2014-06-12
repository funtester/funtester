{
  "name" : "FunTester",
  "actors" : [ {
    "id" : 1,
    "name" : "System Analyst",
    "description" : "Document software's requirements."
  }, {
    "id" : 2,
    "name" : "Test Analyst",
    "description" : "Improve the requirements documentation from the tester's point of view."
  } ],
  "regularExpressions" : [ {
    "id" : 1,
    "name" : "software name",
    "expression" : "[A-Za-z0-9.\\-_ ]{2,50}"
  } ],
  "databaseConfigurations" : [ ],
  "queryConfigurations" : [ ],
  "useCases" : [ {
    "id" : 1,
    "name" : "Access System",
    "elements" : [ {
      "id" : 1,
      "name" : "Main Screen",
      "internalName" : "MainFrame",
      "type" : 8,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 6,
      "name" : "File",
      "internalName" : "fileMenu",
      "type" : 7,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 8,
      "name" : "New",
      "internalName" : "newMenu",
      "type" : 7,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 1,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 1,
        "elements" : [ 1 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.DocStep",
        "id" : 7,
        "trigger" : "ACTOR",
        "sentence" : "starts interacting with the software"
      } ],
      "postconditions" : [ ]
    }, {
      "@class" : "org.funtester.core.software.TerminatorFlow",
      "id" : 3,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 10,
        "elements" : [ 6, 8 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.UseCaseCallStep",
        "id" : 8,
        "actionNickname" : 5,
        "referencedUseCaseId" : 2
      } ],
      "postconditions" : [ ],
      "description" : "Create a New Software",
      "starterFlow" : 1,
      "starterStep" : 7,
      "influencingFlows" : [ ]
    } ],
    "actors" : [ 1 ],
    "preconditions" : [ ],
    "ignoreToGenerateTests" : true,
    "description" : "Allow accessing the system",
    "includeFiles" : [ {
      "name" : "org.funtester.app.ui.main.MainFrame"
    } ],
    "databaseScripts" : [ ]
  }, {
    "id" : 2,
    "name" : "Create a Software",
    "elements" : [ {
      "id" : 2,
      "name" : "Software Dialog",
      "internalName" : "SoftwareDialog",
      "type" : 6,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 3,
      "name" : "Name",
      "internalName" : "name",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 1,
        "type" : "REG_EX",
        "importance" : "MEDIUM",
        "message" : "The name must be between 2 to 50 characters long.",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.RegExBasedVC",
          "id" : 4,
          "regEx" : 1
        }
      }, {
        "id" : 5,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Please inform the name.",
        "valueConfiguration" : null
      } ]
    }, {
      "id" : 4,
      "name" : "Vocabulary",
      "internalName" : "vocabulary",
      "type" : 3,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 3,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Please choose a vocabulary.",
        "valueConfiguration" : null
      }, {
        "id" : 4,
        "type" : "ONE_OF",
        "importance" : "MEDIUM",
        "message" : "Please choose one of the vocabulary items on the list.",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.MultiVC",
          "id" : 3,
          "values" : [ "Default Vocabulary (English)", "Another Vocabulary (Portuguese)" ]
        }
      } ]
    }, {
      "id" : 5,
      "name" : "OK",
      "internalName" : "okButton",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 2,
      "priority" : "MUST",
      "complexity" : "LOW",
      "frequency" : "LOW",
      "importance" : "HIGH",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 2,
        "elements" : [ 2 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 3,
        "elements" : [ 3 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 4,
        "elements" : [ 4 ],
        "trigger" : "ACTOR",
        "actionNickname" : 4
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 5,
        "elements" : [ 5 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.OracleStep",
        "id" : 12,
        "elements" : [ 3, 4 ],
        "actionNickname" : 2,
        "messageOccurrence" : null,
        "elementType" : null
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 6,
        "elements" : [ 2 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 9
      } ],
      "postconditions" : [ {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 1,
        "description" : "Software created.",
        "ownerFlow" : 2
      } ]
    } ],
    "actors" : [ 1 ],
    "preconditions" : [ ],
    "ignoreToGenerateTests" : false,
    "description" : "Create a software project",
    "includeFiles" : [ {
      "name" : "org.funtester.app.ui.software.SoftwareDialog"
    } ],
    "databaseScripts" : [ ]
  } ],
  "lastIds" : {
    "Flow" : 3,
    "Step" : 12,
    "UseCase" : 2,
    "Element" : 8,
    "BusinessRule" : 5,
    "ValueConfiguration" : 4,
    "RegEx" : 1,
    "Actor" : 2,
    "ConditionState" : 1
  }
}