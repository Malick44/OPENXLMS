export interface IInstructor {
  id: string;
  courseId?: string | null;
  name?: string | null;
  email?: string | null;
  instructorUrl?: string | null;
  instructorThumbnail?: string | null;
  instructorRating?: string | null;
  instructorRatingCount?: string | null;
  instructorTotalStudents?: string | null;
  instructorTotalReviews?: string | null;
  ratingCount?: string | null;
}

export type NewInstructor = Omit<IInstructor, 'id'> & { id: null };
