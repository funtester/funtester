<#list test.headerComments as comment>
// ${comment}
</#list>

package ${test.namespace};

<#list test.imports as import>
import ${import};
</#list>
<#list annotation.imports as import>
import ${import};
</#list>

/**
<#list test.comments as comment>
 * ${comment}
</#list>
 */
${annotation.testCase} <#if annotation.categoryStart?? >${annotation.categoryStart}</#if>
<#list test.categories as cat>
  <#if annotation.categoryEachStart?? >${annotation.categoryEachStart}</#if><#if ( cat_index > 0 ) && annotation.categoryEachSeparator?? >${annotation.categoryEachSeparator}</#if>"${cat}"<#if annotation.categoryEachEnd?? >${annotation.categoryEachEnd}</#if>
</#list>
<#if annotation.categoryEnd?? >${annotation.categoryEnd}</#if>
public class ${test.name} {

	<#if ( test.attributes?size > 0 ) >
	<#list test.attributes as var>
	private ${var.type} ${var.name};
	</#list>
	</#if>
	
	//
	// HELPER METHODS
	//
	<#list test.helperMethods as helper>
	
	public ${helper.returnType} ${helper.name}(<#list helper.args as var><#if ( var_index >= 1 ) >, </#if>${var.type} ${var.name}</#list>) {
		<#list helper.commands as cmd>
		${cmd}
		</#list>
	}
	</#list>
	
	//
	// SETUP AND TEAR DOWN
	//
	<#if ( test.setUpOnceCommands?size > 0 ) >
	
	${annotation.setUpOnce}
	public void setUpOnce() {
		<#list test.setUpOnceCommands as cmd>
		${cmd}
		</#list>
	}
	</#if>
	<#if ( test.tearDownOnceCommands?size > 0 ) >
	
	${annotation.tearDownOnce}
	public void tearDownOnce() {
		<#list test.tearDownOnceCommands as cmd>
		${cmd}
		</#list>
	}
	</#if>
	<#if ( test.setUpCommands?size > 0 ) >
	
	${annotation.setUp}
	public void setUp() {
		<#list test.setUpCommands as cmd>
		${cmd}
		</#list>
	}
	</#if>
	<#if ( test.tearDownCommands?size > 0 ) >
	
	${annotation.tearDown}
	public void tearDown() {
		<#list test.tearDownCommands as cmd>
		${cmd}
		</#list>
	}
	</#if>
	
	//
	// TESTS
	//
	<#list test.methods as method>
	
	${annotation.testMethod}
	public void ${method.name}() {
		<#list method.commands as cmd>
		${cmd}
		</#list>
	}
	</#list>
	
}