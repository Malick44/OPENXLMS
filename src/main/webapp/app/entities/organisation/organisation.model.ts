export interface IOrganisation {
  id: string;
  name?: string | null;
  description?: string | null;
}

export type NewOrganisation = Omit<IOrganisation, 'id'> & { id: null };
