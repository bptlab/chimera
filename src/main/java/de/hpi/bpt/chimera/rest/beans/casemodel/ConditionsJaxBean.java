package de.hpi.bpt.chimera.rest.beans.casemodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.condition.AtomicDataStateCondition;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;

@XmlRootElement
public class ConditionsJaxBean {

	private List<List<DataObjectStateConditionJaxBean>> conditions;

	public ConditionsJaxBean(TerminationCondition terminationCondition) {

		List<List<DataObjectStateConditionJaxBean>> conditionsBean = new ArrayList<>();
		for (ConditionSet conditionSet : terminationCondition.getConditionSets()) {
			List<DataObjectStateConditionJaxBean> componentBean = new ArrayList<>();
			for (AtomicDataStateCondition condition : conditionSet.getConditions()) {
				DataObjectStateConditionJaxBean conditionJaxBean = new DataObjectStateConditionJaxBean(condition);
				componentBean.add(conditionJaxBean);
			}
			conditionsBean.add(componentBean);
		}
		setConditions(conditionsBean);
	}

	public List<List<DataObjectStateConditionJaxBean>> getConditions() {
		return conditions;
	}

	public void setConditions(List<List<DataObjectStateConditionJaxBean>> conditions) {
		this.conditions = conditions;
	}

}
