import { IInstructor, NewInstructor } from './instructor.model';

export const sampleWithRequiredData: IInstructor = {
  id: 'e3e2faa7-4e20-447d-8f58-4c9a105ed62d',
};

export const sampleWithPartialData: IInstructor = {
  id: '8412ea52-c397-4c4e-871e-d99c77c8472f',
  email: 'Thurman19@gmail.com',
  instructorUrl: 'card blue silver',
  instructorRating: 'incubate navigating Niger',
  instructorTotalReviews: 'Quality-focused Electronics',
};

export const sampleWithFullData: IInstructor = {
  id: 'ae5ef3ca-e70b-48c1-bb2e-76296cccf745',
  courseId: 'Optimization',
  name: 'real-time cultivate Planner',
  email: 'Christine_Mills@gmail.com',
  instructorUrl: 'ADP TCP',
  instructorThumbnail: 'Applications mobile',
  instructorRating: 'generating Designer Toys',
  instructorRatingCount: 'Officer Bedfordshire Plaza',
  instructorTotalStudents: 'withdrawal overriding impactful',
  instructorTotalReviews: 'Burgs Dakota',
  ratingCount: 'monitor Upgradable Electronics',
};

export const sampleWithNewData: NewInstructor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
