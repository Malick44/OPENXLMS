import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { QuizzFormService, QuizzFormGroup } from './quizz-form.service';
import { IQuizz } from '../quizz.model';
import { QuizzService } from '../service/quizz.service';

@Component({
  selector: 'jhi-quizz-update',
  templateUrl: './quizz-update.component.html',
})
export class QuizzUpdateComponent implements OnInit {
  isSaving = false;
  quizz: IQuizz | null = null;

  editForm: QuizzFormGroup = this.quizzFormService.createQuizzFormGroup();

  constructor(
    protected quizzService: QuizzService,
    protected quizzFormService: QuizzFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quizz }) => {
      this.quizz = quizz;
      if (quizz) {
        this.updateForm(quizz);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quizz = this.quizzFormService.getQuizz(this.editForm);
    if (quizz.id !== null) {
      this.subscribeToSaveResponse(this.quizzService.update(quizz));
    } else {
      this.subscribeToSaveResponse(this.quizzService.create(quizz));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuizz>>): void {
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

  protected updateForm(quizz: IQuizz): void {
    this.quizz = quizz;
    this.quizzFormService.resetForm(this.editForm, quizz);
  }
}
