import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { FyPv6UserProfileModule } from './user-profile/user-profile.module';
import { FyPv6PostModule } from './post/post.module';
import { FyPv6InventoryModule } from './inventory/inventory.module';
import { FyPv6ShoppingListModule } from './shopping-list/shopping-list.module';
import { FyPv6IngredientModule } from './ingredient/ingredient.module';
import { FyPv6RecipeModule } from './recipe/recipe.module';
import { FyPv6ReviewModule } from './review/review.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        FyPv6UserProfileModule,
        FyPv6PostModule,
        FyPv6InventoryModule,
        FyPv6ShoppingListModule,
        FyPv6IngredientModule,
        FyPv6RecipeModule,
        FyPv6ReviewModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class FyPv6EntityModule {}
