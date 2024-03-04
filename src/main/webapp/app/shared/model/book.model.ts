import { IAuthor } from 'app/shared/model/author.model';
import { IPublisher } from 'app/shared/model/publisher.model';

export interface IBook {
  id?: number;
  title?: string | null;
  author?: IAuthor | null;
  publisher?: IPublisher | null;
}

export const defaultValue: Readonly<IBook> = {};
