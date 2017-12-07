import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { ShoppingList } from './shopping-list.model';
import { ShoppingListService } from './shopping-list.service';

@Component({
    selector: 'jhi-shopping-list-detail',
    templateUrl: './shopping-list-detail.component.html'
})
export class ShoppingListDetailComponent implements OnInit, OnDestroy {

    shoppingList: ShoppingList;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private shoppingListService: ShoppingListService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInShoppingLists();
    }

    load(id) {
        this.shoppingListService.find(id).subscribe((shoppingList) => {
            this.shoppingList = shoppingList;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInShoppingLists() {
        this.eventSubscriber = this.eventManager.subscribe(
            'shoppingListListModification',
            (response) => this.load(this.shoppingList.id)
        );
    }
}
