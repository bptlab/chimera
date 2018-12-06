package de.hpi.bpt.chimera.model.petrinet;

/**
 * A modifiable reference to a place in a Petri net.
 */
public class PlaceReference implements Place {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((impl == null) ? 0 : impl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlaceReference other = (PlaceReference) obj;
		if (impl == null) {
			if (other.impl != null)
				return false;
		} else if (!impl.equals(other.impl))
			return false;
		return true;
	}

	private PlaceImpl impl;

	public PlaceReference(PlaceImpl impl) {
		this.impl = impl;
	}

	public PlaceReference(TranslationContext context, String name) {
		this(new PlaceImpl(context, name));
	}

	public void setImpl(PlaceImpl newImpl) {
		this.impl = newImpl;
	}

	public PlaceImpl getImpl() {
		return impl;
	}

	@Override
	public int getNumTokens() {
		return impl.getNumTokens();
	}

	@Override
	public void setNumTokens(int numTokens) {
		impl.setNumTokens(numTokens);
	}

	@Override
	public boolean isSignificant() {
		return impl.isSignificant();
	}

	@Override
	public void setSignificant(boolean isSignificant) {
		impl.setSignificant(isSignificant);
	}

	@Override
	public String getName() {
		return impl.getName();
	}

	@Override
	public TranslationContext getContext() {
		return impl.getContext();
	}
}
