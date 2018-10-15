import React, { Component } from "react";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";

import { getCaseModel, startCase, deleteCaseModel } from "../API";
import NavBar from "./NavBar";
import ExpandableOverview from "../components/ExpandableOverview";
import NewCaseNameModal from "../modals/NewCaseModal";
import DeleteCaseModelModal from "../modals/DeleteCaseModelModal";

class CaseModelDetails extends Component {
  state = {
    id: ""
  };

  componentWillReceiveProps = ({ casemodel }) => {
    this.setState({ id: casemodel.id });
  };

  handleDeleteCaseModelSubmit = async () => {
    const { id } = this.state;
    await deleteCaseModel(id);
    window.location = `${window.location.origin}/${
      process.env.REACT_APP_ROUTER_BASE
    }/casemodels`;
  };

  // start a case and change the window location
  handleNewCaseSubmit = async name => {
    const { id } = this.state;
    const response = await startCase(id, name);
    const caseId = response.id;

    window.location = `${window.location.origin}/${
      process.env.REACT_APP_ROUTER_BASE
    }/casemodels/${id}/cases/${caseId}`;
  };

  startCase = async casemodel => {};

  render() {
    const { casemodel } = this.props;
    return (
      <main className="main-container">
        <NewCaseNameModal
          onSubmit={this.handleNewCaseSubmit}
          name={casemodel.name}
        />
        <DeleteCaseModelModal
          onSubmit={this.handleDeleteCaseModelSubmit}
          name={casemodel.name}
        />
        <h1>{casemodel.name}</h1>
        <h5>{casemodel.description}</h5>
        <h5>Deployed on {casemodel.deployment}</h5>
        <h5>Version: {casemodel.modelversion}</h5>
        <button
          className="btn btn-danger"
          data-toggle="modal"
          data-target="#deleteCaseModelModal"
        >
          <i className="fa fa-trash-o" /> Delete Casemodel
        </button>
        <button
          className="btn btn-primary"
          data-toggle="modal"
          data-target="#newCaseNameModal"
        >
          New Case
        </button>
      </main>
    );
  }
}

class CaseModelView extends Component {
  state = {
    casemodel: { name: "", id: "", deployment: "", modelVersion: 0, cases: [] }
  };

  componentDidMount = async () => {
    const { cmId } = this.props.match.params;
    const casemodel = await getCaseModel(cmId);
    this.setState({ casemodel });
  };

  render() {
    const { casemodel } = this.state;
    return (
      <React.Fragment>
        <NavBar casemodel={casemodel} />
        <div className="container">
          <CaseModelDetails casemodel={casemodel} />
          <h4>Cases ({casemodel.cases.length})</h4>
          {casemodel.cases.map((c, idx) => (
            <ExpandableOverview
              key={idx}
              idx={idx}
              header={
                <Link to={casemodel.id + "/cases/" + c.id}>{c.name}</Link>
              }
              body={
                <div>
                  <h6>{c.terminated ? "Terminated" : "Running"}</h6>
                  <h6>
                    {c.canTerminate ? "can terminate" : "cannot terminate"}
                  </h6>
                  <h6>{c.instantiation}</h6>
                </div>
              }
            />
          ))}
        </div>
      </React.Fragment>
    );
  }
}

CaseModelView.propTypes = {
  casemodel: PropTypes.shape({
    name: PropTypes.string.isRequired,
    id: PropTypes.string.isRequired,
    deployment: PropTypes.instanceOf(Date).isRequired,
    modelversion: PropTypes.number.isRequired,
    cases: PropTypes.arrayOf(
      PropTypes.shape({ name: PropTypes.string.isRequired })
    ).isRequired
  })
};

export default CaseModelView;
