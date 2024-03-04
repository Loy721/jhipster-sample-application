import { IMyTest } from 'app/shared/model/my-test.model';

export interface IFile {
  id?: number;
  name?: string | null;
  books?: IMyTest[] | null;
}

export const defaultValue: Readonly<IFile> = {};
