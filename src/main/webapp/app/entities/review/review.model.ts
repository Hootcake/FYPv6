import { BaseEntity, User } from './../../shared';

export class Review implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public rating?: number,
        public message?: string,
        public reviewed_by?: User,
        public recipe?: BaseEntity,
    ) {
    }
}
