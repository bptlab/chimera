package de.hpi.bpt.chimera.model.petrinet;

import java.util.ArrayList;
import java.util.List;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;

public class DataStatePostConditionTranslation extends AbstractDataStateConditionTranslation {

	public DataStatePostConditionTranslation(TranslationContext translationContext, DataStateCondition preCondition,
			DataStateCondition postCondition, String name, Place initialPlace, Place finalPlace) {
		super(translationContext, name, initialPlace, finalPlace);

		final String prefixString = this.context.getPrefixString();

		List<ConditionSet> preConditionSets = preCondition.getConditionSets();
		List<ConditionSet> postConditionSets = postCondition.getConditionSets();

		// We need at least one pre-ConditionSet, even if it does not contain any
		// conditions
		if (preConditionSets.isEmpty()) {
			preConditionSets = new ArrayList<ConditionSet>();
			preConditionSets.add(new ConditionSet());
		}
		// We need at least one post-ConditionSet, even if it does not contain any
		// conditions
		if (postConditionSets.isEmpty()) {
			postConditionSets = new ArrayList<ConditionSet>();
			postConditionSets.add(new ConditionSet());
		}

		assert (!preConditionSets.isEmpty());
		assert (!postConditionSets.isEmpty());

		int conditionId = 1;
		for (ConditionSet preConditionSet : preConditionSets) {
			for (ConditionSet postConditionSet : postConditionSets) {
				Transition conditionSetTransition = addTransition(prefixString + "cs_" + Integer.toString(conditionId));

				// read / consume
				for (AtomicDataStateCondition atomicDataStateCondition : preConditionSet.getConditions()) {
					Place placeForDataState = getPlaceForDataState(atomicDataStateCondition);
					conditionSetTransition.addInputPlace(placeForDataState);
				}
				// write
				for (AtomicDataStateCondition atomicDataStateCondition : postConditionSet.getConditions()) {
					Place placeForDataState = getPlaceForDataState(atomicDataStateCondition);
					conditionSetTransition.addOutputPlace(placeForDataState);
				}

				conditionSetTransition.addInputPlace(initialPlace);
				conditionSetTransition.addOutputPlace(finalPlace);
				conditionId++;
			}
		}
	}
}
