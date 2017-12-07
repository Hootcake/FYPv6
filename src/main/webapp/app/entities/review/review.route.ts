import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ReviewComponent } from './review.component';
import { ReviewDetailComponent } from './review-detail.component';
import { ReviewPopupComponent } from './review-dialog.component';
import { ReviewDeletePopupComponent } from './review-delete-dialog.component';

export const reviewRoute: Routes = [
    {
        path: 'review',
        component: ReviewComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Reviews'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'review/:id',
        component: ReviewDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Reviews'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reviewPopupRoute: Routes = [
    {
        path: 'review-new',
        component: ReviewPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Reviews'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'review/:id/edit',
        component: ReviewPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Reviews'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'review/:id/delete',
        component: ReviewDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Reviews'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
