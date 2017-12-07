import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { IngredientComponent } from './ingredient.component';
import { IngredientDetailComponent } from './ingredient-detail.component';
import { IngredientPopupComponent } from './ingredient-dialog.component';
import { IngredientDeletePopupComponent } from './ingredient-delete-dialog.component';

export const ingredientRoute: Routes = [
    {
        path: 'ingredient',
        component: IngredientComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Ingredients'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'ingredient/:id',
        component: IngredientDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Ingredients'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const ingredientPopupRoute: Routes = [
    {
        path: 'ingredient-new',
        component: IngredientPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Ingredients'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ingredient/:id/edit',
        component: IngredientPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Ingredients'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'ingredient/:id/delete',
        component: IngredientDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Ingredients'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
