package de.hpi.bpt.chimera.execution.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.execution.CaseExecutioner;
import de.hpi.bpt.chimera.model.datamodel.DataAttribute;

@Entity
public class DataAttributeInstance {

	private static final Logger log = Logger.getLogger(DataAttributeInstance.class);

	@Id
	private String id;
	@OneToOne(cascade = CascadeType.ALL)
	private DataAttribute dataAttribute;
	// ToDo find a Way to persist this Object, now it leads to Errors
	private Serializable value;
	@OneToOne(cascade = CascadeType.ALL)
	private DataObject dataObject;


	/**
	 * for JPA only
	 */
	public DataAttributeInstance() {
		// JPA needs an empty constructor to instantiate objects of this class
		// at runtime.
	}


	public DataAttributeInstance(DataAttribute attribute, DataObject dataObject) {
		this.id = UUID.randomUUID().toString().replace("-", "");
		this.setDataAttribute(attribute);
		this.setDataObject(dataObject);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DataAttribute getDataAttribute() {
		return dataAttribute;
	}

	public void setDataAttribute(DataAttribute dataAttribute) {
		this.dataAttribute = dataAttribute;
	}

	public Object getValue() {
		return value;
	}

	// TODO: implement better type checking and allow others than String
	/**
	 * Set the value of the DataAttributeInstance but throws an exception if the
	 * value hasn't the right type.
	 * 
	 * @param value
	 */
	public void setValue(Object value) {
		if (isValueAllowed(value.toString())) {
			log.info("DataObject: Attribute value set.");
			getCaseExecutioner().logDataAttributeTransition(this, value);
			this.value = value.toString();
		} else {
			String errorMessage = String.format("Data attribute %s of data type %s could not be set to %s" + 
					" because value has wrong data type.", getDataAttribute().getName(), 
					getDataAttribute().getType(), value);
//			SseNotifier.notifyWarning(errorMessage);
			log.error(errorMessage);
		}
	}

	/**
	 * Checks if a given value is allowed for this attribute instance,
	 * e.g. if it fits to the data type.
	 *
	 * @param value
	 */
	public boolean isValueAllowed(String value) {
		try {
			validateValueType(value);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	// Copied from old version
	// TODO: look if could be maked better
	// TODO: put this in DataAttribute in CaseModel
	private void validateValueType(String value) {
		// TODO notify frontend when value is wrong
		String excp = "Could not set data attribute value " + "because it did not have the correct data type.";
		try {
			switch (dataAttribute.getType()) {
			case "Integer":
				Integer.valueOf(value);
				break;
			case "Double":
				Double.valueOf(value);
				break;
			case "Boolean":
				if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
					throw new IllegalArgumentException(excp);
				}
				break;
			case "Date":
				new SimpleDateFormat("dd.MM.yyyy").parse(value);
				break;
			case "String":
			case "Enum":
			case "Class":
				break;
			default:
				throw new IllegalArgumentException("Attribute data type is not supported.");
			}
		} catch (IllegalArgumentException | ParseException e) {
			throw new IllegalArgumentException(excp);
		}
	}

	public DataObject getDataObject() {
		return dataObject;
	}

	public void setDataObject(DataObject dataObject) {
		this.dataObject = dataObject;
	}

	public CaseExecutioner getCaseExecutioner() {
		return dataObject.getCaseExecutioner();
	}
}
