import React, { Component } from "react";

class NewCaseNameModal extends Component {
  state = {
    name: ""
  };

  componentWillReceiveProps = ({ name }) => {
    this.setState({ name });
  };

  handleNameChange = name => {
    this.setState({ name });
  };

  handleEnterSubmit = e => {
    if (e.keyCode === 13) {
      this.handleSubmit();
    }
  };

  handleSubmit = () => {
    this.props.onSubmit(this.state.name);
  };

  render() {
    const { name } = this.state;
    return (
      <div
        className="modal fade bs-example-modal-sm"
        tabIndex="-1"
        role="dialog"
        aria-labelledby="newCaseNameModalTitle"
        id="newCaseNameModal"
      >
        <div className="modal-dialog">
          <div className="modal-content">
            <form>
              <div className="modal-header">
                <h4 className="modal-title" id="newCaseNameModalTitle">
                  Start a Case
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
                <fieldset className="form-group">
                  <label htmlFor="scenarioName">Case Name</label>
                  <input
                    type="text"
                    className="form-control"
                    id="newCaseNameModalName"
                    placeholder="Enter case name"
                    defaultValue={name}
                    onChange={evt => this.handleNameChange(evt.target.value)}
                    onKeyDown={this.handleEnterSubmit}
                  />
                </fieldset>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn" data-dismiss="modal">
                  Close
                </button>
                <button
                  type="button"
                  className="btn btn-default btn-primary"
                  onClick={this.handleSubmit}
                >
                  Start Case
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }
}

export default NewCaseNameModal;
