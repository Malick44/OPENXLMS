import dayjs from 'dayjs/esm';

import { IAssignment, NewAssignment } from './assignment.model';

export const sampleWithRequiredData: IAssignment = {
  id: '7c8d53f5-eff5-447c-ae25-3ef3db33f497',
  userId: 'Grocery monitor Account',
  courseId: 'drive',
  examDate: dayjs('2023-02-17'),
};

export const sampleWithPartialData: IAssignment = {
  id: '559870dc-a444-415e-9343-1e0203884daf',
  userId: 'mobile Fresh',
  courseId: 'needs-based Generic',
  sectionId: 'Dynamic cyan',
  examDate: dayjs('2023-02-16'),
  numberOfQuestions: 54778,
  timeLimit: 43356,
  score: 95293,
};

export const sampleWithFullData: IAssignment = {
  id: '5709b7fb-eecd-401b-aa9b-b24a32d5e307',
  userId: 'navigating',
  courseId: 'Wooden mobile',
  title: 'reintermediate bus',
  sectionId: 'Ergonomic',
  examDate: dayjs('2023-02-16'),
  numberOfQuestions: 48361,
  timeLimit: 1118,
  score: 76785,
};

export const sampleWithNewData: NewAssignment = {
  userId: 'Fresh connecting',
  courseId: 'Liberian Market Dollar',
  examDate: dayjs('2023-02-17'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
