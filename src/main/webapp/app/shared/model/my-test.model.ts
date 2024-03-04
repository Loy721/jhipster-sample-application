import { IQuestion } from 'app/shared/model/question.model';
import { IFile } from 'app/shared/model/file.model';

export interface IMyTest {
  id?: number;
  name?: string | null;
  myQuestions?: IQuestion[] | null;
  file?: IFile | null;
}

export const defaultValue: Readonly<IMyTest> = {};
