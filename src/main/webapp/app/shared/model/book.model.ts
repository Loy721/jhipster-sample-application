import { IAuthor } from 'app/shared/model/author.model';

export interface IBook {
  id?: number;
  title?: string | null;
  author?: IAuthor | null;
}

export const defaultValue: Readonly<IBook> = {};
