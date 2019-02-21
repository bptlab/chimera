package de.hpi.bpt.chimera.model.petrinet;

import de.hpi.bpt.chimera.model.fragment.bpmn.AbstractDataControlNode;

public abstract class AbstractDataControlNodeTranslation extends AbstractSequentialControlNodeTranslation {

	protected final Place innerInitialPlace;
	protected final Place innerFinalPlace;
	private final DataStatePreConditionTranslation preconditionTranslation;
	private final IoRelationTranslation ioRelationTranslation;

	public AbstractDataControlNodeTranslation(TranslationContext translationContext, AbstractDataControlNode node) {
		super(translationContext, node);

		// Is an io-relation needed?
		if (!node.getPreCondition().getConditionSets().isEmpty()
				|| !node.getPostCondition().getConditionSets().isEmpty()
				|| !TranslationContext.isOptimizeTranslation()) {
			innerInitialPlace = addPlace("innerInit");
			innerFinalPlace = addPlace("innerFinal");
			ioRelationTranslation = new IoRelationTranslation(this.context, node.getPreCondition(),
					node.getPostCondition(), name, initialPlace, innerInitialPlace, innerFinalPlace, finalPlace);
			preconditionTranslation = null;

//			// Is a precondition needed?
//		} else if (!node.getPreCondition().getConditionSets().isEmpty()
//				|| !TranslationContext.isOptimizeTranslation()) {
//			innerInitialPlace = addPlace("innerInit");
//			ioRelationTranslation = new IoRelationTranslation(this.context, node.getPreCondition(), new DataStateCondition(), name + "_pre", getInitialPlace(), innerInitialPlace, innerFinalPlace, innerFinalPlace)
//			preconditionTranslation = new DataStatePreConditionTranslation(this.context, node.getPreCondition(),
//					name + "pre", getInitialPlace(), innerInitialPlace);
//
//			innerFinalPlace = getFinalPlace();
//			ioRelationTranslation = null;
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

	public IoRelationTranslation getIoRelationTranslation() {
		return ioRelationTranslation;
	}

	public DataStatePreConditionTranslation getPreconditionTranslation() {
		return preconditionTranslation;
	}
}
