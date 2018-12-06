package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;

public abstract class AbstractDataControlNodeTranslation extends AbstractSequentialControlNodeTranslation {

	protected final Place innerInitialPlace;
	protected final Place innerFinalPlace;
	protected final DataStatePreConditionTranslation precondition;
	protected final DataStatePostConditionTranslation postcondition;

	public AbstractDataControlNodeTranslation(TranslationContext translationContext, AbstractDataControlNode node,
			String name) {
		super(translationContext, name);

		final String prefixString = this.context.getPrefixString();

		// Only add precondition overhead if necessary
		if (!node.getPreCondition().getConditionSets().isEmpty() || !TranslationContext.isOptimizeTranslation()) {
			innerInitialPlace = addPlace(prefixString + "innerInit");
			precondition = new DataStatePreConditionTranslation(this.context, node.getPreCondition(),
					node.getPostCondition(), name + "pre", getInitialPlace(), innerInitialPlace);
		} else {
			innerInitialPlace = getInitialPlace();
			precondition = null;
		}

		// Only add postcondition overhead if necessary
		if (!node.getPostCondition().getConditionSets().isEmpty() || !TranslationContext.isOptimizeTranslation()) {
			innerFinalPlace = addPlace(prefixString + "innerFinal");
			postcondition = new DataStatePostConditionTranslation(this.context, node.getPreCondition(),
					node.getPostCondition(), name + "post", innerFinalPlace, getFinalPlace());
		} else {
			innerFinalPlace = getFinalPlace();
			postcondition = null;
		}
	}

	public Place getInnerInitialPlace() {
		return innerInitialPlace;
	}

	public Place getInnerFinalPlace() {
		return innerFinalPlace;
	}
}
