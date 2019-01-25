package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;

public abstract class AbstractDataControlNodeTranslation extends AbstractSequentialControlNodeTranslation {

	protected final Place innerInitialPlace;
	protected final Place innerFinalPlace;
	protected final DataStatePreConditionTranslation preconditionTranslation;
	protected final IoRelationTranslation ioRelationTranslation;

	public AbstractDataControlNodeTranslation(TranslationContext translationContext, AbstractDataControlNode node,
			String name) {
		super(translationContext, name);

		// Is an io-relation needed?
		if (!node.getPostCondition().getConditionSets().isEmpty() || !TranslationContext.isOptimizeTranslation()) {
			innerInitialPlace = addPlace("innerInit");
			innerFinalPlace = addPlace("innerFinal");
			ioRelationTranslation = new IoRelationTranslation(this.context, node.getPreCondition(),
					node.getPostCondition(), name, initialPlace, innerInitialPlace, innerFinalPlace, finalPlace);
			preconditionTranslation = null;

			// Is a precondition needed?
		} else if (!node.getPreCondition().getConditionSets().isEmpty()
				|| !TranslationContext.isOptimizeTranslation()) {
			innerInitialPlace = addPlace("innerInit");
			preconditionTranslation = new DataStatePreConditionTranslation(this.context, node.getPreCondition(),
					name + "pre", getInitialPlace(), innerInitialPlace);

			innerFinalPlace = getFinalPlace();
			ioRelationTranslation = null;
		} else {
			innerInitialPlace = getInitialPlace();
			preconditionTranslation = null;
			innerFinalPlace = getFinalPlace();
			ioRelationTranslation = null;
		}
	}

	public Place getInnerInitialPlace() {
		return innerInitialPlace;
	}

	public Place getInnerFinalPlace() {
		return innerFinalPlace;
	}
}
