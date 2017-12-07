import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Inventory } from './inventory.model';
import { InventoryPopupService } from './inventory-popup.service';
import { InventoryService } from './inventory.service';
import { User, UserService } from '../../shared';
import { Ingredient, IngredientService } from '../ingredient';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-inventory-dialog',
    templateUrl: './inventory-dialog.component.html'
})
export class InventoryDialogComponent implements OnInit {

    inventory: Inventory;
    isSaving: boolean;

    users: User[];

    ingredients: Ingredient[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private inventoryService: InventoryService,
        private userService: UserService,
        private ingredientService: IngredientService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.ingredientService.query()
            .subscribe((res: ResponseWrapper) => { this.ingredients = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.inventory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.inventoryService.update(this.inventory));
        } else {
            this.subscribeToSaveResponse(
                this.inventoryService.create(this.inventory));
        }
    }

    private subscribeToSaveResponse(result: Observable<Inventory>) {
        result.subscribe((res: Inventory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Inventory) {
        this.eventManager.broadcast({ name: 'inventoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackIngredientById(index: number, item: Ingredient) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-inventory-popup',
    template: ''
})
export class InventoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private inventoryPopupService: InventoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.inventoryPopupService
                    .open(InventoryDialogComponent as Component, params['id']);
            } else {
                this.inventoryPopupService
                    .open(InventoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
