import React, { Component } from "react";
import BpmnViewer from "bpmn-js";

class BpmnFragment extends Component {
  constructor(props) {
    super(props);
    this.viewer = new BpmnViewer();
  }

  componentDidMount = () => {
    const { id } = this.props;
    this.viewer.attachTo("#" + id);
  };

  importXML = xml => {
    const viewer = this.viewer;
    // import diagram
    viewer.importXML(xml, function(err) {
      if (err) {
        return console.error("could not import BPMN 3.0 diagram", err);
      }

      var canvas = viewer.get("canvas");

      // // zoom to fit full viewport
      canvas.zoom("fit-viewport", "auto");
    });
  };

  render() {
    const { id, xml } = this.props;
    this.importXML(xml, this.viewer);

    return <div id={id} />;
  }
}

export default BpmnFragment;
