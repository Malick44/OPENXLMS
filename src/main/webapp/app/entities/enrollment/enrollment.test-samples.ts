import dayjs from 'dayjs/esm';

import { IEnrollment, NewEnrollment } from './enrollment.model';

export const sampleWithRequiredData: IEnrollment = {
  id: 'e36a7f7e-21a4-434d-b05f-3ca24535d038',
  courseId: 'Bhutan Martinique',
  userId: 'interface Automated',
  enrolledDate: dayjs('2023-02-16'),
};

export const sampleWithPartialData: IEnrollment = {
  id: '335ea232-326a-4f95-a916-4c4366c35720',
  courseId: 'invoice RSS',
  userId: 'Investment',
  enrolledDate: dayjs('2023-02-16'),
};

export const sampleWithFullData: IEnrollment = {
  id: '024ac4f1-179a-4c68-884d-11939c00bada',
  courseId: 'compressing Pants',
  userId: 'capacitor',
  enrolledDate: dayjs('2023-02-17'),
  completionRate: 18294,
  completedDate: dayjs('2023-02-16'),
};

export const sampleWithNewData: NewEnrollment = {
  courseId: 'Ball port',
  userId: 'neutral',
  enrolledDate: dayjs('2023-02-16'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
