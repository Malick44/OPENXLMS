import dayjs from 'dayjs/esm';

import { IAssessment, NewAssessment } from './assessment.model';

export const sampleWithRequiredData: IAssessment = {
  id: 'd26b2791-fe8e-4355-b9ad-e97c8fbd9085',
  userId: 'system interface Towels',
  courseId: 'Bedfordshire',
  examDate: dayjs('2023-02-17'),
};

export const sampleWithPartialData: IAssessment = {
  id: 'a1f657cd-5e01-4c99-9433-24ad53df3b5b',
  userId: 'engine Jersey Tactics',
  courseId: 'online Awesome',
  title: 'deposit',
  sectionId: 'Account JBOD',
  examDate: dayjs('2023-02-17'),
  numberOfQuestions: 19371,
  timeLimit: 94013,
  score: 47545,
};

export const sampleWithFullData: IAssessment = {
  id: 'c22e62fe-8027-41a0-99b9-62f87996da08',
  userId: 'withdrawal',
  courseId: 'Drives drive Oklahoma',
  title: 'Franc Maine Product',
  sectionId: 'Profound tan productize',
  examDate: dayjs('2023-02-17'),
  numberOfQuestions: 80071,
  timeLimit: 4587,
  score: 46340,
};

export const sampleWithNewData: NewAssessment = {
  userId: 'Auto',
  courseId: 'Rwanda',
  examDate: dayjs('2023-02-16'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
