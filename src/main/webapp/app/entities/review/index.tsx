import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Review from './review';
import ReviewUpdate from './review-update';
import ReviewDeleteDialog from './review-delete-dialog';
import ReviewAdd from './review-add';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ReviewUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ReviewUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:login/add`} component={ReviewAdd}/>v
      <ErrorBoundaryRoute path={match.url} component={Review} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ReviewDeleteDialog} />
  </>
);

export default Routes;
