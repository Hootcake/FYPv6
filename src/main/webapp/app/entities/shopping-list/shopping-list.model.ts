import { BaseEntity, User } from './../../shared';

export class ShoppingList implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public notes?: string,
        public user?: User,
        public ingredients?: BaseEntity[],
    ) {
    }
}
