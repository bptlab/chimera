//package de.hpi.bpt.chimera.model.datamodel;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//
//import de.hpi.bpt.chimera.model.Listable;
//import de.hpi.bpt.chimera.model.Nameable;
//
//@Entity
//public abstract class DataModelClass implements Nameable, Listable {
//	@Id
//	@GeneratedValue
//	private int dbId;
//	private String name;
//	@OneToMany(cascade = CascadeType.ALL)
//	private List<DataAttribute> dataAttributes;
//
//	@Override
//	public String getName() {
//		return name;
//	}
//
//	@Override
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public List<DataAttribute> getDataAttributes() {
//		return dataAttributes;
//	}
//
//	public void setDataAttributes(List<DataAttribute> dataAttributes) {
//		this.dataAttributes = dataAttributes;
//	}
//
//	public Map<String, DataAttribute> getNameToDataAttribute() {
//		Map<String, DataAttribute> nameToDataAttribute = new HashMap<>();
//
//		for (DataAttribute dataAttribute : this.dataAttributes) {
//			nameToDataAttribute.put(dataAttribute.getName(), dataAttribute);
//		}
//		return nameToDataAttribute;
//	}
//}
