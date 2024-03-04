import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MyFile from './my-file';
import MyFileDetail from './my-file-detail';
import MyFileUpdate from './my-file-update';
import MyFileDeleteDialog from './my-file-delete-dialog';

const MyFileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MyFile />} />
    <Route path="new" element={<MyFileUpdate />} />
    <Route path=":id">
      <Route index element={<MyFileDetail />} />
      <Route path="edit" element={<MyFileUpdate />} />
      <Route path="delete" element={<MyFileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MyFileRoutes;
