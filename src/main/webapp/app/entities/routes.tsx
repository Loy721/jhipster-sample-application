import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MyFile from './my-file';
import MyTest from './my-test';
import MyQuestion from './my-question';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="my-file/*" element={<MyFile />} />
        <Route path="my-test/*" element={<MyTest />} />
        <Route path="my-question/*" element={<MyQuestion />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
