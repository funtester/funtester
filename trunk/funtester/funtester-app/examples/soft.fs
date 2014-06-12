{
  "name" : "My s1",
  "actors" : [ {
    "id" : 1,
    "name" : "Administrador",
    "description" : "Responsável por manter outros usuários e as demais informações do sistema."
  } ],
  "regularExpressions" : [ {
    "id" : 1,
    "name" : "nome",
    "expression" : "[A-Za-z0-9 .-]{2,50}"
  }, {
    "id" : 2,
    "name" : "endereço",
    "expression" : "[A-Za-z--9 ,.-]{0,100}"
  } ],
  "databaseConfigurations" : [ {
    "id" : 1,
    "name" : "sicnf",
    "driver" : "com.mysql.jdbc.Driver",
    "dialect" : "",
    "type" : "mysql",
    "host" : "127.0.0.1",
    "port" : 3306,
    "path" : "sicnf",
    "user" : "root",
    "password" : ""
  } ],
  "queryConfigurations" : [ {
    "id" : 1,
    "databaseConfig" : 1,
    "name" : "cidades de um estado",
    "command" : "SELECT * FROM cidade WHERE estado_id = ?"
  }, {
    "id" : 2,
    "databaseConfig" : 1,
    "name" : "estados",
    "command" : "SELECT * FROM estado"
  }, {
    "id" : 3,
    "databaseConfig" : 1,
    "name" : "estado com nome",
    "command" : "SELECT * FROM estado WHERE nome = ?"
  }, {
    "id" : 4,
    "databaseConfig" : 1,
    "name" : "logins de usuário",
    "command" : "SELECT login FROM usuario"
  }, {
    "id" : 5,
    "databaseConfig" : 1,
    "name" : "senhas para um login",
    "command" : "SELECT senha FROM usuario WHERE login = ?"
  } ],
  "useCases" : [ {
    "id" : 5,
    "name" : "Efetuar Login",
    "elements" : [ {
      "id" : 30,
      "name" : "Janela de Login",
      "internalName" : "LoginDialog",
      "type" : 6,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 31,
      "name" : "Login",
      "internalName" : "login",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 8,
        "type" : "ONE_OF",
        "message" : "Usuário ou senha inválidos",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.QueryBasedVC",
          "id" : 10,
          "queryConfig" : 2,
          "targetColumn" : "",
          "targetColumnType" : "STRING",
          "parameters" : [ ]
        }
      } ]
    }, {
      "id" : 32,
      "name" : "Senha",
      "internalName" : "senha",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 9,
        "type" : "ONE_OF",
        "message" : "Usuário ou senha inválidos.",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.QueryBasedVC",
          "id" : 12,
          "queryConfig" : 5,
          "targetColumn" : "",
          "targetColumnType" : "STRING",
          "parameters" : [ {
            "valueConfiguration" : {
              "@class" : "org.funtester.core.software.ElementBasedVC",
              "id" : 11,
              "referencedElementId" : 31
            },
            "valueType" : "STRING"
          } ]
        }
      } ]
    }, {
      "id" : 33,
      "name" : "Entrar",
      "internalName" : "entrarButton",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 9,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 45,
        "elements" : [ 30 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 46,
        "elements" : [ 31, 32 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 47,
        "elements" : [ 33 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 48,
        "elements" : [ 30 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 9
      } ],
      "postconditions" : [ ]
    } ],
    "actors" : [ 1 ],
    "preconditions" : [ ],
    "activationFlows" : [ ],
    "description" : "",
    "includeFiles" : [ ],
    "databaseScripts" : [ ],
    "onlyStartedByAnotherUseCase" : false
  }, {
    "id" : 1,
    "name" : "my uc1",
    "elements" : [ {
      "id" : 7,
      "name" : "My Field",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 13,
      "name" : "AnotherField",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 11,
      "name" : "JustLikeAnyOtherField",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 20,
      "name" : "TwoField",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 6,
      "name" : "ReFazer",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 14,
      "name" : "A New Field",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 1,
      "name" : "OK",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 9,
      "name" : "SomeButton",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 2,
      "name" : "Janela de Algo",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 15,
      "name" : "Nome",
      "internalName" : "",
      "type" : null,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 19,
      "name" : "OneField",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 17,
      "name" : "Endereço",
      "internalName" : "",
      "type" : null,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 4,
      "name" : "Testar",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 10,
      "name" : "JustAnotherField",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 3,
      "name" : "Cancelar",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 16,
      "name" : "Telefone",
      "internalName" : "",
      "type" : null,
      "editable" : true,
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
        "elements" : [ 13, 4, 3, 1 ],
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
        "elements" : [ 10, 1 ],
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
      "postconditions" : [ {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 1,
        "description" : "aaa",
        "ownerFlow" : 1
      }, {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 2,
        "description" : "bbb",
        "ownerFlow" : 1
      } ]
    }, {
      "@class" : "org.funtester.core.software.TerminatorFlow",
      "id" : 2,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 18,
        "elements" : [ 13 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 33,
        "elements" : [ 15, 17, 16 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 19,
        "elements" : [ 4 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.OracleStep",
        "id" : 34,
        "elements" : [ 16, 15 ],
        "actionNickname" : 2,
        "messageOccurrence" : null,
        "elementType" : null
      }, {
        "@class" : "org.funtester.core.software.OracleStep",
        "id" : 35,
        "elements" : [ 16 ],
        "actionNickname" : 2,
        "messageOccurrence" : null,
        "elementType" : null
      } ],
      "postconditions" : [ {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 3,
        "description" : "ccc",
        "ownerFlow" : 2
      } ],
      "description" : "Second flow",
      "starterFlow" : 1,
      "starterStep" : 8,
      "influencingFlows" : [ ]
    }, {
      "@class" : "org.funtester.core.software.TerminatorFlow",
      "id" : 3,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 20,
        "elements" : [ 14 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 21,
        "elements" : [ 3 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      } ],
      "postconditions" : [ ],
      "description" : "Third Flow",
      "starterFlow" : 2,
      "starterStep" : 18,
      "influencingFlows" : [ ]
    }, {
      "@class" : "org.funtester.core.software.ReturnableFlow",
      "id" : 4,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 27,
        "elements" : [ 17, 16, 15 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 28,
        "elements" : [ 3 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      } ],
      "postconditions" : [ ],
      "description" : "Fourth Flow",
      "starterFlow" : 1,
      "starterStep" : 1,
      "influencingFlows" : [ ],
      "returningFlow" : 1,
      "returningStep" : 3,
      "recursive" : false
    }, {
      "@class" : "org.funtester.core.software.ReturnableFlow",
      "id" : 5,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "steps" : [ ],
      "postconditions" : [ ],
      "description" : "Fifth Flow",
      "starterFlow" : 1,
      "starterStep" : 1,
      "influencingFlows" : [ ],
      "returningFlow" : 1,
      "returningStep" : 3,
      "recursive" : false
    }, {
      "@class" : "org.funtester.core.software.ReturnableFlow",
      "id" : 7,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 31,
        "elements" : [ 20, 19 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 32,
        "elements" : [ 4 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      } ],
      "postconditions" : [ ],
      "description" : "Sixth Flow",
      "starterFlow" : 1,
      "starterStep" : 1,
      "influencingFlows" : [ ],
      "returningFlow" : 1,
      "returningStep" : 7,
      "recursive" : false
    } ],
    "actors" : [ ],
    "preconditions" : [ ],
    "activationFlows" : [ ],
    "description" : "",
    "includeFiles" : [ ],
    "databaseScripts" : [ ],
    "onlyStartedByAnotherUseCase" : false
  }, {
    "id" : 2,
    "name" : "my uc2",
    "elements" : [ {
      "id" : 25,
      "name" : "OK",
      "internalName" : "okButton",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 24,
      "name" : "Telefone",
      "internalName" : "telefoneField",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 1,
        "type" : "MIN_LENGTH",
        "message" : "O telefone deve ter pelo menos %d caracteres.",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.SingleVC",
          "id" : 1,
          "value" : "8"
        }
      }, {
        "id" : 2,
        "type" : "MAX_LENGTH",
        "message" : "O telefone deve ter no máximo %d caracteres.",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.SingleVC",
          "id" : 2,
          "value" : "10"
        }
      }, {
        "id" : 3,
        "type" : "REQUIRED",
        "message" : "O telefone deve ser informado.",
        "valueConfiguration" : null
      } ]
    }, {
      "id" : 21,
      "name" : "Janela XPTO",
      "internalName" : "JanelaXPTO",
      "type" : 6,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 23,
      "name" : "Endereço",
      "internalName" : "enderecoField",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 5,
        "type" : "REG_EX",
        "message" : "O endereço deve ...",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.RegExBasedVC",
          "id" : 4,
          "regEx" : 2
        }
      } ]
    }, {
      "id" : 22,
      "name" : "Nome",
      "internalName" : "nomeField",
      "type" : 3,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 4,
        "type" : "ONE_OF",
        "message" : "O valor do nome deve pertencer à um dos valores da lista.",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.MultiVC",
          "id" : 3,
          "values" : [ "Bob", "Bruce", "John" ]
        }
      } ]
    }, {
      "id" : 27,
      "name" : "Cidade",
      "internalName" : "cidadeCombo",
      "type" : 3,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 7,
        "type" : "ONE_OF",
        "message" : "A cidade deve pertencer à lista de cidades do estado selecionado.",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.QueryBasedVC",
          "id" : 6,
          "queryConfig" : 1,
          "targetColumn" : "id",
          "targetColumnType" : "STRING",
          "parameters" : [ {
            "valueConfiguration" : {
              "@class" : "org.funtester.core.software.QueryBasedVC",
              "id" : 9,
              "queryConfig" : 3,
              "targetColumn" : "id",
              "targetColumnType" : "STRING",
              "parameters" : [ {
                "valueConfiguration" : {
                  "@class" : "org.funtester.core.software.ElementBasedVC",
                  "id" : 8,
                  "referencedElementId" : 29
                },
                "valueType" : "STRING"
              } ]
            },
            "valueType" : "STRING"
          } ]
        }
      } ]
    }, {
      "id" : 29,
      "name" : "Estado",
      "internalName" : "estadoCombo",
      "type" : 3,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 6,
        "type" : "ONE_OF",
        "message" : "Por favor, informe um estado pertencente à lista de estados.",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.QueryBasedVC",
          "id" : 5,
          "queryConfig" : 2,
          "targetColumn" : "",
          "targetColumnType" : "STRING",
          "parameters" : [ ]
        }
      } ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 6,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 36,
        "elements" : [ 21 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 37,
        "elements" : [ 22, 23, 24 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 44,
        "elements" : [ 29 ],
        "trigger" : "ACTOR",
        "actionNickname" : 4
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 41,
        "elements" : [ 27 ],
        "trigger" : "ACTOR",
        "actionNickname" : 4
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 38,
        "elements" : [ 25 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      } ],
      "postconditions" : [ {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 4,
        "description" : "ddd",
        "ownerFlow" : 6
      }, {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 10,
        "description" : "eee",
        "ownerFlow" : 6
      } ]
    } ],
    "actors" : [ ],
    "preconditions" : [ ],
    "activationFlows" : [ ],
    "description" : "",
    "includeFiles" : [ ],
    "databaseScripts" : [ ],
    "onlyStartedByAnotherUseCase" : false
  }, {
    "id" : 3,
    "name" : "my uc3",
    "elements" : [ ],
    "flows" : [ ],
    "actors" : [ ],
    "preconditions" : [ {
      "@class" : "org.funtester.core.software.Postcondition",
      "id" : 8,
      "description" : "ccc",
      "ownerFlow" : 2
    }, {
      "@class" : "org.funtester.core.software.Precondition",
      "id" : 9,
      "description" : "ggg"
    } ],
    "activationFlows" : [ ],
    "description" : "",
    "includeFiles" : [ ],
    "databaseScripts" : [ ],
    "onlyStartedByAnotherUseCase" : false
  }, {
    "id" : 4,
    "name" : "my uc4",
    "elements" : [ ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 8,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "steps" : [ ],
      "postconditions" : [ {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 11,
        "description" : "uc4 bf pos1",
        "ownerFlow" : 8
      } ]
    } ],
    "actors" : [ ],
    "preconditions" : [ ],
    "activationFlows" : [ ],
    "description" : "",
    "includeFiles" : [ ],
    "databaseScripts" : [ ],
    "onlyStartedByAnotherUseCase" : false
  } ],
  "lastIds" : {
    "UseCase" : 5,
    "Step" : 48,
    "Flow" : 9,
    "Actor" : 1,
    "DatabaseConfig" : 1,
    "QueryConfig" : 5,
    "Element" : 33,
    "BusinessRule" : 9,
    "ValueConfiguration" : 12,
    "RegEx" : 2,
    "ConditionState" : 11
  }
}