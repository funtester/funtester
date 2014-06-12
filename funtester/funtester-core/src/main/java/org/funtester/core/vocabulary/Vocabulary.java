package org.funtester.core.vocabulary;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;

import org.funtester.common.util.Copier;
import org.funtester.common.util.CopierUtil;
import org.funtester.common.util.EqUtil;
import org.funtester.core.profile.Profile;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;

/**
 * Translates a profile or adapts it to the user's needs.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@JsonIdentityInfo(generator=PropertyGenerator.class, property="id", scope=Vocabulary.class)
public class Vocabulary implements Copier< Vocabulary > {

	private long id = 0;
	private String name = "";
	private Locale locale;
	@JsonIgnore
	private Profile profile;
	private Collection< ActionNickname > nicknames = new LinkedHashSet< ActionNickname >();
	
	public Vocabulary() {
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public Collection< ActionNickname > getNicknames() {
		return nicknames;
	}

	public void setNicknames(Collection< ActionNickname > nicknames) {
		this.nicknames = nicknames;
	}
	
	public boolean addNickname(ActionNickname nickname) {
		return nicknames.add( nickname );
	}
	
	public ActionNickname nicknameWithId(final long anId) {
		for ( ActionNickname an : nicknames ) {
			if ( an.getId() == anId ) return an;
		}
		return null;
	}

	@Override
	public Vocabulary copy(final Vocabulary that) {
		this.id = that.id;
		this.name = that.name;
		this.locale = that.locale; // Reference
		this.profile = that.profile;  // Reference
		CopierUtil.copyCollection( that.nicknames, this.nicknames );
		return this;
	}
	
	@Override
	public Vocabulary newCopy() {
		return ( new Vocabulary() ).copy( this );
	}	
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode( new Object[] {
			id, name, locale, profile, nicknames
		} );
	}
	
	@Override
	public boolean equals(Object obj) {
		if ( ! ( obj instanceof Vocabulary ) ) return false;
		final Vocabulary that = (Vocabulary) obj;
		return
			EqUtil.equalsIgnoreCase( this.name, that.name )
			|| ( EqUtil.equals( locale, that.locale )
				&& EqUtil.equals( profile, that.profile )
				&& EqUtil.equals( nicknames, that.nicknames ) )
			;
	}
}
