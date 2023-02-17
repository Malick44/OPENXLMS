import dayjs from 'dayjs/esm';

export interface IAssignment {
  id: string;
  userId?: string | null;
  courseId?: string | null;
  title?: string | null;
  sectionId?: string | null;
  examDate?: dayjs.Dayjs | null;
  numberOfQuestions?: number | null;
  timeLimit?: number | null;
  score?: number | null;
}

export type NewAssignment = Omit<IAssignment, 'id'> & { id: null };
