package de.hpi.bpt.chimera.rest.beans.activity;

import de.hpi.bpt.chimera.execution.data.DataObject;
import de.hpi.bpt.chimera.model.datamodel.DataClass;
import de.hpi.bpt.chimera.model.datamodel.ObjectLifecycleState;
import de.hpi.bpt.chimera.rest.beans.datamodel.DataAttributeJaxBeanNew;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataJaxBean {
    private String dataclassName;
    private List<String> availableStates;
    private List<DataAttributeJaxBeanNew> attributes;

    public DataJaxBean(DataClass dataclass, List<ObjectLifecycleState> states, List<DataObject> dataObjects) {
        setDataclassName(dataclass.getName());
        setAvailableStates(states.stream()
                            .map(ObjectLifecycleState::getName)
                            .collect(Collectors.toList()));

        Optional<DataObject> dataObject = dataObjects.stream()
                                            .filter(d -> d.getDataClass().equals(dataclass))
                                            .findFirst();

        if (dataObject.isPresent()) {
            // Transition
            setAttributes(dataObject.get().getDataAttributeInstances().stream()
                            .map(DataAttributeJaxBeanNew::new)
                            .collect(Collectors.toList()));
        } else {
            // Creation
            setAttributes(dataclass.getDataAttributes().stream()
                            .map(DataAttributeJaxBeanNew::new)
                            .collect(Collectors.toList()));
        }
    }

    public String getDataclassName() {
        return dataclassName;
    }

    public void setDataclassName(String dataclassName) {
        this.dataclassName = dataclassName;
    }

    public List<String> getAvailableStates() {
        return availableStates;
    }

    public void setAvailableStates(List<String> availableStates) {
        this.availableStates = availableStates;
    }

    public List<DataAttributeJaxBeanNew> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<DataAttributeJaxBeanNew> attributes) {
        this.attributes = attributes;
    }
}
