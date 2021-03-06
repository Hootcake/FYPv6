import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Review } from './review.model';
import { ReviewPopupService } from './review-popup.service';
import { ReviewService } from './review.service';

@Component({
    selector: 'jhi-review-delete-dialog',
    templateUrl: './review-delete-dialog.component.html'
})
export class ReviewDeleteDialogComponent {

    review: Review;

    constructor(
        private reviewService: ReviewService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reviewService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'reviewListModification',
                content: 'Deleted an review'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-review-delete-popup',
    template: ''
})
export class ReviewDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private reviewPopupService: ReviewPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.reviewPopupService
                .open(ReviewDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
