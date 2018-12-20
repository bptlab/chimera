import React, { Component } from "react";

// class Filter extends Component {
//   render() {
//     const { handleClick } = this.props;

//     return (
//       <div className="col-2 input-group mb-3">
//         <div className="input-group-prepend">
//           <div className="input-group-text">
//             <input
//               type="checkbox"
//               onClick={evt => handleClick(evt.target.checked)}
//             />
//           </div>
//         </div>
//         <input
//           type="text"
//           className="form-control"
//           disabled
//           defaultValue={"Last 5min"}
//         />
//       </div>
//     );
//   }
// }

class SortBy extends Component {
  render() {
    const { options, onChange } = this.props;
    return (
      <div className="col-4">
        <select
          className="custom-select"
          onChange={evt => onChange(evt.target.value)}
        >
          {options.map(o => {
            return <option key={o}>{o}</option>;
          })}
        </select>
      </div>
    );
  }
}

class SearchField extends Component {
  updateInputValue(evt) {
    this.props.onChange(evt.target.value);
  }

  render() {
    return (
      <div className="col-4 input-group mb-3">
        <div className="input-group-prepend">
          <span className="input-group-text" id="basic-addon1">
            <i className="fa fa-search" />
          </span>
        </div>
        <input
          type="text"
          className="form-control"
          placeholder="Search"
          onChange={evt => this.updateInputValue(evt)}
        />
      </div>
      // <div className="container">
      //   <div className="col-sm-6 col-sm-offset-3">
      //     <div id="imaginary_container">
      //       <div className="input-group stylish-input-group">
      //         <input
      //           type="text"
      //           className="form-control"
      //           placeholder="Search"
      //           value={this.state.inputValue}
      //           onChange={evt => this.updateInputValue(evt)}
      //         />
      //         <span className="input-group-addon">
      //           <button type="button" onClick={() => this.searchClicked()}>
      //             <span className="glyphicon glyphicon-search">
      //               apply filter
      //             </span>
      //           </button>
      //         </span>
      //       </div>
      //     </div>
      //   </div>
      // </div>
    );
  }
}

class ListArrangeOptions extends Component {
  state = {};
  render() {
    const { sortingOptions, onSortingChange, onFilterChange } = this.props;
    return (
      <div className="row">
        <SortBy options={sortingOptions} onChange={onSortingChange} />
        <SearchField onChange={onFilterChange} />
      </div>
    );
  }
}

export default ListArrangeOptions;
