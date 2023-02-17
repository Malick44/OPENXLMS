import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAssessment, NewAssessment } from '../assessment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAssessment for edit and NewAssessmentFormGroupInput for create.
 */
type AssessmentFormGroupInput = IAssessment | PartialWithRequiredKeyOf<NewAssessment>;

type AssessmentFormDefaults = Pick<NewAssessment, 'id'>;

type AssessmentFormGroupContent = {
  id: FormControl<IAssessment['id'] | NewAssessment['id']>;
  userId: FormControl<IAssessment['userId']>;
  courseId: FormControl<IAssessment['courseId']>;
  title: FormControl<IAssessment['title']>;
  sectionId: FormControl<IAssessment['sectionId']>;
  examDate: FormControl<IAssessment['examDate']>;
  numberOfQuestions: FormControl<IAssessment['numberOfQuestions']>;
  timeLimit: FormControl<IAssessment['timeLimit']>;
  score: FormControl<IAssessment['score']>;
};

export type AssessmentFormGroup = FormGroup<AssessmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AssessmentFormService {
  createAssessmentFormGroup(assessment: AssessmentFormGroupInput = { id: null }): AssessmentFormGroup {
    const assessmentRawValue = {
      ...this.getFormDefaults(),
      ...assessment,
    };
    return new FormGroup<AssessmentFormGroupContent>({
      id: new FormControl(
        { value: assessmentRawValue.id, disabled: assessmentRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      userId: new FormControl(assessmentRawValue.userId, {
        validators: [Validators.required],
      }),
      courseId: new FormControl(assessmentRawValue.courseId, {
        validators: [Validators.required],
      }),
      title: new FormControl(assessmentRawValue.title),
      sectionId: new FormControl(assessmentRawValue.sectionId),
      examDate: new FormControl(assessmentRawValue.examDate, {
        validators: [Validators.required],
      }),
      numberOfQuestions: new FormControl(assessmentRawValue.numberOfQuestions),
      timeLimit: new FormControl(assessmentRawValue.timeLimit),
      score: new FormControl(assessmentRawValue.score),
    });
  }

  getAssessment(form: AssessmentFormGroup): IAssessment | NewAssessment {
    return form.getRawValue() as IAssessment | NewAssessment;
  }

  resetForm(form: AssessmentFormGroup, assessment: AssessmentFormGroupInput): void {
    const assessmentRawValue = { ...this.getFormDefaults(), ...assessment };
    form.reset(
      {
        ...assessmentRawValue,
        id: { value: assessmentRawValue.id, disabled: assessmentRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AssessmentFormDefaults {
    return {
      id: null,
    };
  }
}
