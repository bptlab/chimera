package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.datamodel.DataClass;

public class DataClassTranslation extends AbstractTranslation {

	public DataClassTranslation(TranslationContext translationContext, DataClass dataClass) {
		super(translationContext, dataClass.getName());

		final String prefixString = this.context.getPrefixString();
	}
}
