import { ISubCategory, NewSubCategory } from './sub-category.model';

export const sampleWithRequiredData: ISubCategory = {
  id: 'b2ab9e94-6f5f-4b04-9ac3-29df2bf9612d',
};

export const sampleWithPartialData: ISubCategory = {
  id: '025625ec-0a8c-445f-8035-73abcf21c650',
  name: 'Concrete',
};

export const sampleWithFullData: ISubCategory = {
  id: 'aef9a9b0-d96b-4193-9175-3d3a17787c47',
  name: 'content Account Handmade',
  description: 'Incredible SMS program',
};

export const sampleWithNewData: NewSubCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
