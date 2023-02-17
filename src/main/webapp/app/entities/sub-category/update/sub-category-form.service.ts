import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISubCategory, NewSubCategory } from '../sub-category.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISubCategory for edit and NewSubCategoryFormGroupInput for create.
 */
type SubCategoryFormGroupInput = ISubCategory | PartialWithRequiredKeyOf<NewSubCategory>;

type SubCategoryFormDefaults = Pick<NewSubCategory, 'id'>;

type SubCategoryFormGroupContent = {
  id: FormControl<ISubCategory['id'] | NewSubCategory['id']>;
  name: FormControl<ISubCategory['name']>;
  description: FormControl<ISubCategory['description']>;
};

export type SubCategoryFormGroup = FormGroup<SubCategoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubCategoryFormService {
  createSubCategoryFormGroup(subCategory: SubCategoryFormGroupInput = { id: null }): SubCategoryFormGroup {
    const subCategoryRawValue = {
      ...this.getFormDefaults(),
      ...subCategory,
    };
    return new FormGroup<SubCategoryFormGroupContent>({
      id: new FormControl(
        { value: subCategoryRawValue.id, disabled: subCategoryRawValue.id !== null },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(subCategoryRawValue.name),
      description: new FormControl(subCategoryRawValue.description),
    });
  }

  getSubCategory(form: SubCategoryFormGroup): ISubCategory | NewSubCategory {
    return form.getRawValue() as ISubCategory | NewSubCategory;
  }

  resetForm(form: SubCategoryFormGroup, subCategory: SubCategoryFormGroupInput): void {
    const subCategoryRawValue = { ...this.getFormDefaults(), ...subCategory };
    form.reset(
      {
        ...subCategoryRawValue,
        id: { value: subCategoryRawValue.id, disabled: subCategoryRawValue.id !== null },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SubCategoryFormDefaults {
    return {
      id: null,
    };
  }
}
