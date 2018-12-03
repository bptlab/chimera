import React, { Component } from "react";
import PropTypes from "prop-types";

import SearchField from "../components/SearchField";

class SearchableList extends Component {
  state = {
    elements: [],
    displayedElements: [],
    filterString: "",
    width: 0,
    height: 0
  };

  componentDidMount() {
    this.setState({
      elements: this.props.elements,
      displayedElements: this.props.elements,
      width: window.innerWidth,
      height: window.innerHeight
    });
    window.addEventListener("resize", event => this.updateWindowDimensions());
  }

  componentWillUnmount() {
    window.removeEventListener("resize", even => this.updateWindowDimensions());
  }

  updateWindowDimensions() {
    this.setState({ width: window.innerWidth, height: window.innerHeight });
  }

  componentWillReceiveProps(nextProps) {
    /*alert(nextProps.elements);
    this.setState({
      elements: nextProps.elements,
    });
    this.filterElements();*/
    this.filterElements(nextProps.elements, this.state.filterString);
  }

  filterElements(elements, filterString) {
    let filteredElements = elements.filter(element =>
      element.includes(filterString)
    );
    this.setState({
      displayedElements: filteredElements,
      filterString: filterString
    });
  }

  render() {
    return (
      <React.Fragment>
        <SearchField
          searchHandler={filterString => {
            this.filterElements(this.props.elements, filterString);
          }}
        />
        <div style={{ overflow: "auto", height: this.state.height * 0.5 }}>
          <main className="main-container">
            <div className="container">
              <ul className="list-group">
                {this.state.displayedElements.map(element => (
                  <li className="list-group-item" key={element}>
                    {element}
                  </li>
                ))}
              </ul>
            </div>
          </main>
        </div>
      </React.Fragment>
    );
  }
}

SearchableList.propTypes = {
  elements: PropTypes.arrayOf(PropTypes.string).isRequired
};

export default SearchableList;
