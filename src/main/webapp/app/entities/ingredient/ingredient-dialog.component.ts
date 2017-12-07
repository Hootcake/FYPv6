import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Ingredient } from './ingredient.model';
import { IngredientPopupService } from './ingredient-popup.service';
import { IngredientService } from './ingredient.service';
import { ShoppingList, ShoppingListService } from '../shopping-list';
import { Inventory, InventoryService } from '../inventory';
import { Recipe, RecipeService } from '../recipe';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-ingredient-dialog',
    templateUrl: './ingredient-dialog.component.html'
})
export class IngredientDialogComponent implements OnInit {

    ingredient: Ingredient;
    isSaving: boolean;

    shoppinglists: ShoppingList[];

    inventories: Inventory[];

    recipes: Recipe[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private ingredientService: IngredientService,
        private shoppingListService: ShoppingListService,
        private inventoryService: InventoryService,
        private recipeService: RecipeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.shoppingListService.query()
            .subscribe((res: ResponseWrapper) => { this.shoppinglists = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.inventoryService.query()
            .subscribe((res: ResponseWrapper) => { this.inventories = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.recipeService.query()
            .subscribe((res: ResponseWrapper) => { this.recipes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.ingredient.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ingredientService.update(this.ingredient));
        } else {
            this.subscribeToSaveResponse(
                this.ingredientService.create(this.ingredient));
        }
    }

    private subscribeToSaveResponse(result: Observable<Ingredient>) {
        result.subscribe((res: Ingredient) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Ingredient) {
        this.eventManager.broadcast({ name: 'ingredientListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackShoppingListById(index: number, item: ShoppingList) {
        return item.id;
    }

    trackInventoryById(index: number, item: Inventory) {
        return item.id;
    }

    trackRecipeById(index: number, item: Recipe) {
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
    selector: 'jhi-ingredient-popup',
    template: ''
})
export class IngredientPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ingredientPopupService: IngredientPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.ingredientPopupService
                    .open(IngredientDialogComponent as Component, params['id']);
            } else {
                this.ingredientPopupService
                    .open(IngredientDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
