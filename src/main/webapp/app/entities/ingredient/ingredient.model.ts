import { BaseEntity } from './../../shared';

export class Ingredient implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public unit_of_measure?: string,
        public quantity?: number,
        public weight?: number,
        public shoppingLists?: BaseEntity[],
        public inventories?: BaseEntity[],
        public recipes?: BaseEntity[],
    ) {
    }
}
