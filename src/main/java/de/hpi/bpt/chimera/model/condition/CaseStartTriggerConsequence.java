package de.hpi.bpt.chimera.model.condition;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import de.hpi.bpt.chimera.model.JsonPath;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

@Entity
public class CaseStartTriggerConsequence {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int dbId;
	@OneToOne(cascade = CascadeType.ALL)
	private AtomicDataStateCondition dataObjectState;
	/*
	 * @ManyToMany(targetEntity = String.class, fetch = FetchType.LAZY)
	@JoinTable(
		name="AuthorBookGroup",
		joinColumns={@JoinColumn(name="fk_author", referencedColumnName="id")},
		inverseJoinColumns={@JoinColumn(name="fk_group", referencedColumnName="id")})
	@MapKey(name = "dbId")
	*/
	/*
	@ElementCollection
	@CollectionTable(name="MAP_TABLE", joinColumns={
	        @JoinColumn(name="entityid",referencedColumnName="id"),
	        @JoinColumn(name="entitysource",referencedColumnName="source")
	     })
	@MapKeyJoinColumn(name = "secondaryid")
	@Column(name = "VALUE")
	*/
	@OneToMany(targetEntity = JsonPath.class, fetch = FetchType.LAZY)
	@JoinTable(name = "CASESTARTTRIGGERCONSEQUENCE_JSONPATHMAPPING", joinColumns = { @JoinColumn(name = "DATAATTRIBUTE_ID", referencedColumnName = "DBID") }, inverseJoinColumns = {
			@JoinColumn(name = "JSONPATH_ID", referencedColumnName = "DBID") })
	@MapKey(name = "dbId")
	private Map<DataAttribute, JsonPath> dataAttributeToJsonPath;


	// no-argument constructor is needed by EclipseLink to persist and restore
	// objects.
	CaseStartTriggerConsequence() {
	}

	public CaseStartTriggerConsequence(AtomicDataStateCondition dataObjectStateCondition, Map<DataAttribute, JsonPath> dataAttributeToJsonPath) {
		this.dataObjectState = dataObjectStateCondition;
		this.dataAttributeToJsonPath = dataAttributeToJsonPath;
	}

	public AtomicDataStateCondition getDataObjectState() {
		return dataObjectState;
	}

	public void setDataObjectState(AtomicDataStateCondition dataObjectState) {
		this.dataObjectState = dataObjectState;
	}

	public Map<DataAttribute, JsonPath> getDataAttributeToJsonPath() {
		return dataAttributeToJsonPath;
	}

	public void setDataAttributeToJsonPath(Map<DataAttribute, JsonPath> dataAttributeToJsonPath) {
		this.dataAttributeToJsonPath = dataAttributeToJsonPath;
	}


}
