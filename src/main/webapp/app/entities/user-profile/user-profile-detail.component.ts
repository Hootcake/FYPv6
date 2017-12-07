import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { UserProfile } from './user-profile.model';
import { UserProfileService } from './user-profile.service';

@Component({
    selector: 'jhi-user-profile-detail',
    templateUrl: './user-profile-detail.component.html'
})
export class UserProfileDetailComponent implements OnInit, OnDestroy {

    userProfile: UserProfile;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private userProfileService: UserProfileService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInUserProfiles();
    }

    load(id) {
        this.userProfileService.find(id).subscribe((userProfile) => {
            this.userProfile = userProfile;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUserProfiles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'userProfileListModification',
            (response) => this.load(this.userProfile.id)
        );
    }
}
