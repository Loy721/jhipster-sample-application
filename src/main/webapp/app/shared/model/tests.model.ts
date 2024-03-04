export interface ITests {
  id?: number;
  name?: string | null;
  questions?: string | null;
}

export const defaultValue: Readonly<ITests> = {};
