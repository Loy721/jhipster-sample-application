import file from 'app/entities/file/file.reducer';
import myTest from 'app/entities/my-test/my-test.reducer';
import question from 'app/entities/question/question.reducer';
import myUser from 'app/entities/my-user/my-user.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  file,
  myTest,
  question,
  myUser,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
