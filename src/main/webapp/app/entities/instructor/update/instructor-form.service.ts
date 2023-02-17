import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInstructor, NewInstructor } from '../instructor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInstructor for edit and NewInstructorFormGroupInput for create.
 */
type InstructorFormGroupInput = IInstructor | PartialWithRequiredKeyOf<NewInstructor>;

type InstructorFormDefaults = Pick<NewInstructor, 'id'>;

type InstructorFormGroupContent = {
  id: FormControl<IInstructor['id'] | NewInstructor['id']>;
  courseId: FormControl<IInstructor['courseId']>;
  name: FormControl<IInstructor['name']>;
  email: FormControl<IInstructor['email']>;
  instructorUrl: FormControl<IInstructor['instructorUrl']>;
  instructorThumbnail: FormControl<IInstructor['instructorThumbnail']>;
  instructorRating: FormControl<IInstructor['instructorRating']>;
  instructorRatingCount: FormControl<IInstructor['instructorRatingCount']>;
  instructorTotalStudents: FormControl<IInstructor['instructorTotalStudents']>;
  instructorTotalReviews: FormControl<IInstructor['instructorTotalReviews']>;
  ratingCount: FormControl<IInstructor['ratingCount']>;
};

export type InstructorFormGroup = FormGroup<InstructorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InstructorFormService {
  createInstructorFormGroup(instructor: InstructorFormGroupInput = { id: null }): InstructorFormGroup {
    const instructorRawValue = {
      ...this.getFormDefaults(),
      ...instructor,
    };
    return new FormGroup<InstructorFormGroupContent>({
      id: new FormControl(
        { value: instructorRawValue.id, disabled: instructorRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      courseId: new FormControl(instructorRawValue.courseId),
      name: new FormControl(instructorRawValue.name),
      email: new FormControl(instructorRawValue.email),
      instructorUrl: new FormControl(instructorRawValue.instructorUrl),
      instructorThumbnail: new FormControl(instructorRawValue.instructorThumbnail),
      instructorRating: new FormControl(instructorRawValue.instructorRating),
      instructorRatingCount: new FormControl(instructorRawValue.instructorRatingCount),
      instructorTotalStudents: new FormControl(instructorRawValue.instructorTotalStudents),
      instructorTotalReviews: new FormControl(instructorRawValue.instructorTotalReviews),
      ratingCount: new FormControl(instructorRawValue.ratingCount),
    });
  }

  getInstructor(form: InstructorFormGroup): IInstructor | NewInstructor {
    return form.getRawValue() as IInstructor | NewInstructor;
  }

  resetForm(form: InstructorFormGroup, instructor: InstructorFormGroupInput): void {
    const instructorRawValue = { ...this.getFormDefaults(), ...instructor };
    form.reset(
      {
        ...instructorRawValue,
        id: { value: instructorRawValue.id, disabled: instructorRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InstructorFormDefaults {
    return {
      id: null,
    };
  }
}
