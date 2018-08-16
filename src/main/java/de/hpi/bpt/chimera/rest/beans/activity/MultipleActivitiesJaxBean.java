package de.hpi.bpt.chimera.rest.beans.activity;

import java.util.List;
import java.util.stream.Collectors;

import de.hpi.bpt.chimera.execution.controlnodes.activity.AbstractActivityInstance;

public class MultipleActivitiesJaxBean {
	List<ActivityJaxBean> activities;

	public MultipleActivitiesJaxBean(List<AbstractActivityInstance> rawActivities) {
		this.activities = rawActivities.stream()
							.map(ActivityJaxBean::new)
							.collect(Collectors.toList());
	}

	public List<ActivityJaxBean> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityJaxBean> activities) {
		this.activities = activities;
	}
}
