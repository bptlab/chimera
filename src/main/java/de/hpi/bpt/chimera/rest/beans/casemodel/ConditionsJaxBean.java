package de.hpi.bpt.chimera.rest.beans.casemodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;

@XmlRootElement
public class ConditionsJaxBean {

	private DataObjectStateConditionJaxBean[][] conditions;

	public ConditionsJaxBean(TerminationCondition terminationCondition) {

		List<DataObjectStateConditionJaxBean[]> terminationConditionJaxBean = new ArrayList<>(terminationCondition.getConditionSets().size());
		for (ConditionSet conditionSet : terminationCondition.getConditionSets()) {

			List<DataObjectStateConditionJaxBean> componentJaxBean = new ArrayList<>(conditionSet.getConditions().size());
			for (AtomicDataStateCondition condition : conditionSet.getConditions()) {
				DataObjectStateConditionJaxBean conditionJaxBean = new DataObjectStateConditionJaxBean(condition);
				componentJaxBean.add(conditionJaxBean);
			}
			DataObjectStateConditionJaxBean[] componentArray = componentJaxBean.toArray(new DataObjectStateConditionJaxBean[conditionSet.getConditions().size()]);
			terminationConditionJaxBean.add(componentArray);
		}
		DataObjectStateConditionJaxBean[][] terminationConditionArray = terminationConditionJaxBean.toArray(new DataObjectStateConditionJaxBean[terminationCondition.getConditionSets().size()][]);
		setConditions(terminationConditionArray);
	}

	public DataObjectStateConditionJaxBean[][] getConditions() {
		return conditions;
	}

	public void setConditions(DataObjectStateConditionJaxBean[][] conditions) {
		this.conditions = conditions;
	}

}
