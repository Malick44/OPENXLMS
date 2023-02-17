import dayjs from 'dayjs/esm';

import { IRating, NewRating } from './rating.model';

export const sampleWithRequiredData: IRating = {
  id: 'ee6a755f-88af-4513-b28c-d62e7692563d',
};

export const sampleWithPartialData: IRating = {
  id: 'a243990d-583a-4760-a917-e2e6aa977999',
  courseId: 'program Concrete',
  instractorId: 'calculating',
  value: 64269,
  timestamp: dayjs('2023-02-17T10:29'),
};

export const sampleWithFullData: IRating = {
  id: 'dda7c2c1-eff4-4c44-a822-3c1b2f077a7c',
  userId: 'payment Tasty',
  courseId: 'mindshare',
  instractorId: 'HTTP mission-critical',
  value: 42862,
  comment: 'Faso',
  timestamp: dayjs('2023-02-17T05:13'),
};

export const sampleWithNewData: NewRating = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
