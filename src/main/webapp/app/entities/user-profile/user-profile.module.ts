import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FyPv6SharedModule } from '../../shared';
import { FyPv6AdminModule } from '../../admin/admin.module';
import {
    UserProfileService,
    UserProfilePopupService,
    UserProfileComponent,
    UserProfileDetailComponent,
    UserProfileDialogComponent,
    UserProfilePopupComponent,
    UserProfileDeletePopupComponent,
    UserProfileDeleteDialogComponent,
    userProfileRoute,
    userProfilePopupRoute,
} from './';

const ENTITY_STATES = [
    ...userProfileRoute,
    ...userProfilePopupRoute,
];

@NgModule({
    imports: [
        FyPv6SharedModule,
        FyPv6AdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        UserProfileComponent,
        UserProfileDetailComponent,
        UserProfileDialogComponent,
        UserProfileDeleteDialogComponent,
        UserProfilePopupComponent,
        UserProfileDeletePopupComponent,
    ],
    entryComponents: [
        UserProfileComponent,
        UserProfileDialogComponent,
        UserProfilePopupComponent,
        UserProfileDeleteDialogComponent,
        UserProfileDeletePopupComponent,
    ],
    providers: [
        UserProfileService,
        UserProfilePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FyPv6UserProfileModule {}
