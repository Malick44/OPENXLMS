import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QuizzComponent } from './list/quizz.component';
import { QuizzDetailComponent } from './detail/quizz-detail.component';
import { QuizzUpdateComponent } from './update/quizz-update.component';
import { QuizzDeleteDialogComponent } from './delete/quizz-delete-dialog.component';
import { QuizzRoutingModule } from './route/quizz-routing.module';

@NgModule({
  imports: [SharedModule, QuizzRoutingModule],
  declarations: [QuizzComponent, QuizzDetailComponent, QuizzUpdateComponent, QuizzDeleteDialogComponent],
})
export class QuizzModule {}
