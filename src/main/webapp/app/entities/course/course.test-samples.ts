import { ICourse, NewCourse } from './course.model';

export const sampleWithRequiredData: ICourse = {
  id: '98c9991d-c9da-411e-a53c-64ed275db9c4',
  title: 'Lake',
  authorId: 'Borders',
};

export const sampleWithPartialData: ICourse = {
  id: 'acb4f8a0-9b63-4de5-9584-b16b949f8c36',
  title: 'mindshare calculating haptic',
  authorId: 'bluetooth Handmade violet',
  instructorId: 'Generic Car secured',
  level: 'Legacy',
  language: 'Frozen Lempira',
  duration: 'Circles Delaware contextually-based',
  price: 'Response Shore Investment',
  ratingCount: 'auxiliary',
  thumbnail: 'calculate target',
  subCategoryId: 'Macao',
};

export const sampleWithFullData: ICourse = {
  id: 'efe3e5f3-1563-4340-bbb1-a022eb5810fd',
  title: 'navigate redundant',
  authorId: 'Total Usability holistic',
  description: 'deposit',
  instructorId: 'Fresh',
  level: 'bleeding-edge',
  language: 'mobile Infrastructure',
  duration: 'Mouse Response Account',
  price: 'monitor Steel Belgium',
  rating: 'wireless quantify neural-net',
  ratingCount: 'New Florida Peso',
  thumbnail: 'blue Route Concrete',
  url: 'http://zachary.com',
  categoryId: 'Assistant Buckinghamshire program',
  subCategoryId: 'expedite',
};

export const sampleWithNewData: NewCourse = {
  title: 'Manager AGP Zambia',
  authorId: 'Camp Market',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
