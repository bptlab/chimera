package de.uni_potsdam.hpi.bpt.bp2014.jcore.dtables;

import java.util.List;
import java.util.Set;

public class Clause {
	String name;
	Expression inputExpression;
	Set<Expression> inputEntries;
	List<Expression> outputEntries;
	ItemDefinition outputDifinition;
}
