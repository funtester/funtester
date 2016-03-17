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
    "id" : 9,
    "name" : "Acessar Tela Principal",
    "elements" : [ {
      "id" : 54,
      "name" : "Janela Principal",
      "internalName" : "JanelaPrincipal",
      "type" : 6,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 55,
      "name" : "Clientes",
      "internalName" : "clientes",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 56,
      "name" : "Novo",
      "internalName" : "novo",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 57,
      "name" : "Produtos",
      "internalName" : "produtos",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 58,
      "name" : "Vendas",
      "internalName" : "vendas",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 12,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.DocStep",
        "id" : 80,
        "trigger" : "SYSTEM",
        "sentence" : "carrega as configurações."
      }, {
        "@class" : "org.funtester.core.software.UseCaseCallStep",
        "id" : 81,
        "actionNickname" : 5,
        "referencedUseCaseId" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 65,
        "elements" : [ 54 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.DocStep",
        "id" : 66,
        "trigger" : "SYSTEM",
        "sentence" : "aguarda a interação do usuário"
      } ],
      "postconditions" : [ {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 11,
        "description" : "Tela principal acessada.",
        "ownerFlow" : 12
      } ]
    }, {
      "@class" : "org.funtester.core.software.TerminatorFlow",
      "id" : 13,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 67,
        "elements" : [ 55 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 68,
        "elements" : [ 56 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.UseCaseCallStep",
        "id" : 69,
        "actionNickname" : 5,
        "referencedUseCaseId" : 3
      } ],
      "postconditions" : [ ],
      "description" : "Cadastrar Cliente",
      "starterFlow" : 12,
      "starterStep" : 66,
      "influencingFlows" : [ ]
    }, {
      "@class" : "org.funtester.core.software.TerminatorFlow",
      "id" : 14,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 70,
        "elements" : [ 57 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 71,
        "elements" : [ 56 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.UseCaseCallStep",
        "id" : 72,
        "actionNickname" : 5,
        "referencedUseCaseId" : 4
      } ],
      "postconditions" : [ ],
      "description" : "Cadastrar Produto",
      "starterFlow" : 12,
      "starterStep" : 66,
      "influencingFlows" : [ ]
    }, {
      "@class" : "org.funtester.core.software.TerminatorFlow",
      "id" : 16,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 76,
        "elements" : [ 58 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 77,
        "elements" : [ 56 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.UseCaseCallStep",
        "id" : 78,
        "actionNickname" : 5,
        "referencedUseCaseId" : 5
      } ],
      "postconditions" : [ ],
      "description" : "Efetuar Venda",
      "starterFlow" : 12,
      "starterStep" : 66,
      "influencingFlows" : [ ]
    } ],
    "actors" : [ ],
    "preconditions" : [ ],
    "ignoreToGenerateTests" : true,
    "description" : "",
    "includeFiles" : [ {
      "name" : "petshop.gui.JanelaPrincipal"
    } ],
    "databaseScripts" : [ ]
  }, {
    "id" : 3,
    "name" : "Cadastrar Cliente",
    "elements" : [ {
      "id" : 6,
      "name" : "Janela de Cadastro de Cliente",
      "internalName" : "JanelaCliente",
      "type" : 6,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 7,
      "name" : "Nome",
      "internalName" : "nome",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 11,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Você esqueceu de preencher os seguintes campos obrigatórios: %s",
        "valueConfiguration" : null
      } ]
    }, {
      "id" : 8,
      "name" : "Sexo",
      "internalName" : "sexo",
      "type" : 3,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 12,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Você esqueceu de preencher os seguintes campos obrigatórios: %s",
        "valueConfiguration" : null
      } ]
    }, {
      "id" : 9,
      "name" : "Rua",
      "internalName" : "rua",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 13,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Você esqueceu de preencher os seguintes campos obrigatórios: %s",
        "valueConfiguration" : null
      } ]
    }, {
      "id" : 10,
      "name" : "Número",
      "internalName" : "numero",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 11,
      "name" : "Complemento",
      "internalName" : "complemento",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 12,
      "name" : "Bairro",
      "internalName" : "bairro",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 13,
      "name" : "Cidade",
      "internalName" : "cidade",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 14,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Você esqueceu de preencher os seguintes campos obrigatórios: %s",
        "valueConfiguration" : null
      } ]
    }, {
      "id" : 14,
      "name" : "UF",
      "internalName" : "uf",
      "type" : 3,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 15,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Você esqueceu de preencher os seguintes campos obrigatórios: %s",
        "valueConfiguration" : null
      } ]
    }, {
      "id" : 15,
      "name" : "CEP",
      "internalName" : "cep",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 16,
      "name" : "RG",
      "internalName" : "rg",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 16,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Você esqueceu de preencher os seguintes campos obrigatórios: %s",
        "valueConfiguration" : null
      } ]
    }, {
      "id" : 17,
      "name" : "CPF",
      "internalName" : "cpf",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 17,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Você esqueceu de preencher os seguintes campos obrigatórios: %s",
        "valueConfiguration" : null
      } ]
    }, {
      "id" : 18,
      "name" : "Email",
      "internalName" : "email",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 19,
      "name" : "Telefone",
      "internalName" : "telefone",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 18,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Você esqueceu de preencher os seguintes campos obrigatórios: %s",
        "valueConfiguration" : null
      } ]
    }, {
      "id" : 20,
      "name" : "Celular",
      "internalName" : "celular",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 19,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Você esqueceu de preencher os seguintes campos obrigatórios: %s",
        "valueConfiguration" : null
      } ]
    }, {
      "id" : 21,
      "name" : "Animal",
      "internalName" : "animal",
      "type" : 3,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 30,
      "name" : "Cadastrar",
      "internalName" : "cadastrar",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 59,
      "name" : "Cancelar",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 60,
      "name" : "Tem certeza que deseja cancelar?",
      "internalName" : "",
      "type" : null,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 3,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 7,
        "elements" : [ 6 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 8,
        "elements" : [ 7 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 9,
        "elements" : [ 8 ],
        "trigger" : "ACTOR",
        "actionNickname" : 4
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 10,
        "elements" : [ 9, 10, 11, 12, 13 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 11,
        "elements" : [ 14 ],
        "trigger" : "ACTOR",
        "actionNickname" : 4
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 12,
        "elements" : [ 15, 16, 17, 18, 19, 20 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 13,
        "elements" : [ 21 ],
        "trigger" : "ACTOR",
        "actionNickname" : 4
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 17,
        "elements" : [ 30 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      } ],
      "postconditions" : [ {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 2,
        "description" : "Cliente cadastrado",
        "ownerFlow" : 3
      } ]
    }, {
      "@class" : "org.funtester.core.software.CancelatorFlow",
      "id" : 17,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 82,
        "elements" : [ 59 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 83,
        "elements" : [ 60 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 3
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 84,
        "elements" : [ 6 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 9
      } ],
      "postconditions" : [ ],
      "description" : "Cancelar",
      "starterFlow" : 3,
      "starterStep" : 17,
      "influencingFlows" : [ ]
    } ],
    "actors" : [ ],
    "preconditions" : [ {
      "@class" : "org.funtester.core.software.Postcondition",
      "id" : 12,
      "description" : "Tela principal acessada.",
      "ownerFlow" : 12
    } ],
    "ignoreToGenerateTests" : false,
    "description" : "",
    "includeFiles" : [ {
      "name" : "petshop.gui.JanelaCliente"
    } ],
    "databaseScripts" : [ ]
  }, {
    "id" : 4,
    "name" : "Cadastrar Produto",
    "elements" : [ {
      "id" : 22,
      "name" : "Código",
      "internalName" : "codigo",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 23,
      "name" : "Nome",
      "internalName" : "nome",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 24,
      "name" : "Estoque",
      "internalName" : "estoque",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 25,
      "name" : "Preço de Custo",
      "internalName" : "precoCusto",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 26,
      "name" : "Preço de Venda",
      "internalName" : "precoVenda",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 27,
      "name" : "Informaões Adicionais",
      "internalName" : "informacoes",
      "type" : 23,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 28,
      "name" : "Janela de Cadastro de Produto",
      "internalName" : "JanelaProduto",
      "type" : 6,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 29,
      "name" : "Cadastrar",
      "internalName" : "cadastrar",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 4,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 15,
        "elements" : [ 28 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 14,
        "elements" : [ 22, 23, 24, 25, 26, 27 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 16,
        "elements" : [ 29 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.OracleStep",
        "id" : 31,
        "elements" : [ 22, 23, 24, 25, 26, 27 ],
        "actionNickname" : 2,
        "messageOccurrence" : null,
        "elementType" : null
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 30,
        "elements" : [ 28 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 9
      } ],
      "postconditions" : [ {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 3,
        "description" : "Produto cadastrado.",
        "ownerFlow" : 4
      } ]
    } ],
    "actors" : [ ],
    "preconditions" : [ {
      "@class" : "org.funtester.core.software.Postcondition",
      "id" : 13,
      "description" : "Tela principal acessada.",
      "ownerFlow" : 12
    } ],
    "ignoreToGenerateTests" : false,
    "description" : "",
    "includeFiles" : [ {
      "name" : "petshop.gui.JanelaProduto"
    } ],
    "databaseScripts" : [ ]
  }, {
    "id" : 5,
    "name" : "Efetuar Venda",
    "elements" : [ {
      "id" : 31,
      "name" : "Janela de Venda",
      "internalName" : "JanelaVenda",
      "type" : 6,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 33,
      "name" : "Pesquisar",
      "internalName" : "pesquisarCliente",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 35,
      "name" : "Tipo de Item",
      "internalName" : "tipoItem",
      "type" : 3,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 6,
        "type" : "ONE_OF",
        "importance" : "MEDIUM",
        "message" : "Informe o Tipo de Item.",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.MultiVC",
          "id" : 7,
          "values" : [ "PRODUTO", "SERVIÇO" ]
        }
      } ]
    }, {
      "id" : 36,
      "name" : "Escolher Produto",
      "internalName" : "pesquisarProduto",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 37,
      "name" : "Quantidade",
      "internalName" : "quantidade",
      "type" : 5,
      "editable" : true,
      "valueType" : "INTEGER",
      "businessRules" : [ ]
    }, {
      "id" : 38,
      "name" : "Pagar",
      "internalName" : "pagar",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 5,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 18,
        "elements" : [ 31 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 20,
        "elements" : [ 33 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.UseCaseCallStep",
        "id" : 21,
        "actionNickname" : 5,
        "referencedUseCaseId" : 6
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 23,
        "elements" : [ 35 ],
        "trigger" : "ACTOR",
        "actionNickname" : 4
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 24,
        "elements" : [ 36 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.UseCaseCallStep",
        "id" : 25,
        "actionNickname" : 5,
        "referencedUseCaseId" : 7
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 26,
        "elements" : [ 37 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 27,
        "elements" : [ 38 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.OracleStep",
        "id" : 28,
        "elements" : [ 35, 37 ],
        "actionNickname" : 2,
        "messageOccurrence" : null,
        "elementType" : null
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 29,
        "elements" : [ 31 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 9
      } ],
      "postconditions" : [ {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 4,
        "description" : "Venda realizada.",
        "ownerFlow" : 5
      } ]
    } ],
    "actors" : [ ],
    "preconditions" : [ {
      "@class" : "org.funtester.core.software.Postcondition",
      "id" : 14,
      "description" : "Tela principal acessada.",
      "ownerFlow" : 12
    } ],
    "ignoreToGenerateTests" : false,
    "description" : "",
    "includeFiles" : [ {
      "name" : "petshop.gui.JanelaVenda"
    } ],
    "databaseScripts" : [ ]
  }, {
    "id" : 8,
    "name" : "Login",
    "elements" : [ {
      "id" : 49,
      "name" : "Janela de Login",
      "internalName" : "JanelaLogin",
      "type" : 8,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 50,
      "name" : "Usuário",
      "internalName" : "usuario",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 7,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Preencha o campo de %s.",
        "valueConfiguration" : null
      }, {
        "id" : 9,
        "type" : "ONE_OF",
        "importance" : "MEDIUM",
        "message" : "Usuario invalido.",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.QueryBasedVC",
          "id" : 8,
          "queryConfig" : 1,
          "targetColumn" : "user",
          "targetColumnType" : "STRING",
          "parameters" : [ ]
        }
      } ]
    }, {
      "id" : 51,
      "name" : "Senha",
      "internalName" : "senha",
      "type" : 5,
      "editable" : true,
      "valueType" : "STRING",
      "businessRules" : [ {
        "id" : 8,
        "type" : "REQUIRED",
        "importance" : "MEDIUM",
        "message" : "Preencha o campo de %s.",
        "valueConfiguration" : null
      }, {
        "id" : 10,
        "type" : "ONE_OF",
        "importance" : "MEDIUM",
        "message" : "Senha invalida.",
        "valueConfiguration" : {
          "@class" : "org.funtester.core.software.QueryBasedVC",
          "id" : 10,
          "queryConfig" : 2,
          "targetColumn" : "senha",
          "targetColumnType" : "STRING",
          "parameters" : [ {
            "valueConfiguration" : {
              "@class" : "org.funtester.core.software.ElementBasedVC",
              "id" : 9,
              "referencedElementId" : 50
            },
            "valueType" : "STRING"
          } ]
        }
      } ]
    }, {
      "id" : 52,
      "name" : "Entrar",
      "internalName" : "entrar",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 11,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 55,
        "elements" : [ 49 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 56,
        "elements" : [ 50, 51 ],
        "trigger" : "ACTOR",
        "actionNickname" : 8
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 57,
        "elements" : [ 52 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.OracleStep",
        "id" : 58,
        "elements" : [ 50, 51 ],
        "actionNickname" : 2,
        "messageOccurrence" : null,
        "elementType" : null
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 59,
        "elements" : [ 49 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 9
      } ],
      "postconditions" : [ {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 9,
        "description" : "Login efetuado.",
        "ownerFlow" : 11
      } ]
    } ],
    "actors" : [ ],
    "preconditions" : [ ],
    "ignoreToGenerateTests" : false,
    "description" : "",
    "includeFiles" : [ {
      "name" : "petshop.gui.JanelaLogin"
    } ],
    "databaseScripts" : [ ]
  }, {
    "id" : 6,
    "name" : "Pesquisar Cliente",
    "elements" : [ {
      "id" : 32,
      "name" : "Janela de Pesquisa de Cliente",
      "internalName" : "JanelaAdicionaCliente",
      "type" : 6,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 39,
      "name" : "Pesquisar",
      "internalName" : "pesquisar",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 40,
      "name" : "Lista de Clientes",
      "internalName" : "clientes",
      "type" : 39,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 41,
      "name" : "OK",
      "internalName" : "ok",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 6,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 19,
        "elements" : [ 32 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 32,
        "elements" : [ 39 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 33,
        "elements" : [ 40 ],
        "trigger" : "ACTOR",
        "actionNickname" : 7
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 34,
        "elements" : [ 41 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 35,
        "elements" : [ 32 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 9
      } ],
      "postconditions" : [ {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 5,
        "description" : "Cliente selecionado.",
        "ownerFlow" : 6
      } ]
    } ],
    "actors" : [ ],
    "preconditions" : [ ],
    "ignoreToGenerateTests" : true,
    "description" : "",
    "includeFiles" : [ {
      "name" : "petshop.gui.JanelaAdicionaCliente"
    } ],
    "databaseScripts" : [ ]
  }, {
    "id" : 7,
    "name" : "Pesquisar Produto",
    "elements" : [ {
      "id" : 34,
      "name" : "Janela de Pesquisa de Produto",
      "internalName" : "JanelaAdicionaProduto",
      "type" : 6,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 42,
      "name" : "Pesquisar",
      "internalName" : "pesquisar",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 43,
      "name" : "Lista de Produtos",
      "internalName" : "produtos",
      "type" : 39,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    }, {
      "id" : 44,
      "name" : "OK",
      "internalName" : "ok",
      "type" : 4,
      "editable" : false,
      "valueType" : "STRING",
      "businessRules" : [ ]
    } ],
    "flows" : [ {
      "@class" : "org.funtester.core.software.BasicFlow",
      "id" : 7,
      "priority" : "SHOULD",
      "complexity" : "MEDIUM",
      "frequency" : "MEDIUM",
      "importance" : "MEDIUM",
      "steps" : [ {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 22,
        "elements" : [ 34 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 10
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 36,
        "elements" : [ 42 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 37,
        "elements" : [ 43 ],
        "trigger" : "ACTOR",
        "actionNickname" : 7
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 38,
        "elements" : [ 44 ],
        "trigger" : "ACTOR",
        "actionNickname" : 6
      }, {
        "@class" : "org.funtester.core.software.ActionStep",
        "id" : 39,
        "elements" : [ 34 ],
        "trigger" : "SYSTEM",
        "actionNickname" : 9
      } ],
      "postconditions" : [ {
        "@class" : "org.funtester.core.software.Postcondition",
        "id" : 6,
        "description" : "Produto selecionado.",
        "ownerFlow" : 7
      } ]
    } ],
    "actors" : [ ],
    "preconditions" : [ ],
    "ignoreToGenerateTests" : true,
    "description" : "",
    "includeFiles" : [ {
      "name" : "petshop.gui.JanelaAdicionaProduto"
    } ],
    "databaseScripts" : [ ]
  } ],
  "lastIds" : {
    "Flow" : 17,
    "Step" : 84,
    "DatabaseConfig" : 2,
    "UseCase" : 9,
    "Actor" : 1,
    "ConditionState" : 17,
    "BusinessRule" : 19,
    "Element" : 60,
    "QueryConfig" : 2,
    "ValueConfiguration" : 10
  }
}