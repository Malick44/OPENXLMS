import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuizz } from '../quizz.model';
import { QuizzService } from '../service/quizz.service';

@Injectable({ providedIn: 'root' })
export class QuizzRoutingResolveService implements Resolve<IQuizz | null> {
  constructor(protected service: QuizzService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuizz | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((quizz: HttpResponse<IQuizz>) => {
          if (quizz.body) {
            return of(quizz.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
