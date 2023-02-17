export interface IOption {
  id: string;
  text?: string | null;
  questionId?: string | null;
  correct?: boolean | null;
  assessmentId?: string | null;
  assignmentId?: string | null;
  quizzId?: string | null;
}

export type NewOption = Omit<IOption, 'id'> & { id: null };
