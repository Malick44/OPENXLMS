import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAuthor, NewAuthor } from '../author.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAuthor for edit and NewAuthorFormGroupInput for create.
 */
type AuthorFormGroupInput = IAuthor | PartialWithRequiredKeyOf<NewAuthor>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAuthor | NewAuthor> = Omit<T, 'createDate' | 'resetDate' | 'lastModifiedDate'> & {
  createDate?: string | null;
  resetDate?: string | null;
  lastModifiedDate?: string | null;
};

type AuthorFormRawValue = FormValueOf<IAuthor>;

type NewAuthorFormRawValue = FormValueOf<NewAuthor>;

type AuthorFormDefaults = Pick<NewAuthor, 'id' | 'createDate' | 'activated' | 'resetDate' | 'lastModifiedDate'>;

type AuthorFormGroupContent = {
  id: FormControl<AuthorFormRawValue['id'] | NewAuthor['id']>;
  firstName: FormControl<AuthorFormRawValue['firstName']>;
  lastName: FormControl<AuthorFormRawValue['lastName']>;
  email: FormControl<AuthorFormRawValue['email']>;
  password: FormControl<AuthorFormRawValue['password']>;
  createDate: FormControl<AuthorFormRawValue['createDate']>;
  avatarUrl: FormControl<AuthorFormRawValue['avatarUrl']>;
  activated: FormControl<AuthorFormRawValue['activated']>;
  langKey: FormControl<AuthorFormRawValue['langKey']>;
  resetDate: FormControl<AuthorFormRawValue['resetDate']>;
  resetKey: FormControl<AuthorFormRawValue['resetKey']>;
  authorities: FormControl<AuthorFormRawValue['authorities']>;
  createdBy: FormControl<AuthorFormRawValue['createdBy']>;
  lastModifiedBy: FormControl<AuthorFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<AuthorFormRawValue['lastModifiedDate']>;
  courseId: FormControl<AuthorFormRawValue['courseId']>;
};

export type AuthorFormGroup = FormGroup<AuthorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AuthorFormService {
  createAuthorFormGroup(author: AuthorFormGroupInput = { id: null }): AuthorFormGroup {
    const authorRawValue = this.convertAuthorToAuthorRawValue({
      ...this.getFormDefaults(),
      ...author,
    });
    return new FormGroup<AuthorFormGroupContent>({
      id: new FormControl(
        { value: authorRawValue.id, disabled: authorRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(authorRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(authorRawValue.lastName),
      email: new FormControl(authorRawValue.email),
      password: new FormControl(authorRawValue.password, {
        validators: [Validators.required],
      }),
      createDate: new FormControl(authorRawValue.createDate, {
        validators: [Validators.required],
      }),
      avatarUrl: new FormControl(authorRawValue.avatarUrl),
      activated: new FormControl(authorRawValue.activated, {
        validators: [Validators.required],
      }),
      langKey: new FormControl(authorRawValue.langKey),
      resetDate: new FormControl(authorRawValue.resetDate),
      resetKey: new FormControl(authorRawValue.resetKey),
      authorities: new FormControl(authorRawValue.authorities, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(authorRawValue.createdBy),
      lastModifiedBy: new FormControl(authorRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(authorRawValue.lastModifiedDate),
      courseId: new FormControl(authorRawValue.courseId),
    });
  }

  getAuthor(form: AuthorFormGroup): IAuthor | NewAuthor {
    return this.convertAuthorRawValueToAuthor(form.getRawValue() as AuthorFormRawValue | NewAuthorFormRawValue);
  }

  resetForm(form: AuthorFormGroup, author: AuthorFormGroupInput): void {
    const authorRawValue = this.convertAuthorToAuthorRawValue({ ...this.getFormDefaults(), ...author });
    form.reset(
      {
        ...authorRawValue,
        id: { value: authorRawValue.id, disabled: authorRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AuthorFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createDate: currentTime,
      activated: false,
      resetDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertAuthorRawValueToAuthor(rawAuthor: AuthorFormRawValue | NewAuthorFormRawValue): IAuthor | NewAuthor {
    return {
      ...rawAuthor,
      createDate: dayjs(rawAuthor.createDate, DATE_TIME_FORMAT),
      resetDate: dayjs(rawAuthor.resetDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawAuthor.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertAuthorToAuthorRawValue(
    author: IAuthor | (Partial<NewAuthor> & AuthorFormDefaults)
  ): AuthorFormRawValue | PartialWithRequiredKeyOf<NewAuthorFormRawValue> {
    return {
      ...author,
      createDate: author.createDate ? author.createDate.format(DATE_TIME_FORMAT) : undefined,
      resetDate: author.resetDate ? author.resetDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: author.lastModifiedDate ? author.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
