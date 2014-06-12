{
  "id" : 1,
  "name" : "Desktop GUI Profile",
  "description" : "Desktop GUI Profile",
  "types" : [ {
    "id" : 2,
    "name" : "key",
    "kind" : "KEY_COMBINATION",
	"editable" : true
  }, {
    "id" : 3,
    "name" : "combobox",
    "kind" : "WIDGET",
	"editable" : true
  }, {
    "id" : 4,
    "name" : "button",
    "kind" : "WIDGET",
	"editable" : false
  }, {
    "id" : 5,
    "name" : "textbox",
    "kind" : "WIDGET",
	"editable" : true
  }, {
    "id" : 6,
    "name" : "dialog",
    "kind" : "WIDGET",
	"editable" : false
  }, {
    "id" : 7,
    "name" : "menu",
    "kind" : "WIDGET",
	"editable" : false
  }, {
    "id" : 8,
    "name" : "frame",
    "kind" : "WIDGET",
	"editable" : false
  }, {
    "id" : 9,
    "name" : "message",
    "kind" : "WIDGET",
	"editable" : false
  } ],
  "actions" : [ {
    "id" : 1,
    "name" : "call",
    "trigger" : "SYSTEM",
    "kind" : "USE_CASE_CALL",
    "maxElements" : 1
  }, {
    "id" : 2,
    "name" : "press",
    "trigger" : "ACTOR",
    "kind" : "ACTION",
    "maxElements" : 1000
  }, {
    "id" : 3,
    "name" : "check",
    "trigger" : "SYSTEM",
    "kind" : "ORACLE",
    "maxElements" : 1000
  }, {
    "id" : 4,
    "name" : "select",
    "trigger" : "ACTOR",
    "kind" : "ACTION",
    "maxElements" : 1000,
	"makeElementsEditable" : true
  }, {
    "id" : 5,
    "name" : "answer.yes",
    "trigger" : "SYSTEM",
    "kind" : "ACTION",
    "maxElements" : 1
  }, {
    "id" : 6,
    "name" : "click",
    "trigger" : "ACTOR",
    "kind" : "ACTION",
    "maxElements" : 1000
  }, {
    "id" : 7,
    "name" : "show",
    "trigger" : "SYSTEM",
    "kind" : "ACTION",
    "maxElements" : 1
  }, {
    "id" : 8,
    "name" : "type",
    "trigger" : "ACTOR",
    "kind" : "ACTION",
    "maxElements" : 1000,
	"makeElementsEditable" : true
  }, {
    "id" : 9,
    "name" : "select.first",
    "trigger" : "ACTOR",
    "kind" : "ACTION",
    "maxElements" : 1000,
	"makeElementsEditable" : true
  }, {
    "id" : 10,
    "name" : "close",
    "trigger" : "SYSTEM",
    "kind" : "ACTION",
    "maxElements" : 1
  } ]
}