package de.hpi.bpt.chimera.model.petrinet;

import org.apache.log4j.Logger;

public abstract class AbstractTranslation {

	protected final TranslationContext context;
	protected final String name;

	protected static Logger log = Logger.getLogger(AbstractTranslation.class);

	public AbstractTranslation(TranslationContext translationContext, String name) {
		name = sanitizeName(name);
		this.context = translationContext.withPrefix(name);
		this.name = name;
	}

	// Replace everything non-alphanumeric with a ''
	public static String sanitizeName(String name) {
		return name.replaceAll("[^a-zA-Z0-9]", "");
	}

	protected Place addPlace(String name) {
		Place newPlace = new PlaceReference(this.context, name);
		getPetriNet().addPlace(newPlace);
		return newPlace;
	}

	protected Transition addTransition(String name) {
		Transition newTransition = new TransitionReference(this.context, name);
		getPetriNet().addTransition(newTransition);
		return newTransition;
	}

	protected Transition addTransition(String name, Place inputPlace, Place outputPlace) {
		Transition newTransition = addTransition(name);
		newTransition.addInputPlace(inputPlace);
		newTransition.addOutputPlace(outputPlace);
		return newTransition;
	}

	protected void fusePlaces(Place p1, Place p2, String fusedPlaceName) {
		// Create new fused place
		Place fusedPlace = addPlace(fusedPlaceName);
		// System.out.println("merging " + p1.getPrefixedIdString() + " and " +
		// p2.getPrefixedIdString() + " into " + fusedPlaceName);
		((PlaceReference) p1).setImpl(((PlaceReference) fusedPlace).getImpl());
		((PlaceReference) p2).setImpl(((PlaceReference) fusedPlace).getImpl());

		// Remove old lingering places from petri net
		getPetriNet().getPlaces().remove(p1);
		getPetriNet().getPlaces().remove(p2);

		assert (getPetriNet().getPlaces().contains(fusedPlace));
	}

	public String getName() {
		return name;
	}

	public String getPrefixString() {
		return context.getPrefixString();
	}

	public PetriNet getPetriNet() {
		return context.getCaseModelTranslation().getPetriNet();
	}
}
