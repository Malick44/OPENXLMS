import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuizz } from '../quizz.model';
import { QuizzService } from '../service/quizz.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './quizz-delete-dialog.component.html',
})
export class QuizzDeleteDialogComponent {
  quizz?: IQuizz;

  constructor(protected quizzService: QuizzService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.quizzService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
