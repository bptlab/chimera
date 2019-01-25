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

public class IoRelationTranslation extends AbstractDataStateConditionTranslation {

	final DataStateCondition preCondition;
	final DataStateCondition postCondition;
	final Place innerInitialPlace;
	final Place innerFinalPlace;

	final Map<ConditionSet, Place> dataObjectBindPlaceByConditionSet = new HashMap<>();

	public IoRelationTranslation(TranslationContext translationContext, DataStateCondition preCondition,
			DataStateCondition postCondition, String name, Place initialPlace, Place innerInitialPlace,
			Place innerFinalPlace, Place finalPlace) {
		super(translationContext, name, initialPlace, finalPlace);

		this.preCondition = preCondition;
		this.postCondition = postCondition;
		this.innerInitialPlace = innerInitialPlace;
		this.innerFinalPlace = innerFinalPlace;

		List<ConditionSet> preConditionSets = preCondition.getConditionSets();
		List<ConditionSet> postConditionSets = postCondition.getConditionSets();

		// We need at least one pre-ConditionSet, even if it does not contain any
		// conditions
		if (preConditionSets.isEmpty()) {
			preConditionSets = new ArrayList<ConditionSet>();
			preConditionSets.add(new ConditionSet());
		}
		assert (!preConditionSets.isEmpty());

		// We need at least one post-ConditionSet, even if it does not contain any
		// conditions
		if (postConditionSets.isEmpty()) {
			postConditionSets = new ArrayList<ConditionSet>();
			postConditionSets.add(new ConditionSet());
		}
		assert (!postConditionSets.isEmpty());

		int preConditionId = 1;
		int postConditionId = 1;
		for (ConditionSet preConditionSet : preConditionSets) {

			Transition preConditionSetTransition = addTransition("i_" + Integer.toString(preConditionId));

			// Bind data objects (one place per condition set representing all bound data
			// objects for this condition set)
			Place dataObjectBindPlace = addPlace("io_" + Integer.toString(postConditionId) + "_bind");
			dataObjectBindPlaceByConditionSet.put(preConditionSet, dataObjectBindPlace);
			preConditionSetTransition.addOutputPlace(dataObjectBindPlace);

			// Data objects that are only read need to be written back (not consumed)
			Map<DataClass, ObjectLifecycleState> readOnlyDataClassesWithState = new HashMap<>();

			// read and consume
			for (AtomicDataStateCondition atomicDataStateCondition : preConditionSet.getConditions()) {
				Place placeForDataState = getPlaceForDataState(atomicDataStateCondition);
				preConditionSetTransition.addInputPlace(placeForDataState);

				readOnlyDataClassesWithState.put(atomicDataStateCondition.getDataClass(),
						atomicDataStateCondition.getObjectLifecycleState());
			}

			// For each combination of pre-condition sets and post-condition sets...
			for (ConditionSet postConditionSet : postConditionSets) {

				Transition postConditionSetTransition = addTransition("o_" + Integer.toString(postConditionId));

				// Unbind data objects (consume token of pre-condition set)
				postConditionSetTransition.addInputPlace(dataObjectBindPlace);

				// write / produce
				for (AtomicDataStateCondition atomicDataStateCondition : postConditionSet.getConditions()) {
					Place placeForDataState = getPlaceForDataState(atomicDataStateCondition);
					postConditionSetTransition.addOutputPlace(placeForDataState);

					// remove from list of data classes that were only read
					if (readOnlyDataClassesWithState.containsKey(atomicDataStateCondition.getDataClass())) {
						readOnlyDataClassesWithState.remove(atomicDataStateCondition.getDataClass());
					}
				}

				// restore state for read-only data objects
				for (Entry<DataClass, ObjectLifecycleState> readOnlyDataClass : readOnlyDataClassesWithState
						.entrySet()) {
					Place placeForDataState = getPlaceForDataState(readOnlyDataClass.getKey(),
							readOnlyDataClass.getValue());
					postConditionSetTransition.addOutputPlace(placeForDataState);
				}

				// Transition from innerFinal to final
				postConditionSetTransition.addInputPlace(innerFinalPlace);
				postConditionSetTransition.addOutputPlace(finalPlace);
				postConditionId++;
			}

			// Transition from initial to innerInitial
			preConditionSetTransition.addInputPlace(initialPlace);
			preConditionSetTransition.addOutputPlace(innerInitialPlace);
			preConditionId++;
		}
	}
}
