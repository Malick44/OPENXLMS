import dayjs from 'dayjs/esm';

export interface IAuthor {
  id: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  password?: string | null;
  createDate?: dayjs.Dayjs | null;
  avatarUrl?: string | null;
  activated?: boolean | null;
  langKey?: string | null;
  resetDate?: dayjs.Dayjs | null;
  resetKey?: string | null;
  authorities?: string | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  courseId?: string | null;
}

export type NewAuthor = Omit<IAuthor, 'id'> & { id: null };
