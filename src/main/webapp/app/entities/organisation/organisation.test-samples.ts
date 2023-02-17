import { IOrganisation, NewOrganisation } from './organisation.model';

export const sampleWithRequiredData: IOrganisation = {
  id: '0bf0a074-d77e-4f94-b122-d2c4658e6098',
  name: 'Gloves Customer CSS',
};

export const sampleWithPartialData: IOrganisation = {
  id: '9f0ba955-9d7c-484f-a2d8-0b3f9907b4d4',
  name: 'software',
};

export const sampleWithFullData: IOrganisation = {
  id: 'f92e33fb-4a2b-4cb8-a026-ef9617d2f1e0',
  name: 'Borders',
  description: 'Mauritius tolerance',
};

export const sampleWithNewData: NewOrganisation = {
  name: 'Dollar Metal',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
