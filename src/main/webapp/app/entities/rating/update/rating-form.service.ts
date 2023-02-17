import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRating, NewRating } from '../rating.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRating for edit and NewRatingFormGroupInput for create.
 */
type RatingFormGroupInput = IRating | PartialWithRequiredKeyOf<NewRating>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRating | NewRating> = Omit<T, 'timestamp'> & {
  timestamp?: string | null;
};

type RatingFormRawValue = FormValueOf<IRating>;

type NewRatingFormRawValue = FormValueOf<NewRating>;

type RatingFormDefaults = Pick<NewRating, 'id' | 'timestamp'>;

type RatingFormGroupContent = {
  id: FormControl<RatingFormRawValue['id'] | NewRating['id']>;
  userId: FormControl<RatingFormRawValue['userId']>;
  courseId: FormControl<RatingFormRawValue['courseId']>;
  instractorId: FormControl<RatingFormRawValue['instractorId']>;
  value: FormControl<RatingFormRawValue['value']>;
  comment: FormControl<RatingFormRawValue['comment']>;
  timestamp: FormControl<RatingFormRawValue['timestamp']>;
};

export type RatingFormGroup = FormGroup<RatingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RatingFormService {
  createRatingFormGroup(rating: RatingFormGroupInput = { id: null }): RatingFormGroup {
    const ratingRawValue = this.convertRatingToRatingRawValue({
      ...this.getFormDefaults(),
      ...rating,
    });
    return new FormGroup<RatingFormGroupContent>({
      id: new FormControl(
        { value: ratingRawValue.id, disabled: ratingRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      userId: new FormControl(ratingRawValue.userId),
      courseId: new FormControl(ratingRawValue.courseId),
      instractorId: new FormControl(ratingRawValue.instractorId),
      value: new FormControl(ratingRawValue.value),
      comment: new FormControl(ratingRawValue.comment),
      timestamp: new FormControl(ratingRawValue.timestamp),
    });
  }

  getRating(form: RatingFormGroup): IRating | NewRating {
    return this.convertRatingRawValueToRating(form.getRawValue() as RatingFormRawValue | NewRatingFormRawValue);
  }

  resetForm(form: RatingFormGroup, rating: RatingFormGroupInput): void {
    const ratingRawValue = this.convertRatingToRatingRawValue({ ...this.getFormDefaults(), ...rating });
    form.reset(
      {
        ...ratingRawValue,
        id: { value: ratingRawValue.id, disabled: ratingRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RatingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timestamp: currentTime,
    };
  }

  private convertRatingRawValueToRating(rawRating: RatingFormRawValue | NewRatingFormRawValue): IRating | NewRating {
    return {
      ...rawRating,
      timestamp: dayjs(rawRating.timestamp, DATE_TIME_FORMAT),
    };
  }

  private convertRatingToRatingRawValue(
    rating: IRating | (Partial<NewRating> & RatingFormDefaults)
  ): RatingFormRawValue | PartialWithRequiredKeyOf<NewRatingFormRawValue> {
    return {
      ...rating,
      timestamp: rating.timestamp ? rating.timestamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
