package de.hpi.bpt.chimera.model.petrinet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TranslationContext {
	private final CaseModelTranslation caseModelTranslation;
	private final List<String> prefixes;

	public TranslationContext(CaseModelTranslation caseModelTranslation) {
		this(caseModelTranslation, Collections.emptyList());
	}

	public TranslationContext(CaseModelTranslation caseModelTranslation, String prefix) {
		this(caseModelTranslation, Collections.singletonList(prefix));
	}

	public TranslationContext(CaseModelTranslation caseModelTranslation, List<String> prefix) {
		this.caseModelTranslation = caseModelTranslation;
		this.prefixes = prefix;
	}

	public TranslationContext withPrefix(String prefix) {
		List<String> newPrefixes = new ArrayList<>();
		newPrefixes.addAll(prefixes);
		newPrefixes.add(prefix);
		return new TranslationContext(getCaseModelTranslation(), newPrefixes);
	}

	public CaseModelTranslation getCaseModelTranslation() {
		return caseModelTranslation;
	}

	public String getPrefixString() {
		return String.join("_", prefixes) + "_";
	}
}
