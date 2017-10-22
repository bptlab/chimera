package de.hpi.bpt.chimera.jcore.rest.beans.casemodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.model.condition.DataStateCondition;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.condition.ConditionSet;

@XmlRootElement
public class ConditionsJaxBean {

	private DataObjectStateConditionJaxBean[][] conditions;

	public ConditionsJaxBean(TerminationCondition terminationCondition) {

		List<DataObjectStateConditionJaxBean[]> terminationConditionJaxBean = new ArrayList<>(terminationCondition.getConditionSets().size());
		for (ConditionSet component : terminationCondition.getConditionSets()) {

			List<DataObjectStateConditionJaxBean> componentJaxBean = new ArrayList<>(component.getConditions().size());
			for (DataStateCondition condition : component.getConditions()) {
				DataObjectStateConditionJaxBean conditionJaxBean = new DataObjectStateConditionJaxBean(condition);
				componentJaxBean.add(conditionJaxBean);
			}
			DataObjectStateConditionJaxBean[] componentArray = componentJaxBean.toArray(new DataObjectStateConditionJaxBean[component.getConditions().size()]);
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
