import React, { Component } from "react";
import { BrowserRouter } from "react-router-dom";

import Footer from "./containers/Footer";
import Routes from "./components/Routes";

class App extends Component {
  render() {
    return (
      <React.Fragment>
        <BrowserRouter basename={process.env.REACT_APP_ROUTER_BASE || ""}>
          <Routes />
        </BrowserRouter>

        <Footer />
      </React.Fragment>
    );
  }
}

export default App;
