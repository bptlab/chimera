import React, { Component } from "react";
import PropTypes from "prop-types";

class ExpandableOverview extends Component {
  state = { isCollapsed: true };

  invertCollapsed = () => {
    const isCollapsed = !this.state.isCollapsed;
    this.setState({ isCollapsed });
  };

  getCollapseButtonDirection = () => {
    const isCollapsed = this.state.isCollapsed;
    let collapseButtonDirection = "fa fa-angle-";
    collapseButtonDirection += isCollapsed ? "left" : "down";
    return collapseButtonDirection;
  };

  // <div className="card-footer">Panel Footer</div>

  render() {
    const { idx, header, children } = this.props;
    const style = {
      marginLeft: "30px"
    };
    const panelBody = this.state.isCollapsed ? (
      ""
    ) : (
      <div style={style} className="card">
        <div id={"collapse" + idx} className="card-collapse">
          <div className="card-body">{children}</div>
        </div>
      </div>
    );

    return (
      <div>
        <div className="card">
          <div className="card-header">
            <div className="card-title float-left h5">{header}</div>
            <div className="btn-group float-right">
              <a
                data-toggle={"collapse"}
                href={"#collapse" + idx}
                onClick={this.invertCollapsed}
              >
                <i className={this.getCollapseButtonDirection()} />
              </a>
            </div>
          </div>
        </div>
        {panelBody}
      </div>
    );
  }
}

ExpandableOverview.propTypes = {
  idx: PropTypes.number.isRequired,
  header: PropTypes.any.isRequired
};

export default ExpandableOverview;
