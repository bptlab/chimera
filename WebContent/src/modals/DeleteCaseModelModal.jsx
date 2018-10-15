import React from "react";

const DeleteCaseModelModal = props => {
  const { name, onSubmit } = props;
  return (
    <div
      className="modal fade bs-example-modal-sm"
      tabIndex="-1"
      role="dialog"
      aria-labelledby="deleteCaseModelModalTitle"
      id="deleteCaseModelModal"
    >
      <div className="modal-dialog">
        <div className="modal-content">
          <form>
            <div className="modal-header">
              <h4 className="modal-title" id="deleteCaseModelModalTitle">
                Delete the Casemodel
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
              <label>
                Do you really want to delete '{name}
                '?
              </label>
            </div>
            <div className="modal-footer">
              <button type="button" className="btn" data-dismiss="modal">
                Close
              </button>
              <button
                type="button"
                className="btn btn-default btn-danger"
                onClick={onSubmit}
              >
                Delete
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default DeleteCaseModelModal;
