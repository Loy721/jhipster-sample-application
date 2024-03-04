import { IMyQuestion } from 'app/shared/model/my-question.model';
import { IFile } from 'app/shared/model/file.model';

export interface IMyTest {
  id?: number;
  title?: string | null;
  publishedBooks?: IMyQuestion[] | null;
  author?: IFile | null;
}

export const defaultValue: Readonly<IMyTest> = {};
