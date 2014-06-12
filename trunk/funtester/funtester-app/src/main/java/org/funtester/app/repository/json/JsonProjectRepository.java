package org.funtester.app.repository.json;

import java.io.File;

import org.funtester.app.project.Project;
import org.funtester.app.project.VocabularyProxy;
import org.funtester.app.repository.ProjectRepository;
import org.funtester.app.repository.VocabularyProxyRepository;
import org.funtester.app.util.JsonMapper;
import org.funtester.common.generation.TestGenerationConfigurationRepository;
import org.funtester.core.profile.ElementType;
import org.funtester.core.profile.StepKind;
import org.funtester.core.software.BusinessRule;
import org.funtester.core.software.Element;
import org.funtester.core.software.ElementBasedVC;
import org.funtester.core.software.Flow;
import org.funtester.core.software.ParameterConfig;
import org.funtester.core.software.QueryBasedVC;
import org.funtester.core.software.Software;
import org.funtester.core.software.Step;
import org.funtester.core.software.UseCase;
import org.funtester.core.software.UseCaseCallStep;
import org.funtester.core.software.ValueConfiguration;
import org.funtester.core.software.ValueConfigurationKind;
import org.funtester.core.vocabulary.ActionNickname;
import org.funtester.core.vocabulary.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * JSON repository for a {@link Project}.
 * 
 * @author Thiago Delgado Pinto
 *
 */
class JsonProjectRepository implements ProjectRepository {
	
	private static final Logger logger = LoggerFactory.getLogger( JsonProjectRepository.class );

	private String fileName;
	
	public JsonProjectRepository(final String fileName) {
		this.fileName = fileName;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Project first() throws Exception {
		
		Project project = JsonMapper.readObject( fileName, Project.class );
		
		//
		// Read the vocabulary
		//
		
		VocabularyProxyRepository vpr = new JsonVocabularyProxyRepository( project.getVocabularyFile() );
		final VocabularyProxy vocabularyProxy = vpr.first();
		Vocabulary vocabulary = vocabularyProxy.getVocabulary();
		
		//
		// Read the last configuration if it exists
		//
		
		if ( project.getLastConfigurationFile() != null &&
				! project.getLastConfigurationFile().isEmpty() &&
				( new File( project.getLastConfigurationFile() ).exists() ) ) {
			
			TestGenerationConfigurationRepository tgr =
					new JsonTestGenerationConfigurationRepository(
							project.getLastConfigurationFile() );
			try {
				project.setLastConfiguration( tgr.first() );
			} catch (Exception e) {
				logger.error( e.getLocalizedMessage() );
				// Can continue in case of error
			}
		}
		
		//
		// Read the software using another mapper
		//
		
		final ObjectMapper mapper = new ObjectMapper();
		JsonMapper.configure( mapper );
		
		// Register the ActionNicknameIdDeserializer to recognize an Action id
		SimpleModule actionIdModule = new SimpleModule( "ActionIdDeserializerModule", new Version( 1, 0, 0, null ) );
		actionIdModule.addDeserializer( ActionNickname.class, new ActionNicknameIdDeserializer( vocabulary ) );
		mapper.registerModule( actionIdModule );
		
		// Register the ElementTypeIdDeserializer to recognize an ElementType id
		SimpleModule elementTypeIdModule = new SimpleModule( "ElementTypeIdDeserializer", new Version( 1, 0, 0, null ) );
		elementTypeIdModule.addDeserializer( ElementType.class, new ElementTypeIdDeserializer( vocabulary.getProfile() ) );
		mapper.registerModule( elementTypeIdModule );
		
		
		Software software = mapper.readValue( new File( project.getSoftwareFile() ), Software.class );
		software.setVocabulary( vocabulary );
		
		//
		// Workaround because Jackson 2.3.1 cannot deserialize objects with
		// forward references
		//
		deserializeReferences( software );
		
		project.setSoftware( software );
		
		return project;
		
	}

	private void deserializeReferences(Software software) {
		for ( UseCase uc : software.getUseCases() ) {
			
			// Deserialize elements
			for ( Element e : uc.getElements() ) {
				if ( ! e.isEditable() ) { continue; }
				for ( BusinessRule br : e.getBusinessRules() ) {
					deserializeValueConfiguration( uc, br.getValueConfiguration() );
				}
			}
			
			// Deserialize use case call steps
			for ( Flow f : uc.getFlows() ) {
				for ( Step s : f.getSteps() ) {
					if ( s.kind() == StepKind.USE_CASE_CALL ) {
						UseCaseCallStep uccs = (UseCaseCallStep) s;
						
						long id = uccs.getReferencedUseCaseId();
						if ( id <= 0 ) { continue; }
						
						UseCase refUC = software.useCaseWithId( id );
						uccs.setUseCase( refUC );
					}
				}
			}
		}
	}

	private void deserializeValueConfiguration(UseCase uc, ValueConfiguration vc) {
		if ( null == vc ) { return; }
		
		if ( vc.kind().equals( ValueConfigurationKind.ELEMENT_BASED ) ) {
			
			ElementBasedVC evc = (ElementBasedVC) vc;
			long id = evc.getReferencedElementId();
			Element element = uc.elementWithId( id );
			evc.setReferencedElement( element );
			
		} else if ( vc.kind().equals( ValueConfigurationKind.QUERY_BASED ) ) {
			
			QueryBasedVC qvc = (QueryBasedVC) vc;
			for ( ParameterConfig pc : qvc.getParameters() ) {
				deserializeValueConfiguration( uc, pc.getValueConfiguration() );
			}
			
		}
	}
	

	@Override
	public void save(Project obj) throws Exception {
		
		// Project
		JsonMapper.writeObject( fileName, obj );

		// Software
		JsonMapper.writeObject( obj.getSoftwareFile(), obj.getSoftware() );
	}
	
}
