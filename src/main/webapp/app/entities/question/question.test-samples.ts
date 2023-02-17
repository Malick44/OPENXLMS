import { IQuestion, NewQuestion } from './question.model';

export const sampleWithRequiredData: IQuestion = {
  id: '7e95d524-aea6-438c-8962-aac87478f875',
  courseId: 'Lats Namibia',
};

export const sampleWithPartialData: IQuestion = {
  id: 'abf579bc-6639-4b8b-9afd-0869cf04d05e',
  sectionId: 'Vermont Account',
  courseId: 'Solutions',
  text: 'Alaska',
  assessmentId: 'Inlet',
  quizzId: 'Strategist grow web',
};

export const sampleWithFullData: IQuestion = {
  id: '830a1e78-eaf6-4b2f-ba6c-5c9f9d992a0a',
  sectionId: 'dot-com Forward strategize',
  courseId: 'Missouri violet firewall',
  text: 'Plains',
  assignmentId: 'Principal',
  assessmentId: 'Intelligent vortals',
  quizzId: '1080p Danish withdrawal',
};

export const sampleWithNewData: NewQuestion = {
  courseId: 'Pound',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
