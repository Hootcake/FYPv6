import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FyPv6SharedModule } from '../../shared';
import { FyPv6AdminModule } from '../../admin/admin.module';
import {
    ShoppingListService,
    ShoppingListPopupService,
    ShoppingListComponent,
    ShoppingListDetailComponent,
    ShoppingListDialogComponent,
    ShoppingListPopupComponent,
    ShoppingListDeletePopupComponent,
    ShoppingListDeleteDialogComponent,
    shoppingListRoute,
    shoppingListPopupRoute,
} from './';

const ENTITY_STATES = [
    ...shoppingListRoute,
    ...shoppingListPopupRoute,
];

@NgModule({
    imports: [
        FyPv6SharedModule,
        FyPv6AdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ShoppingListComponent,
        ShoppingListDetailComponent,
        ShoppingListDialogComponent,
        ShoppingListDeleteDialogComponent,
        ShoppingListPopupComponent,
        ShoppingListDeletePopupComponent,
    ],
    entryComponents: [
        ShoppingListComponent,
        ShoppingListDialogComponent,
        ShoppingListPopupComponent,
        ShoppingListDeleteDialogComponent,
        ShoppingListDeletePopupComponent,
    ],
    providers: [
        ShoppingListService,
        ShoppingListPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FyPv6ShoppingListModule {}
