import dayjs from 'dayjs/esm';

export interface IRating {
  id: string;
  userId?: string | null;
  courseId?: string | null;
  instractorId?: string | null;
  value?: number | null;
  comment?: string | null;
  timestamp?: dayjs.Dayjs | null;
}

export type NewRating = Omit<IRating, 'id'> & { id: null };
