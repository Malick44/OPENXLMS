import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuizzComponent } from '../list/quizz.component';
import { QuizzDetailComponent } from '../detail/quizz-detail.component';
import { QuizzUpdateComponent } from '../update/quizz-update.component';
import { QuizzRoutingResolveService } from './quizz-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const quizzRoute: Routes = [
  {
    path: '',
    component: QuizzComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuizzDetailComponent,
    resolve: {
      quizz: QuizzRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuizzUpdateComponent,
    resolve: {
      quizz: QuizzRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuizzUpdateComponent,
    resolve: {
      quizz: QuizzRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(quizzRoute)],
  exports: [RouterModule],
})
export class QuizzRoutingModule {}
