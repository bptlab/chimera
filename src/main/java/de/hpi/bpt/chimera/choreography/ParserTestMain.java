package de.hpi.bpt.chimera.choreography;

import java.io.InputStream;

public class ParserTestMain {

	public static void main(String[] args) {
		ChoreographyParser choreographyParser = new ChoreographyParser();
		
		InputStream choreoStream = ParserTestMain.class.getClassLoader().getResourceAsStream("TestChor.bpmn");
		ChimeraChoreography chimeraChoreography = new ChimeraChoreography();
		chimeraChoreography = choreographyParser.parse(choreoStream);
		System.out.println(chimeraChoreography.getMessageflows().get(0).getMessageFlowId());
	}

}
