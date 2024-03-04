import { IBook } from 'app/shared/model/book.model';

export interface IPublisher {
  id?: number;
  name?: string | null;
  publishedBooks?: IBook[] | null;
}

export const defaultValue: Readonly<IPublisher> = {};
