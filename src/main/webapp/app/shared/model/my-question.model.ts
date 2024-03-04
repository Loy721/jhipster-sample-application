import { IMyTest } from 'app/shared/model/my-test.model';

export interface IMyQuestion {
  id?: number;
  name?: string | null;
  publisher?: IMyTest | null;
}

export const defaultValue: Readonly<IMyQuestion> = {};
