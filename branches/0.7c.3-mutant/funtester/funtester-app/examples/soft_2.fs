{
  "name" : "My s1",
  "useCases" : [ {
    "id" : 1,
    "name" : "my uc1",
    "elements" : [ {
      "@class" : "org.funtester.core.software.Element",
      "id" : 1,
      "name" : "OK",
      "internalName" : "",
      "type" : null
    }, {
      "@class" : "org.funtester.core.software.Element",
      "id" : 11,
      "name" : "JustLikeAnyOtherField",
      "internalName" : "",
      "type" : null
    }, {
      "@class" : "org.funtester.core.software.Element",
      "id" : 8,
      "name" : "My Other Field",
      "internalName" : "",
      "type" : null
    }, {
      "@class" : "org.funtester.core.software.Element",
      "id" : 10,
      "name" : "JustAnotherField",
      "internalName" : "",
      "type" : null
    }, {
      "@class" : "org.funtester.core.software.Element",
      "id" : 5,
      "name" : "Fazer",
      "internalName" : "",
      "type" : null
    }, {
      "@class" : "org.funtester.core.software.Element",
      "id" : 3,
      "name" : "Cancelar",
      "internalName" : "",
      "type" : null
    }, {
      "@class" : "org.funtester.core.software.Element",
      "id" : 2,
      "name" : "Janela de Algo",
      "internalName" : "",
      "type" : null
    }, {
      "@class" : "org.funtester.core.software.Element",
      "id" : 12,
      "name" : "Bla",
      "internalName" : "",
      "type" : null
    }, {
      "@class" : "org.funtester.core.software.Element",
      "id" : 7,
      "name" : "My Field",
      "internalName" : "",
      "type" : null
    }, {
      "@class" : "org.funtester.core.software.Element",
      "id" : 13,
      "name" : "AnotherField",
      "internalName" : "",
      "type" : null
    }, {
      "@class" : "org.funtester.core.software.Element",
      "id" : 9,
      "name" : "SomeButton",
      "internalName" : "",
      "type" : null
    }, {
      "@class" : "org.funtester.core.software.Element",
      "id" : 4,
      "name" : "Testar",
      "internalName" : "",
      "type" : null
    }, {
      "@class" : "org.funtester.core.software.Element",
      "id" : 6,
      "name" : "ReFazer",
      "internalName" : "",
      "type" : null
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 1,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 2,
        "elements" : [ 2 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 1,
        "elements" : [ 6 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.DocStep",
        "id" : 4,
        "trigger" : "SYSTEM",
        "sentence" : "faz algo"
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 8,
        "elements" : [ 9 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 3,
        "elements" : [ 3 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 7,
        "elements" : [ 7 ],
        "trigger" : "ACTOR",
        "actionNickname" : 4
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 10,
        "elements" : [ 11 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.OracleStep",
        "id" : 11,
        "elements" : [ 7 ],
        "actionNickname" : 2,
        "messageOccurrence" : null,
        "elementType" : null
      }, {
        "@class" : "org.funtester.core.software.OracleStep",
        "id" : 12,
        "elements" : [ 1, 13, 3, 4 ],
        "actionNickname" : 2,
        "messageOccurrence" : null,
        "elementType" : null
      }, {
        "@class" : "org.funtester.core.software.OracleStep",
        "id" : 13,
        "elements" : [ 1 ],
        "actionNickname" : 2,
        "messageOccurrence" : null,
        "elementType" : null
      }, {
        "@class" : "org.funtester.core.software.OracleStep",
        "id" : 14,
        "elements" : [ 1, 10 ],
        "actionNickname" : 2,
        "messageOccurrence" : null,
        "elementType" : null
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 15,
        "elements" : [ 1 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
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
  "actors" : [ {
    "id" : 1,
    "name" : "Administrador",
    "description" : "Responsável por manter outros usuários e as demais informações do sistema."
  } ],
  "regularExpressions" : [ ],
  "databaseConfigurations" : [ ],
  "queryConfigurations" : [ ],
  "lastIds" : {
    "UseCase" : 1,
    "Step" : 17,
    "Element" : 13,
    "Flow" : 1,
    "Actor" : 1
  }
}