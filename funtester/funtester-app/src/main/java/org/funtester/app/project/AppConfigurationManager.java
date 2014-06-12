package org.funtester.app.project;

import java.util.Collection;
import java.util.Locale;

import org.funtester.app.repository.AppConfigurationRepository;
import org.funtester.app.validation.AppConfigurationValidator;

/**
 * Application configuration manager
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class AppConfigurationManager {
	
	private final Collection< Locale > locales;
	private final Collection< String > lookAndFeels;
	private final AppConfigurationValidator validator;
	private final AppConfigurationRepository repository;

	public AppConfigurationManager(
			final Collection< Locale > locales,
			final Collection< String > lookAndFeels,
			final AppConfigurationValidator validator,
			final AppConfigurationRepository repository
			) {
		if ( null == locales ) throw new IllegalArgumentException( "locales" );
		if ( null == lookAndFeels ) throw new IllegalArgumentException( "lookAndFeels" );
		if ( null == validator ) throw new IllegalArgumentException( "validator" );
		if ( null == repository ) throw new IllegalArgumentException( "repository" );
		this.locales = locales;
		this.lookAndFeels = lookAndFeels;
		this.validator = validator;
		this.repository = repository;
	}

	public Collection< Locale > getLocales() {
		return locales;
	}

	public Collection< String > getLookAndFeels() {
		return lookAndFeels;
	}

	public AppConfigurationRepository getRepository() {
		return repository;
	}

	public void save(AppConfiguration appConfiguration) throws Exception {
		validator.validate( appConfiguration );
		repository.save( appConfiguration );
	}
	
}
