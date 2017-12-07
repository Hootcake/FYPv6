import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Recipe } from './recipe.model';
import { RecipePopupService } from './recipe-popup.service';
import { RecipeService } from './recipe.service';
import { User, UserService } from '../../shared';
import { Ingredient, IngredientService } from '../ingredient';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-recipe-dialog',
    templateUrl: './recipe-dialog.component.html'
})
export class RecipeDialogComponent implements OnInit {

    recipe: Recipe;
    isSaving: boolean;

    users: User[];

    ingredients: Ingredient[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private recipeService: RecipeService,
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
        if (this.recipe.id !== undefined) {
            this.subscribeToSaveResponse(
                this.recipeService.update(this.recipe));
        } else {
            this.subscribeToSaveResponse(
                this.recipeService.create(this.recipe));
        }
    }

    private subscribeToSaveResponse(result: Observable<Recipe>) {
        result.subscribe((res: Recipe) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Recipe) {
        this.eventManager.broadcast({ name: 'recipeListModification', content: 'OK'});
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
    selector: 'jhi-recipe-popup',
    template: ''
})
export class RecipePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private recipePopupService: RecipePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.recipePopupService
                    .open(RecipeDialogComponent as Component, params['id']);
            } else {
                this.recipePopupService
                    .open(RecipeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
