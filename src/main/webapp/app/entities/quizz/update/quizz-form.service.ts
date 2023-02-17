import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IQuizz, NewQuizz } from '../quizz.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IQuizz for edit and NewQuizzFormGroupInput for create.
 */
type QuizzFormGroupInput = IQuizz | PartialWithRequiredKeyOf<NewQuizz>;

type QuizzFormDefaults = Pick<NewQuizz, 'id'>;

type QuizzFormGroupContent = {
  id: FormControl<IQuizz['id'] | NewQuizz['id']>;
  userId: FormControl<IQuizz['userId']>;
  courseId: FormControl<IQuizz['courseId']>;
  title: FormControl<IQuizz['title']>;
  sectionId: FormControl<IQuizz['sectionId']>;
  examDate: FormControl<IQuizz['examDate']>;
  numberOfQuestions: FormControl<IQuizz['numberOfQuestions']>;
  timeLimit: FormControl<IQuizz['timeLimit']>;
  score: FormControl<IQuizz['score']>;
};

export type QuizzFormGroup = FormGroup<QuizzFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class QuizzFormService {
  createQuizzFormGroup(quizz: QuizzFormGroupInput = { id: null }): QuizzFormGroup {
    const quizzRawValue = {
      ...this.getFormDefaults(),
      ...quizz,
    };
    return new FormGroup<QuizzFormGroupContent>({
      id: new FormControl(
        { value: quizzRawValue.id, disabled: quizzRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      userId: new FormControl(quizzRawValue.userId, {
        validators: [Validators.required],
      }),
      courseId: new FormControl(quizzRawValue.courseId, {
        validators: [Validators.required],
      }),
      title: new FormControl(quizzRawValue.title),
      sectionId: new FormControl(quizzRawValue.sectionId),
      examDate: new FormControl(quizzRawValue.examDate, {
        validators: [Validators.required],
      }),
      numberOfQuestions: new FormControl(quizzRawValue.numberOfQuestions),
      timeLimit: new FormControl(quizzRawValue.timeLimit),
      score: new FormControl(quizzRawValue.score),
    });
  }

  getQuizz(form: QuizzFormGroup): IQuizz | NewQuizz {
    return form.getRawValue() as IQuizz | NewQuizz;
  }

  resetForm(form: QuizzFormGroup, quizz: QuizzFormGroupInput): void {
    const quizzRawValue = { ...this.getFormDefaults(), ...quizz };
    form.reset(
      {
        ...quizzRawValue,
        id: { value: quizzRawValue.id, disabled: quizzRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): QuizzFormDefaults {
    return {
      id: null,
    };
  }
}
