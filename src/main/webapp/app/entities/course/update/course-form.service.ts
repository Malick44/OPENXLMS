import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICourse, NewCourse } from '../course.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICourse for edit and NewCourseFormGroupInput for create.
 */
type CourseFormGroupInput = ICourse | PartialWithRequiredKeyOf<NewCourse>;

type CourseFormDefaults = Pick<NewCourse, 'id'>;

type CourseFormGroupContent = {
  id: FormControl<ICourse['id'] | NewCourse['id']>;
  title: FormControl<ICourse['title']>;
  authorId: FormControl<ICourse['authorId']>;
  description: FormControl<ICourse['description']>;
  instructorId: FormControl<ICourse['instructorId']>;
  level: FormControl<ICourse['level']>;
  language: FormControl<ICourse['language']>;
  duration: FormControl<ICourse['duration']>;
  price: FormControl<ICourse['price']>;
  rating: FormControl<ICourse['rating']>;
  ratingCount: FormControl<ICourse['ratingCount']>;
  thumbnail: FormControl<ICourse['thumbnail']>;
  url: FormControl<ICourse['url']>;
  categoryId: FormControl<ICourse['categoryId']>;
  subCategoryId: FormControl<ICourse['subCategoryId']>;
};

export type CourseFormGroup = FormGroup<CourseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CourseFormService {
  createCourseFormGroup(course: CourseFormGroupInput = { id: null }): CourseFormGroup {
    const courseRawValue = {
      ...this.getFormDefaults(),
      ...course,
    };
    return new FormGroup<CourseFormGroupContent>({
      id: new FormControl(
        { value: courseRawValue.id, disabled: courseRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(courseRawValue.title, {
        validators: [Validators.required],
      }),
      authorId: new FormControl(courseRawValue.authorId, {
        validators: [Validators.required],
      }),
      description: new FormControl(courseRawValue.description),
      instructorId: new FormControl(courseRawValue.instructorId),
      level: new FormControl(courseRawValue.level),
      language: new FormControl(courseRawValue.language),
      duration: new FormControl(courseRawValue.duration),
      price: new FormControl(courseRawValue.price),
      rating: new FormControl(courseRawValue.rating),
      ratingCount: new FormControl(courseRawValue.ratingCount),
      thumbnail: new FormControl(courseRawValue.thumbnail),
      url: new FormControl(courseRawValue.url),
      categoryId: new FormControl(courseRawValue.categoryId),
      subCategoryId: new FormControl(courseRawValue.subCategoryId),
    });
  }

  getCourse(form: CourseFormGroup): ICourse | NewCourse {
    return form.getRawValue() as ICourse | NewCourse;
  }

  resetForm(form: CourseFormGroup, course: CourseFormGroupInput): void {
    const courseRawValue = { ...this.getFormDefaults(), ...course };
    form.reset(
      {
        ...courseRawValue,
        id: { value: courseRawValue.id, disabled: courseRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CourseFormDefaults {
    return {
      id: null,
    };
  }
}
