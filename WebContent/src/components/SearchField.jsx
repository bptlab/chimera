import React, { Component } from "react";

class SearchField extends Component{

  constructor(props){
    super(props);
    this.state = {
      inputValue: '',
    }
  }

  updateInputValue(evt){
    this.setState({
      inputValue: evt.target.value,
    })
  }

  searchClicked(){
    this.props.searchHandler(this.state.inputValue)
  }

  render(){
    return (
      <div class="container">
        <div class="row">
          <div class="col-sm-6 col-sm-offset-3">
            <div id="imaginary_container">
              <div class="input-group stylish-input-group">
                <input type="text" class="form-control"  placeholder="Search" value={this.state.inputValue} onChange={evt => this.updateInputValue(evt)}/>
                  <span class="input-group-addon">
                    <button type="button" onClick={() => this.searchClicked()} >
                       <span class="glyphicon glyphicon-search">apply filter</span>
                    </button>
                  </span>
                </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default SearchField
