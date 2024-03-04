import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Files from './files';
import FilesDetail from './files-detail';
import FilesUpdate from './files-update';
import FilesDeleteDialog from './files-delete-dialog';

const FilesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Files />} />
    <Route path="new" element={<FilesUpdate />} />
    <Route path=":id">
      <Route index element={<FilesDetail />} />
      <Route path="edit" element={<FilesUpdate />} />
      <Route path="delete" element={<FilesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FilesRoutes;
