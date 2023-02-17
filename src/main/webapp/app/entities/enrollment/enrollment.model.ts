import dayjs from 'dayjs/esm';

export interface IEnrollment {
  id: string;
  courseId?: string | null;
  userId?: string | null;
  enrolledDate?: dayjs.Dayjs | null;
  completionRate?: number | null;
  completedDate?: dayjs.Dayjs | null;
}

export type NewEnrollment = Omit<IEnrollment, 'id'> & { id: null };
