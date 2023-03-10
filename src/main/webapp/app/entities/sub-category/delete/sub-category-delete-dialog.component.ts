import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubCategory } from '../sub-category.model';
import { SubCategoryService } from '../service/sub-category.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './sub-category-delete-dialog.component.html',
})
export class SubCategoryDeleteDialogComponent {
  subCategory?: ISubCategory;

  constructor(protected subCategoryService: SubCategoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.subCategoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
