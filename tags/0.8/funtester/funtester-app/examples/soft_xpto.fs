{
  "name" : "xpto",
  "useCases" : [ {
    "id" : 1,
    "name" : "uc1",
    "elements" : [ {
      "id" : 1,
      "name" : "JanelaXPTO",
      "internalName" : "",
      "type" : null,
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
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 1,
        "elements" : [ 1 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      } ],
      "postconditions" : [ ]
    } ],
    "actors" : [ ],
    "preconditions" : [ ],
    "activationFlows" : [ ],
    "description" : "",
    "includeFiles" : [ ],
    "databaseScripts" : [ ],
    "onlyStartedByAnotherUseCase" : false
  } ],
  "actors" : [ ],
  "regularExpressions" : [ ],
  "databaseConfigurations" : [ ],
  "queryConfigurations" : [ ],
  "lastIds" : {
    "Flow" : 1,
    "Step" : 1,
    "UseCase" : 1,
    "Element" : 1
  }
}