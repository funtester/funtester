package org.funtester.app.project.mutation;

import java.util.LinkedHashSet;
import java.util.Set;

import org.funtester.app.project.Project;
import org.funtester.app.repository.ProjectRepository;
import org.funtester.app.repository.json.JsonProjectRepositoryWithRelativePaths;
import org.funtester.common.util.StringUtil;
import org.funtester.core.software.UseCase;

public class SpecMutantGenerator {

	private final Project project;
	private final String projectFileName;
	private final Set< SpecMutator > mutators;

	public SpecMutantGenerator(
			final Project project,
			final String projectFileName
			) {
		this.project = project;
		this.projectFileName = projectFileName;
		this.mutators = new LinkedHashSet< SpecMutator >();
	}

	public boolean addMutator(SpecMutator sm) {
		return this.mutators.add( sm );
	}

	public void generateToDirectory(final String path) throws Exception {
		for ( SpecMutator sm : this.mutators ) {
			// Clone the project
			Project p = project.newCopy();
			// Mutate and save it
			for ( UseCase uc : p.getSoftware().getUseCases() ) {
				if ( uc.getIgnoreToGenerateTests() ) {
					continue;
				}
				sm.mutate( uc );
				this.saveProjectMutation( p, path, sm.mnemonic(), uc.getName() );
			}
		}
	}

	private void saveProjectMutation(
			Project p, String path, String mnemonic, String useCaseName
		) throws Exception {
		String fileName = path + "/" + generateMutationFileName( projectFileName, mnemonic, useCaseName );
		ProjectRepository repository = new JsonProjectRepositoryWithRelativePaths( fileName );
		repository.save( p );
	}

	private String generateMutationFileName(
			String originalFileName, String mutationMnemonic, String useCaseName
		) {

		final int lastDot = originalFileName.lastIndexOf( "." );

		String fileName =
			originalFileName.substring( 0, lastDot )
		 	+ "__" + mutationMnemonic
			+ "__" + StringUtil.buildNameForCode( useCaseName )
			+ originalFileName.substring( lastDot )
			;

		return fileName;
	}

}
