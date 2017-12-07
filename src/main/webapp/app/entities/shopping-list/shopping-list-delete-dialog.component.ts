import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ShoppingList } from './shopping-list.model';
import { ShoppingListPopupService } from './shopping-list-popup.service';
import { ShoppingListService } from './shopping-list.service';

@Component({
    selector: 'jhi-shopping-list-delete-dialog',
    templateUrl: './shopping-list-delete-dialog.component.html'
})
export class ShoppingListDeleteDialogComponent {

    shoppingList: ShoppingList;

    constructor(
        private shoppingListService: ShoppingListService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.shoppingListService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'shoppingListListModification',
                content: 'Deleted an shoppingList'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-shopping-list-delete-popup',
    template: ''
})
export class ShoppingListDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private shoppingListPopupService: ShoppingListPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.shoppingListPopupService
                .open(ShoppingListDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
