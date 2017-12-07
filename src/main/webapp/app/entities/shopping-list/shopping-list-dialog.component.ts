import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ShoppingList } from './shopping-list.model';
import { ShoppingListPopupService } from './shopping-list-popup.service';
import { ShoppingListService } from './shopping-list.service';
import { User, UserService } from '../../shared';
import { Ingredient, IngredientService } from '../ingredient';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-shopping-list-dialog',
    templateUrl: './shopping-list-dialog.component.html'
})
export class ShoppingListDialogComponent implements OnInit {

    shoppingList: ShoppingList;
    isSaving: boolean;

    users: User[];

    ingredients: Ingredient[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private shoppingListService: ShoppingListService,
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
        if (this.shoppingList.id !== undefined) {
            this.subscribeToSaveResponse(
                this.shoppingListService.update(this.shoppingList));
        } else {
            this.subscribeToSaveResponse(
                this.shoppingListService.create(this.shoppingList));
        }
    }

    private subscribeToSaveResponse(result: Observable<ShoppingList>) {
        result.subscribe((res: ShoppingList) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: ShoppingList) {
        this.eventManager.broadcast({ name: 'shoppingListListModification', content: 'OK'});
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
    selector: 'jhi-shopping-list-popup',
    template: ''
})
export class ShoppingListPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private shoppingListPopupService: ShoppingListPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.shoppingListPopupService
                    .open(ShoppingListDialogComponent as Component, params['id']);
            } else {
                this.shoppingListPopupService
                    .open(ShoppingListDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
