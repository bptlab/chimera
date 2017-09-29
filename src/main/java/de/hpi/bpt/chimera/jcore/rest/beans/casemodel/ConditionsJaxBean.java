package de.hpi.bpt.chimera.jcore.rest.beans.casemodel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.hpi.bpt.chimera.jcore.rest.TransportationBeans.DataAttributeJaxBean;
import de.hpi.bpt.chimera.model.condition.DataObjectStateCondition;
import de.hpi.bpt.chimera.model.condition.TerminationCondition;
import de.hpi.bpt.chimera.model.condition.TerminationConditionComponent;

@XmlRootElement
public class ConditionsJaxBean {

	private DataObjectStateConditionJaxBean[][] conditions;

	public ConditionsJaxBean(TerminationCondition terminationCondition) {

		List<DataObjectStateConditionJaxBean[]> terminationConditionJaxBean = new ArrayList<>(terminationCondition.getConditions().size());
		for (TerminationConditionComponent component : terminationCondition.getConditions()) {

			List<DataObjectStateConditionJaxBean> componentJaxBean = new ArrayList<>(component.getConditions().size());
			for (DataObjectStateCondition condition : component.getConditions()) {
				DataObjectStateConditionJaxBean conditionJaxBean = new DataObjectStateConditionJaxBean(condition);
				componentJaxBean.add(conditionJaxBean);
			}
			DataObjectStateConditionJaxBean[] componentArray = componentJaxBean.toArray(new DataObjectStateConditionJaxBean[component.getConditions().size()]);
			terminationConditionJaxBean.add(componentArray);
		}
		DataObjectStateConditionJaxBean[][] terminationConditionArray = terminationConditionJaxBean.toArray(new DataObjectStateConditionJaxBean[terminationCondition.getConditions().size()][]);
		setConditions(terminationConditionArray);
	}

	public DataObjectStateConditionJaxBean[][] getConditions() {
		return conditions;
	}

	public void setConditions(DataObjectStateConditionJaxBean[][] conditions) {
		this.conditions = conditions;
	}

}
