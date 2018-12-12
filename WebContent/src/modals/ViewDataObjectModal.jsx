import React, { Component, Fragment } from "react";
import AttributeConfiguration from "../components/modals/AttributeConfiguration";

class ViewDataObjectModalBody extends Component {
  render() {
    const { dataObject } = this.props;

    return (
      <Fragment>
        <p>{dataObject.id}</p>
        {dataObject.locked ? (
          <p className="text-danger">locked</p>
        ) : (
          <p className="text-success">unlocked</p>
        )}
        <hr />
        <AttributeConfiguration
          editable={false}
          attributes={dataObject.attributes}
        />
      </Fragment>
    );
  }
}

class ViewDataObjectModal extends Component {
  render() {
    const { dataObject } = this.props;

    return (
      <div
        className="modal fade bs-example-modal-sm"
        tabIndex="-1"
        role="dialog"
        aria-labelledby="viewDataObjectModalTitle"
        id="viewDataObjectModal"
      >
        <div className="modal-dialog">
          <div className="modal-content">
            <form>
              <div className="modal-header">
                <h4 className="modal-title" id="viewDataObjectModalTitle">
                  {dataObject.dataclass}[{dataObject.state}]
                </h4>
                <button
                  type="button"
                  className="close"
                  data-dismiss="modal"
                  aria-label="Close"
                >
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <div className="modal-body">
                <ViewDataObjectModalBody dataObject={dataObject} />
              </div>
              <div className="modal-footer">
                <button type="button" className="btn" data-dismiss="modal">
                  Close
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }
}

export default ViewDataObjectModal;
