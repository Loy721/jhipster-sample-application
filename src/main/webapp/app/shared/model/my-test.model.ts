import { IMyQuestion } from 'app/shared/model/my-question.model';
import { IMyFile } from 'app/shared/model/my-file.model';

export interface IMyTest {
  id?: number;
  title?: string | null;
  publishedBooks?: IMyQuestion[] | null;
  author?: IMyFile | null;
}

export const defaultValue: Readonly<IMyTest> = {};
