import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Questions from './questions';
import QuestionsDetail from './questions-detail';
import QuestionsUpdate from './questions-update';
import QuestionsDeleteDialog from './questions-delete-dialog';

const QuestionsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Questions />} />
    <Route path="new" element={<QuestionsUpdate />} />
    <Route path=":id">
      <Route index element={<QuestionsDetail />} />
      <Route path="edit" element={<QuestionsUpdate />} />
      <Route path="delete" element={<QuestionsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QuestionsRoutes;
