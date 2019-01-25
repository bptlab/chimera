package de.hpi.bpt.chimera.model.petrinet;

public class EntityWithId {

	private final int id;
	private static int idCounter = 0;

	public EntityWithId() {
		this.id = idCounter++;
	}

	public int getId() {
		return id;
	}

}
