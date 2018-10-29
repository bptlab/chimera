import React, { Component } from "react";
import PropTypes from "prop-types";

import SearchField from "../components/SearchField"

class SearchableList extends Component {
  state = {
    elements: [],
    displayedElements : [],
    filterString : "",
  };

  componentDidMount(){
    this.setState({
      elements: this.props.elements,
      displayedElements: this.props.elements,
    });
  };

  componentWillReceiveProps(nextProps){
    /*alert(nextProps.elements);
    this.setState({
      elements: nextProps.elements,
    });
    this.filterElements();*/
    this.filterElements(nextProps.elements, this.state.filterString);
  }

  filterElements(elements, filterString){
    let filteredElements = elements.filter(element => element.includes(filterString));
    this.setState({
      displayedElements: filteredElements,
      filterString: filterString,
    });
  }

  render() {
    return (
      <React.Fragment>
        <SearchField searchHandler = {filterString => {
            this.filterElements(this.props.elements, filterString);
          }}
        />
        <main className="main-container">
          <div className="container">
            <ul className="list-group">
              {this.state.displayedElements.map((element) =>
                <li className="list-group-item" key={element}>{element}</li>
              )}
            </ul>
          </div>
        </main>
      </React.Fragment>
    );
  }
}

SearchableList.propTypes = {
  elements: PropTypes.arrayOf(PropTypes.string).isRequired
};

export default SearchableList;
