import { IFiles } from 'app/shared/model/files.model';

export interface ITests {
  id?: number;
  name?: string | null;
  questions?: string | null;
  files?: IFiles | null;
}

export const defaultValue: Readonly<ITests> = {};
