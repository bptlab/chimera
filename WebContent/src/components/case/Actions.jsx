import React, { Component } from "react";

class CloseCaseButton extends Component {
  render() {
    const { caze, handleCloseCase } = this.props;
    if (caze.canTerminate) {
      return (
        <button className="btn btn-danger" onClick={handleCloseCase}>
          Close Case
        </button>
      );
    }
    return null;
  }
}

class ShowLogButton extends Component {
  render() {
    return <button className="btn btn-light">Show Log</button>;
  }
}

class AbortCaseButton extends Component {
  render() {
    return <button className="btn btn-warning">Abort Case</button>;
  }
}

export { ShowLogButton, AbortCaseButton, CloseCaseButton };
