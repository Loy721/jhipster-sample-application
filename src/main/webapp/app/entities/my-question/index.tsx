import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MyQuestion from './my-question';
import MyQuestionDetail from './my-question-detail';
import MyQuestionUpdate from './my-question-update';
import MyQuestionDeleteDialog from './my-question-delete-dialog';

const MyQuestionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MyQuestion />} />
    <Route path="new" element={<MyQuestionUpdate />} />
    <Route path=":id">
      <Route index element={<MyQuestionDetail />} />
      <Route path="edit" element={<MyQuestionUpdate />} />
      <Route path="delete" element={<MyQuestionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MyQuestionRoutes;
