import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SubCategoryFormService, SubCategoryFormGroup } from './sub-category-form.service';
import { ISubCategory } from '../sub-category.model';
import { SubCategoryService } from '../service/sub-category.service';

@Component({
  selector: 'jhi-sub-category-update',
  templateUrl: './sub-category-update.component.html',
})
export class SubCategoryUpdateComponent implements OnInit {
  isSaving = false;
  subCategory: ISubCategory | null = null;

  editForm: SubCategoryFormGroup = this.subCategoryFormService.createSubCategoryFormGroup();

  constructor(
    protected subCategoryService: SubCategoryService,
    protected subCategoryFormService: SubCategoryFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subCategory }) => {
      this.subCategory = subCategory;
      if (subCategory) {
        this.updateForm(subCategory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subCategory = this.subCategoryFormService.getSubCategory(this.editForm);
    if (subCategory.id !== null) {
      this.subscribeToSaveResponse(this.subCategoryService.update(subCategory));
    } else {
      this.subscribeToSaveResponse(this.subCategoryService.create(subCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubCategory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(subCategory: ISubCategory): void {
    this.subCategory = subCategory;
    this.subCategoryFormService.resetForm(this.editForm, subCategory);
  }
}
