package de.hpi.bpt.chimera.model.petrinet;

public class PlaceReference implements Place {

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
