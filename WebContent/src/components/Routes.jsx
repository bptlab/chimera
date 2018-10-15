import React from "react";
import { Switch, Route } from "react-router-dom";

import OrganizationView from "../containers/OrganizationView";
import CaseModelView from "../containers/CaseModelView";
import CaseView from "../containers/CaseView";
import NotFound from "./NotFound";

const Routes = () => (
  <Switch>
    <Route exact path="/casemodels" component={OrganizationView} />
    <Route exact path="/casemodels/:cmId" component={CaseModelView} />
    <Route exact path="/casemodels/:cmId/cases/:caseId" component={CaseView} />
    <Route exact path="/swagger" />
    <Route path="*" component={NotFound} />
  </Switch>
);

export default Routes;
