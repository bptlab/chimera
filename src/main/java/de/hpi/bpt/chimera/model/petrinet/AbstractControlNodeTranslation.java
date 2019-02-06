package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractControlNode;

public abstract class AbstractControlNodeTranslation extends AbstractTranslation {

	public AbstractControlNodeTranslation(TranslationContext translationContext, AbstractControlNode node) {
		super(translationContext, getNodeName(node));
	}

	protected static String getNodeName(AbstractControlNode node) {
		return node.getName().isEmpty() ? node.getId() : node.getName();
	}

	public abstract Place getInitialPlace();

	public abstract Place getFinalPlace();
}
