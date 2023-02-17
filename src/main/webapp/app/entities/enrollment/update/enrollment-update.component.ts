import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EnrollmentFormService, EnrollmentFormGroup } from './enrollment-form.service';
import { IEnrollment } from '../enrollment.model';
import { EnrollmentService } from '../service/enrollment.service';

@Component({
  selector: 'jhi-enrollment-update',
  templateUrl: './enrollment-update.component.html',
})
export class EnrollmentUpdateComponent implements OnInit {
  isSaving = false;
  enrollment: IEnrollment | null = null;

  editForm: EnrollmentFormGroup = this.enrollmentFormService.createEnrollmentFormGroup();

  constructor(
    protected enrollmentService: EnrollmentService,
    protected enrollmentFormService: EnrollmentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enrollment }) => {
      this.enrollment = enrollment;
      if (enrollment) {
        this.updateForm(enrollment);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enrollment = this.enrollmentFormService.getEnrollment(this.editForm);
    if (enrollment.id !== null) {
      this.subscribeToSaveResponse(this.enrollmentService.update(enrollment));
    } else {
      this.subscribeToSaveResponse(this.enrollmentService.create(enrollment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnrollment>>): void {
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

  protected updateForm(enrollment: IEnrollment): void {
    this.enrollment = enrollment;
    this.enrollmentFormService.resetForm(this.editForm, enrollment);
  }
}
