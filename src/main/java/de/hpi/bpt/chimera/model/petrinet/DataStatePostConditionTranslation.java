package de.hpi.bpt.chimera.model.petrinet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;
import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;

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

				// Data objects that are only read need to be written back (not consumed)
				Map<DataClass, ObjectLifecycleState> readOnlyDataClassesWithState = new HashMap<>();

				// read / consume
				for (AtomicDataStateCondition atomicDataStateCondition : preConditionSet.getConditions()) {
					Place placeForDataState = getPlaceForDataState(atomicDataStateCondition);
					conditionSetTransition.addInputPlace(placeForDataState);

					readOnlyDataClassesWithState.put(atomicDataStateCondition.getDataClass(),
							atomicDataStateCondition.getObjectLifecycleState());
				}

				// write
				for (AtomicDataStateCondition atomicDataStateCondition : postConditionSet.getConditions()) {
					Place placeForDataState = getPlaceForDataState(atomicDataStateCondition);
					conditionSetTransition.addOutputPlace(placeForDataState);

					// remove from list of data classes that were only read
					if (readOnlyDataClassesWithState.containsKey(atomicDataStateCondition.getDataClass())) {
						readOnlyDataClassesWithState.remove(atomicDataStateCondition.getDataClass());
					}

					// Produce semaphore token
					Place placeForSemaphore = getPlaceForDataSemaphore(atomicDataStateCondition.getDataClass());
					conditionSetTransition.addOutputPlace(placeForSemaphore);
				}

				// restore state for read-only data objects
				for (Entry<DataClass, ObjectLifecycleState> readOnlyDataClass : readOnlyDataClassesWithState
						.entrySet()) {
					Place placeForDataState = getPlaceForDataState(readOnlyDataClass.getKey(),
							readOnlyDataClass.getValue());
					conditionSetTransition.addOutputPlace(placeForDataState);
				}

				conditionSetTransition.addInputPlace(initialPlace);
				conditionSetTransition.addOutputPlace(finalPlace);
				conditionId++;
			}
		}
	}
}
