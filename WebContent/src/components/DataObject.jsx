import React, { Component } from "react";

class DataObject extends Component {
  render() {
    const { dataObject, handleClick } = this.props;

    return (
      <button
        className="btn"
        data-toggle="modal"
        data-target="#viewDataObjectModal"
        onClick={() => handleClick(dataObject)}
      >
        {dataObject.dataclass}
        <br />[{dataObject.state}]
      </button>
    );
  }
}

export default DataObject;
