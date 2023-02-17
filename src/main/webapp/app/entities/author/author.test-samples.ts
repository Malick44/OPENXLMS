import dayjs from 'dayjs/esm';

import { IAuthor, NewAuthor } from './author.model';

export const sampleWithRequiredData: IAuthor = {
  id: 'dbd9c993-6c25-4aac-bd4d-ce7fecacc819',
  firstName: 'Magdalena',
  password: 'interface Ways Tunisia',
  createDate: dayjs('2023-02-16T18:08'),
  activated: false,
  authorities: 'synergies Bolivia Small',
};

export const sampleWithPartialData: IAuthor = {
  id: 'b4e0762b-fa00-4c71-bdc7-53f2e811099b',
  firstName: 'Milford',
  password: 'AI impactful mint',
  createDate: dayjs('2023-02-16T23:47'),
  avatarUrl: 'withdrawal',
  activated: false,
  authorities: 'synthesize synergize',
  lastModifiedBy: 'compressing interfaces scalable',
  lastModifiedDate: dayjs('2023-02-17T15:39'),
  courseId: 'Toys parse',
};

export const sampleWithFullData: IAuthor = {
  id: 'f7ee0444-a119-4c3c-8221-6a6f5058ee4c',
  firstName: 'Olin',
  lastName: 'Dickinson',
  email: 'Arnaldo.Effertz87@hotmail.com',
  password: 'Infrastructure Jordan transmitting',
  createDate: dayjs('2023-02-17T03:56'),
  avatarUrl: 'invoice Tools Persistent',
  activated: false,
  langKey: 'magenta synergistic Associate',
  resetDate: dayjs('2023-02-16T21:23'),
  resetKey: 'withdrawal',
  authorities: 'Branch Handcrafted Books',
  createdBy: 'Wooden orchid Krona',
  lastModifiedBy: 'policy',
  lastModifiedDate: dayjs('2023-02-17T02:14'),
  courseId: 'markets',
};

export const sampleWithNewData: NewAuthor = {
  firstName: 'Jesse',
  password: 'Ball Fresh Drives',
  createDate: dayjs('2023-02-17T11:51'),
  activated: true,
  authorities: 'Tobago Corporate',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
