package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;

public abstract class AbstractDataControlNodeTranslation extends AbstractControlNodeTranslation {

	protected final Place innerInitialPlace;
	protected final Place innerFinalPlace;
	protected DataStateConditionTranslation precondition;
	protected DataStateConditionTranslation postcondition;

	public AbstractDataControlNodeTranslation(TranslationContext translationContext, AbstractDataControlNode node,
			String name) {
		super(translationContext, name);

		final String prefixString = this.context.getPrefixString();

		innerInitialPlace = addPlace(prefixString + "innerInit");
		innerFinalPlace = addPlace(prefixString + "innerFinal");

		precondition = new DataStateConditionTranslation(this.context, node.getPreCondition(), name + "pre",
				getInitialPlace(), innerInitialPlace);
		postcondition = new DataStateConditionTranslation(this.context, node.getPostCondition(), name + "post",
				innerFinalPlace, getFinalPlace());
	}

	public Place getInnerInitialPlace() {
		return innerInitialPlace;
	}

	public Place getInnerFinalPlace() {
		return innerFinalPlace;
	}
}
