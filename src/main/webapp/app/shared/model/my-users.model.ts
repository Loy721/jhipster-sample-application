export interface IMyUsers {
  id?: number;
  name?: string | null;
  surname?: string | null;
}

export const defaultValue: Readonly<IMyUsers> = {};