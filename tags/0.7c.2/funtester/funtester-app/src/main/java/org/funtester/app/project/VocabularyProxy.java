package org.funtester.app.project;

import org.funtester.core.vocabulary.Vocabulary;

/**
 * Vocabulary proxy
 * 
 * @author Thiago Delgado Pinto
 *
 */
public class VocabularyProxy {
	
	private String profileFile = "";
	private Vocabulary vocabulary;
	
	public String getProfileFile() {
		return profileFile;
	}
	
	public void setProfileFile(String profileFile) {
		this.profileFile = profileFile;
	}
	
	public Vocabulary getVocabulary() {
		return vocabulary;
	}
	
	public void setVocabulary(Vocabulary vocabulary) {
		this.vocabulary = vocabulary;
	}

}
