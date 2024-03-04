export interface IFiles {
  id?: number;
  topic?: string | null;
  content?: string | null;
}

export const defaultValue: Readonly<IFiles> = {};
