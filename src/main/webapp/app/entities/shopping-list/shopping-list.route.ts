import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ShoppingListComponent } from './shopping-list.component';
import { ShoppingListDetailComponent } from './shopping-list-detail.component';
import { ShoppingListPopupComponent } from './shopping-list-dialog.component';
import { ShoppingListDeletePopupComponent } from './shopping-list-delete-dialog.component';

export const shoppingListRoute: Routes = [
    {
        path: 'shopping-list',
        component: ShoppingListComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ShoppingLists'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'shopping-list/:id',
        component: ShoppingListDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ShoppingLists'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const shoppingListPopupRoute: Routes = [
    {
        path: 'shopping-list-new',
        component: ShoppingListPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ShoppingLists'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'shopping-list/:id/edit',
        component: ShoppingListPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ShoppingLists'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'shopping-list/:id/delete',
        component: ShoppingListDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ShoppingLists'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
