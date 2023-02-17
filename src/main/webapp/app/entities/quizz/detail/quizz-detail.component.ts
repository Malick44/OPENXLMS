import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuizz } from '../quizz.model';

@Component({
  selector: 'jhi-quizz-detail',
  templateUrl: './quizz-detail.component.html',
})
export class QuizzDetailComponent implements OnInit {
  quizz: IQuizz | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quizz }) => {
      this.quizz = quizz;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
