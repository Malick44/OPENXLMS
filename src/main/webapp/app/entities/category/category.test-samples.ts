import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: '0aaf2144-edc0-4adb-933d-efd5dc5e205d',
};

export const sampleWithPartialData: ICategory = {
  id: '84bd1bfc-1212-41b6-9d99-9b9e680cf6dd',
};

export const sampleWithFullData: ICategory = {
  id: '7bb57ea6-4269-4101-80cf-16d432ddbfe9',
  name: 'array port initiative',
  description: 'Accountability Music indexing',
};

export const sampleWithNewData: NewCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
