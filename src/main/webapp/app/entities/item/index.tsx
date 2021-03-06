import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Item from './item';
import ItemUpdate from './item-update';
import ItemDeleteDialog from './item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ItemUpdate} />
      <ErrorBoundaryRoute path={match.url} component={Item} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ItemDeleteDialog} />
  </>
);

export default Routes;
