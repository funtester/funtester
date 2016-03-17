<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="es" lang="es">

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Use Case Diagram</title>

      <link type="text/css" rel="stylesheet" href="css/UDStyle.css" media="screen" />

    <script type="text/javascript" src="UDCore.js"></script>
    <script type="text/javascript" src="UDModules.js"></script>


    <script type="text/javascript">
    window.onload = function(){

			<#assign a = 100>
			<#assign b = 100>
			<#list software.actors as actor>
				<#if a < 900><#assign a = a + 100><#else><#assign a = 1200></#if>
			</#list>
			<#list software.useCases as useCase>
					<#if a < 900><#assign a = a + 100><#else><#assign a = 1200></#if>
			</#list>

            var usecaseDiagram = new UMLUseCaseDiagram({id: 'useCaseDiagram', width:${a}, height:${a} });


            // Creating UML Actors
            <#assign yActor = 50>
            <#list software.actors as actor>
            	var actor${actor.id} = new UMLActor({x:60,y:${yActor}});
            	usecaseDiagram.addElement(actor${actor.id});
            	actor${actor.id}.setName('${actor.name}');
            	<#assign yActor = yActor + 120> 
            </#list>
            
            // Creating the UML use cases
            <#assign yUseCase = 90>
            <#list software.useCases as useCase>
            	var useCase${useCase.id} = new UMLUseCase({x:200,y:${yUseCase}});
            	usecaseDiagram.addElement(useCase${useCase.id});
            	<#assign yUseCase = yUseCase + 60>
            </#list>
        
            // Creating the UML communications
            <#assign com = 1>
            <#list software.useCases as useCase>
            	<#list useCase.actors as actor>
            		var communication${com} = new UMLCommunication({ b:actor${actor.id}, a:useCase${useCase.id} });
            		usecaseDiagram.addElement(communication${com});
            		<#assign com = com + 1>
            	</#list>
            </#list> 

            // Adding the name of use cases
            <#list software.useCases as useCase>
            	useCase${useCase.id}.setName('${useCase.name}');
            	useCase${useCase.id}.notifyChange();
            </#list>
          
            // Drawing the diagram 
            usecaseDiagram.draw();

            // This diagram is editable
            usecaseDiagram.interaction(true);
        }
    </script>
    </head>

    <body>
        The following diagram is editable.<br/><br/>
        <div id="useCaseDiagram">

        </div>
    </body>
</html>
