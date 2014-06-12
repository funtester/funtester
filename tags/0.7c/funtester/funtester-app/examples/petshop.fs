{
  "name" : "PetShop",
  "actors" : [ {
    "id" : 1,
    "name" : "Administrador",
    "description" : ""
  } ],
  "regularExpressions" : [ ],
  "databaseConfigurations" : [ {
    "id" : 1,
    "name" : "petshop",
    "driver" : "com.mysql.jdbc.Driver",
    "type" : "mysql",
    "dialect" : "",
    "host" : "127.0.0.1",
    "port" : 3306,
    "path" : "petshop",
    "user" : "root",
    "password" : ""
  }, {
    "id" : 2,
    "name" : "petshoptest",
    "driver" : "com.mysql.jdbc.Driver",
    "type" : "mysql",
    "dialect" : "",
    "host" : "127.0.0.1",
    "port" : 3306,
    "path" : "petshoptest",
    "user" : "root",
    "password" : ""
  } ],
  "queryConfigurations" : [ {
    "id" : 1,
    "databaseConfig" : 1,
    "name" : "Usuários",
    "command" : "SELECT user FROM usuarios"
  }, {
    "id" : 2,
    "databaseConfig" : 1,
    "name" : "Senhas",
    "command" : "SELECT senha FROM usuarios WHERE user = ?"
  } ],
  "useCases" : [ {
    "id" : 1,
    "name" : "Acessar Tela Principal",
    "elements" : [ {
      "id" : 1,
      "name" : "Janela Principal",
      "internalName" : "JanelaPrincipal",
      "type" : 8,
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
      } ],
      "postconditions" : [ ]
    } ],
    "actors" : [ 1 ],
    "preconditions" : [ ],
    "ignoreToGenerateTests" : true,
    "description" : "",
    "includeFiles" : [ {
      "name" : "petshop.gui.JanelaPrincipal"
    } ],
    "databaseScripts" : [ ]
  }, {
    "id" : 2,
    "name" : "Efetuar Login",
    "elements" : [ {
      "id" : 2,
      "name" : "Tela de Login",
      "internalName" : "JanelaLogin",
      "type" : 8,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 3,
      "name" : "nome de usuário",
      "internalName" : "usuario",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 1,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Preencha o campo de %s.",
        "valueConfiguration" : null
      }, {
        "id" : 2,
        "type" : "ONE_OF",
        "importance" : "MEDIUM",
        "message" : "Usuario invalido",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.QueryBasedVC",
          "id" : 1,
          "queryConfig" : 1,
          "targetColumn" : "user",
          "targetColumnType" : "STRING",
          "parameters" : [ ]
        }
      } ]
    }, {
      "id" : 4,
      "name" : "senha de usuário",
      "internalName" : "senha",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 3,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Preencha o campo de %s.",
        "valueConfiguration" : null
      }, {
        "id" : 4,
        "type" : "ONE_OF",
        "importance" : "MEDIUM",
        "message" : "Senha Invalida",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.QueryBasedVC",
          "id" : 2,
          "queryConfig" : 2,
          "targetColumn" : "senha",
          "targetColumnType" : "STRING",
          "parameters" : [ {
            "valueConfiguration" : {
              "@class" : "org.funtester.core.software.ElementBasedVC",
              "id" : 3,
              "referencedElementId" : 3
            },
            "valueType" : "STRING"
          } ]
        }
      } ]
    }, {
      "id" : 5,
      "name" : "Entrar",
      "internalName" : "entrar",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 2,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "HIGH",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 2,
        "elements" : [ 2 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 3,
        "elements" : [ 3, 4 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 4,
        "elements" : [ 5 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.OracleStep",
        "id" : 5,
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
        "description" : "login efetuado",
        "ownerFlow" : 2
      } ]
    } ],
    "actors" : [ 1 ],
    "preconditions" : [ ],
    "ignoreToGenerateTests" : false,
    "description" : "Autentica um usuário, permitindo ou negando seu acesso ao sistema.",
    "includeFiles" : [ {
      "name" : "petshop.gui.JanelaLogin"
    } ],
    "databaseScripts" : [ ]
  } ],
  "lastIds" : {
    "Flow" : 2,
    "Step" : 6,
    "DatabaseConfig" : 2,
    "UseCase" : 2,
    "Actor" : 1,
    "ConditionState" : 1,
    "BusinessRule" : 5,
    "Element" : 5,
    "QueryConfig" : 2,
    "ValueConfiguration" : 6
  }
}