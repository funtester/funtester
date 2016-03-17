<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style.css" />
		<title>Print Preview</title>
	</head>
	<body>
		<table>
			<tr>
				<td class="withBkg" colspan="2">Software: ${software.name}</td>
			</tr>
			<tr>
				<td class="withBkg">Actor</td>
				<td class="withBkg">Description</td>
			</tr>
			<#list software.actors as actor>
				<tr>
					<td>${actor.name}</td>
					<td>${actor.description}</td>
				</tr>
			</#list>
		</table>
		<br/>
		<#list software.useCases?sort_by("id") as useCase>
		<br/>
		<table>
				<tr>
					<td colspan="2" class="withBkg">Use Case ${useCase.id} - ${useCase.name}</td>
				</tr>
				<tr>
					<td class="withBkg">Description</td>
					<td colspan="2">${useCase.description}</td>
				</tr>
				<tr>
					<td class="withBkg">Actors</td>
					<td>${useCase.actorsNames()}</td>
				</tr>

				<#if useCase.inclusions()?has_content>
				<tr>
						<td class="withBkg">Inclusions</td>
						<td>
							<#list useCase.inclusion() as inclusion>[Use Case ${inclusion.id}]<#if inclusion_has_next>, </#if> </#list>
						</td>

				</tr>
				</#if>
				<#if useCase.extensions()?has_content>
				<tr>
						<td class="withBkg">Extensions</td>
						<td>
							<#list useCase.extensions() as extension>Use Case ${extension.id}<#if extension_has_next>, </#if> </#list>
						</td>

				</tr>
				</#if>
				<tr>
				<#if useCase.preconditions?has_content >
					<td class="withBkg">Preconditions</td>
					<td>
						<table class="internalTb">
						<#list useCase.preconditions as precondition>
							<tr>
								<td>${precondition_index+1}-</td>
								<td>${precondition.description}</td>
							</tr>
						</#list>
						</table>
					</td>
				</tr>
				</#if>
				<#list useCase.flows as flow>
					<tr>
						<td class="withBkg">
							<#switch flow.type()>
								<#case "FLOW">
								Flow ${flow_index+1}
								<#break>
								<#case "BASIC">
								Basic Flow
								<#break>
								<#case "TERMINATOR">
								Terminator Flow
								<#break>
								<#case "CANCELATOR">
								Cancelator Flow
								<#break>
								<#case "RETURNABLE">
								Returnable Flow
								<#break>
							</#switch>
						</td>
						<td>
								<table class="internalTb">
									<#list flow.steps as step>
										<tr>
											<td>${step_index+1}-</td>
											<td>${step.asSentence()}</td>
										</tr>
									</#list>
								</table>
						</td>
					</tr>
					<#if flow.postconditions?has_content>
					<tr>
						<td class="withBkg">Postconditions</td>
						<td>
							<table class="internalTb">
								<#list flow.postconditions as postcondition>
								<tr>
									<td>${postcondition_index+1}-</td>
									<td>${postcondition.description}</td>
									</tr>
								</#list>
							</table>
						</td>
					</tr>
					</#if>
				</#list>
				<#if useCase.elements?has_content>
					<tr>
						<td class="withBkg">Business Rules</td>
						<td class="noborder">
							<table class="internalTb">
								<tr class="withBkg2">
									<td class="withBkg2">Element</td>
									<td class="withBkg2">Restriction</td>
									<td class="withBkg2">Value</td>
									<td class="withBkg2">Expected system behavior/Message to be displayed</td>
								</tr>
								<#list useCase.elements as element>
									<#list element.businessRules as businessRule>
								    	<tr class="withborder">
											<td class="withborder">${element.name}</td>
											<td class="withborder">
												<#switch businessRule.type>
  													<#case "MIN_VALUE">
  													Minimum value
   													<#break>
  													<#case "MAX_VALUE">
  													Maximum value
    												<#break>
    												<#case "MIN_LENGTH">
    												Minimum value length
    												<#break>
    												<#case "MAX_LENGTH">
    												Maximum value length
    												<#break>
    												<#case "REQUIRED">
    												Required
    												<#break>
    												<#case "REG_EX">
    												Regular expression
    												<#break>
    												<#case "EQUAL_TO">
    												Equal to
    												<#break>
    												<#case "ONE_OF">
    												One of
    												<#break>
    												<#case "NOT_ONE_OF">
    												Not one of
    												<#break>
  													<#default>
    												...
												</#switch>
											</td>
											<td class="withborder">
												<#if businessRule.valueConfiguration?exists>
													<#if businessRule.valueConfiguration.kind() == "REGEX_BASED">
														${businessRule.valueConfiguration.regEx.expression}
														<#else>${businessRule.valueConfiguration.toString()}
													</#if>
													<#else>N/A
												</#if>
											</td>
											<td class="withborder">${businessRule.message}</td>
										</tr>
									</#list>
								</#list>
							</table>
						</td>
					</tr>
					</#if>
		</table>
		</#list>
	</body>
</html>