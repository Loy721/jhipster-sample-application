import { IMyTest } from 'app/shared/model/my-test.model';

export interface IMyFile {
  id?: number;
  name?: string | null;
  books?: IMyTest[] | null;
}

export const defaultValue: Readonly<IMyFile> = {};
