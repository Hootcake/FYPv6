import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Ingredient } from './ingredient.model';
import { IngredientService } from './ingredient.service';

@Component({
    selector: 'jhi-ingredient-detail',
    templateUrl: './ingredient-detail.component.html'
})
export class IngredientDetailComponent implements OnInit, OnDestroy {

    ingredient: Ingredient;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private ingredientService: IngredientService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInIngredients();
    }

    load(id) {
        this.ingredientService.find(id).subscribe((ingredient) => {
            this.ingredient = ingredient;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInIngredients() {
        this.eventSubscriber = this.eventManager.subscribe(
            'ingredientListModification',
            (response) => this.load(this.ingredient.id)
        );
    }
}
