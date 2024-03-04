import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MyUsers from './my-users';
import MyUsersDetail from './my-users-detail';
import MyUsersUpdate from './my-users-update';
import MyUsersDeleteDialog from './my-users-delete-dialog';

const MyUsersRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MyUsers />} />
    <Route path="new" element={<MyUsersUpdate />} />
    <Route path=":id">
      <Route index element={<MyUsersDetail />} />
      <Route path="edit" element={<MyUsersUpdate />} />
      <Route path="delete" element={<MyUsersDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MyUsersRoutes;
