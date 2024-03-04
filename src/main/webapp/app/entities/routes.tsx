import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Files from './files';
import Tests from './tests';
import Questions from './questions';
import UserTest from './user-test';
import MyUsers from './my-users';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="files/*" element={<Files />} />
        <Route path="tests/*" element={<Tests />} />
        <Route path="questions/*" element={<Questions />} />
        <Route path="user-test/*" element={<UserTest />} />
        <Route path="my-users/*" element={<MyUsers />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
