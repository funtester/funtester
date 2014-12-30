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
    "id" : 6,
    "name" : "Acessar Sistema",
    "elements" : [ {
      "id" : 34,
      "name" : "Login",
      "internalName" : "loginButton",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 35,
      "name" : "Janela Principal",
      "internalName" : "MainDialog",
      "type" : 6,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 10,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.DocStep",
        "id" : 51,
        "trigger" : "SYSTEM",
        "sentence" : "carrega a configuração"
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 53,
        "elements" : [ 35 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 52,
        "elements" : [ 34 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.UseCaseCallStep",
        "id" : 50,
        "actionNickname" : 5,
        "referencedUseCaseId" : 5
      } ],
      "postconditions" : [ ]
    } ],
    "actors" : [ ],
    "preconditions" : [ ],
    "ignoreToGenerateTests" : true,
    "description" : "",
    "includeFiles" : [ {
      "name" : "br.com.app.iu.MainDialog"
    } ],
    "databaseScripts" : [ ]
  }, {
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
          "queryConfig" : 4,
          "targetColumn" : "login",
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
          "targetColumn" : "senha",
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
    "ignoreToGenerateTests" : false,
    "description" : "",
    "includeFiles" : [ {
      "name" : "br.com.app.iu.LoginDialog"
    } ],
    "databaseScripts" : [ ]
  } ],
  "lastIds" : {
    "UseCase" : 6,
    "Step" : 53,
    "Flow" : 10,
    "Actor" : 1,
    "DatabaseConfig" : 1,
    "QueryConfig" : 5,
    "Element" : 35,
    "BusinessRule" : 9,
    "ValueConfiguration" : 12,
    "RegEx" : 2,
    "ConditionState" : 11
  }
}