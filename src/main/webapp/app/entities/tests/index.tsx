import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Tests from './tests';
import TestsDetail from './tests-detail';
import TestsUpdate from './tests-update';
import TestsDeleteDialog from './tests-delete-dialog';

const TestsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Tests />} />
    <Route path="new" element={<TestsUpdate />} />
    <Route path=":id">
      <Route index element={<TestsDetail />} />
      <Route path="edit" element={<TestsUpdate />} />
      <Route path="delete" element={<TestsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TestsRoutes;
