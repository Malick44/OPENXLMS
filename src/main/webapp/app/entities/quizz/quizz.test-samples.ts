import dayjs from 'dayjs/esm';

import { IQuizz, NewQuizz } from './quizz.model';

export const sampleWithRequiredData: IQuizz = {
  id: '048abb5a-4260-4ec7-8bea-4ad84ebd5c74',
  userId: 'Granite',
  courseId: 'Soft local Ball',
  examDate: dayjs('2023-02-17'),
};

export const sampleWithPartialData: IQuizz = {
  id: 'e9ca69f2-7aeb-49a7-9c49-b98249fb46ca',
  userId: 'withdrawal Dynamic Incredible',
  courseId: 'synthesizing Borders 1080p',
  title: 'Vista',
  examDate: dayjs('2023-02-17'),
  numberOfQuestions: 89584,
  timeLimit: 54514,
  score: 61073,
};

export const sampleWithFullData: IQuizz = {
  id: '560559b0-4256-4275-83a0-80632fa56999',
  userId: 'PNG Avon',
  courseId: 'fuchsia',
  title: 'synergies',
  sectionId: 'SAS Avon',
  examDate: dayjs('2023-02-17'),
  numberOfQuestions: 32699,
  timeLimit: 87948,
  score: 25266,
};

export const sampleWithNewData: NewQuizz = {
  userId: 'scalable Avon',
  courseId: 'approach Concrete',
  examDate: dayjs('2023-02-16'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
