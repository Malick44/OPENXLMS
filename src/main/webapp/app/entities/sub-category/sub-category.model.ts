export interface ISubCategory {
  id: string;
  name?: string | null;
  description?: string | null;
}

export type NewSubCategory = Omit<ISubCategory, 'id'> & { id: null };
