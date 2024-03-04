import { IBook } from 'app/shared/model/book.model';

export interface IAuthor {
  id?: number;
  name?: string | null;
  books?: IBook[] | null;
}

export const defaultValue: Readonly<IAuthor> = {};
