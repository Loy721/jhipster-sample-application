import { IMyTest } from 'app/shared/model/my-test.model';

export interface IFile {
  id?: number;
  topic?: string | null;
  content?: string | null;
  myTests?: IMyTest[] | null;
}

export const defaultValue: Readonly<IFile> = {};
