export interface ICourse {
  id: string;
  title?: string | null;
  authorId?: string | null;
  description?: string | null;
  instructorId?: string | null;
  level?: string | null;
  language?: string | null;
  duration?: string | null;
  price?: string | null;
  rating?: string | null;
  ratingCount?: string | null;
  thumbnail?: string | null;
  url?: string | null;
  categoryId?: string | null;
  subCategoryId?: string | null;
}

export type NewCourse = Omit<ICourse, 'id'> & { id: null };
