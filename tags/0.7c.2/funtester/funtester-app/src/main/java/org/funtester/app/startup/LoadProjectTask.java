package org.funtester.app.startup;

import java.io.File;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppState;
import org.funtester.app.project.Project;
import org.funtester.app.repository.ProjectRepository;
import org.funtester.app.repository.json.JsonProjectRepositoryWithRelativePaths;
import org.funtester.common.util.ArgUtil;
import org.funtester.core.util.RepositoryException;

/**
 * Task for loading the project.
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class LoadProjectTask implements Task {

	@Override
	public String description() {
		return Messages.alt( "_PROJECT_LOADING", "Loading project..." );
	}

	@Override
	public boolean canContinueInCaseOfError() {
		return true;
	}
	
	@Override
	public void perform(final String args[], AppState appState)
			throws Exception {
		
		final String PROJECT_ARGS[] = { "p", "project" };
		final int ARG_INDEX = ArgUtil.argumentIndex( args, PROJECT_ARGS );
		
		final int VALUE_INDEX = ARG_INDEX + 1;
		if ( ARG_INDEX < 0 || VALUE_INDEX >= args.length ) { return; }
		final String projectFilePath = args[ VALUE_INDEX ];
				
		File file = new File( projectFilePath );
		if ( ! file.isFile() ) {
			throw new RepositoryException( String.format(
					Messages.alt( "_NOT_A_FILE", "\"%s\" is not a file." ),
					projectFilePath ) );
		}
		
		appState.setProjectFileName( projectFilePath );
		
		if ( ! file.canRead() ) {
			throw new RepositoryException( String.format(
					Messages.alt( "_CANNOT_READ_FILE", "File \"%s\" cannot be read." ),
					projectFilePath ) );
		}

		ProjectRepository repository = new JsonProjectRepositoryWithRelativePaths( projectFilePath );
		Project project = repository.first();
		appState.setProject( project );
	}

}
