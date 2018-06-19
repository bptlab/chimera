/**
 * 
 */
package de.hpi.bpt.chimera.model.datamodel;

import java.util.ArrayList;
import java.util.List;
import de.hpi.bpt.chimera.model.datamodel.DataClass;

/**
 * @author hewelt
 *
 */
public final class Olc {
	
	private final List<OlcState> states = new ArrayList<>();
	private List<Transition> transitions  = new ArrayList<>();
	private DataClass dataClass;
	
	public Olc(DataClass dc) {
		dataClass = dc;
	}
	
	public Boolean isValidState(OlcState s) {
		return states.contains(s);
	}
	
	public Boolean isValidTransition(OlcState s, OlcState t) {
		return transitions.contains(new Transition(s, t));
	}
	
	public void addState(OlcState state) {
		states.add(state);
	}
	
	public void addTransition(OlcState source, OlcState target) {
		Transition t = new Transition(source, target);
		transitions.add(t);
	}
	
	private class Transition {
		private final OlcState source;
		private final OlcState target;
		
		private Transition(OlcState source, OlcState target) {
			this.source = source;
			this.target = target;
		}
		/**
		 * Transitions are equal if their source and target are equal.
		 */
		public boolean equals(Object o) {
			if (o instanceof Transition) {
 				return ((Transition) o).source.equals(source) &&
 						((Transition) o).target.equals(target);
			}
			return false;
		}
	}
	public static void main(String[] args) {
		DataClass dc = new DataClass();
		Olc olc = new Olc(dc);
		OlcState s1 = new OlcState("1", olc);
		OlcState s2 = new OlcState("2", olc);
		OlcState s1copy = new OlcState("1", olc);
		olc.addState(s1);
		olc.addState(s2);
		olc.addTransition(s1, s2);
		System.out.println(olc.isValidState(s1copy));
	}

	public Object getDataClass() {
		// TODO Auto-generated method stub
		return null;
	}
}
