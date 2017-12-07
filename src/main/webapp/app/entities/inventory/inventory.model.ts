import { BaseEntity, User } from './../../shared';

export class Inventory implements BaseEntity {
    constructor(
        public id?: number,
        public user?: User,
        public ingredients?: BaseEntity[],
    ) {
    }
}
