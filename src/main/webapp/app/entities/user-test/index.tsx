import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserTest from './user-test';
import UserTestDetail from './user-test-detail';
import UserTestUpdate from './user-test-update';
import UserTestDeleteDialog from './user-test-delete-dialog';

const UserTestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserTest />} />
    <Route path="new" element={<UserTestUpdate />} />
    <Route path=":id">
      <Route index element={<UserTestDetail />} />
      <Route path="edit" element={<UserTestUpdate />} />
      <Route path="delete" element={<UserTestDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserTestRoutes;
