import { ITests } from 'app/shared/model/tests.model';

export interface IFiles {
  id?: number;
  topic?: string | null;
  content?: string | null;
  tests?: ITests[] | null;
}

export const defaultValue: Readonly<IFiles> = {};
