import React, { Component } from "react";

class AttributeConfiguration extends Component {
  attributeValue = attribute => {
    const { editable, handleAttributeValueChanges } = this.props;
    return (
      <input
        type="text"
        className="form-control"
        value={attribute.value || ""}
        onChange={event =>
          handleAttributeValueChanges(attribute.name, event.target.value)
        }
        readOnly={!editable}
      />
    );
  };

  render() {
    const { attributes } = this.props;
    return (
      <div>
        {attributes.map((attribute, idx) => (
          <div key={idx} className="input-group mb-3">
            <div className="input-group-prepend">
              <label className="input-group-text">{attribute.name}</label>
            </div>
            {this.attributeValue(attribute)}
            <div className="input-group-append">
              <label className="input-group-text">{attribute.type}</label>
            </div>
          </div>
        ))}
      </div>
    );
  }
}

export default AttributeConfiguration;
