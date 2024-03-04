import files from 'app/entities/files/files.reducer';
import tests from 'app/entities/tests/tests.reducer';
import questions from 'app/entities/questions/questions.reducer';
import userTest from 'app/entities/user-test/user-test.reducer';
import myUsers from 'app/entities/my-users/my-users.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  files,
  tests,
  questions,
  userTest,
  myUsers,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
