import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEnrollment, NewEnrollment } from '../enrollment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEnrollment for edit and NewEnrollmentFormGroupInput for create.
 */
type EnrollmentFormGroupInput = IEnrollment | PartialWithRequiredKeyOf<NewEnrollment>;

type EnrollmentFormDefaults = Pick<NewEnrollment, 'id'>;

type EnrollmentFormGroupContent = {
  id: FormControl<IEnrollment['id'] | NewEnrollment['id']>;
  courseId: FormControl<IEnrollment['courseId']>;
  userId: FormControl<IEnrollment['userId']>;
  enrolledDate: FormControl<IEnrollment['enrolledDate']>;
  completionRate: FormControl<IEnrollment['completionRate']>;
  completedDate: FormControl<IEnrollment['completedDate']>;
};

export type EnrollmentFormGroup = FormGroup<EnrollmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnrollmentFormService {
  createEnrollmentFormGroup(enrollment: EnrollmentFormGroupInput = { id: null }): EnrollmentFormGroup {
    const enrollmentRawValue = {
      ...this.getFormDefaults(),
      ...enrollment,
    };
    return new FormGroup<EnrollmentFormGroupContent>({
      id: new FormControl(
        { value: enrollmentRawValue.id, disabled: enrollmentRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      courseId: new FormControl(enrollmentRawValue.courseId, {
        validators: [Validators.required],
      }),
      userId: new FormControl(enrollmentRawValue.userId, {
        validators: [Validators.required],
      }),
      enrolledDate: new FormControl(enrollmentRawValue.enrolledDate, {
        validators: [Validators.required],
      }),
      completionRate: new FormControl(enrollmentRawValue.completionRate),
      completedDate: new FormControl(enrollmentRawValue.completedDate),
    });
  }

  getEnrollment(form: EnrollmentFormGroup): IEnrollment | NewEnrollment {
    return form.getRawValue() as IEnrollment | NewEnrollment;
  }

  resetForm(form: EnrollmentFormGroup, enrollment: EnrollmentFormGroupInput): void {
    const enrollmentRawValue = { ...this.getFormDefaults(), ...enrollment };
    form.reset(
      {
        ...enrollmentRawValue,
        id: { value: enrollmentRawValue.id, disabled: enrollmentRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EnrollmentFormDefaults {
    return {
      id: null,
    };
  }
}
