import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Review } from './review.model';
import { ReviewPopupService } from './review-popup.service';
import { ReviewService } from './review.service';
import { User, UserService } from '../../shared';
import { Recipe, RecipeService } from '../recipe';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-review-dialog',
    templateUrl: './review-dialog.component.html'
})
export class ReviewDialogComponent implements OnInit {

    review: Review;
    isSaving: boolean;

    users: User[];

    recipes: Recipe[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private reviewService: ReviewService,
        private userService: UserService,
        private recipeService: RecipeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.recipeService.query()
            .subscribe((res: ResponseWrapper) => { this.recipes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.review.id !== undefined) {
            this.subscribeToSaveResponse(
                this.reviewService.update(this.review));
        } else {
            this.subscribeToSaveResponse(
                this.reviewService.create(this.review));
        }
    }

    private subscribeToSaveResponse(result: Observable<Review>) {
        result.subscribe((res: Review) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: Review) {
        this.eventManager.broadcast({ name: 'reviewListModification', content: 'OK'});
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

    trackRecipeById(index: number, item: Recipe) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-review-popup',
    template: ''
})
export class ReviewPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reviewPopupService: ReviewPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.reviewPopupService
                    .open(ReviewDialogComponent as Component, params['id']);
            } else {
                this.reviewPopupService
                    .open(ReviewDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
