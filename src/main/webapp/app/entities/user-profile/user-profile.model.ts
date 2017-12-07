import { BaseEntity, User } from './../../shared';

export class UserProfile implements BaseEntity {
    constructor(
        public id?: number,
        public description?: string,
        public user?: User,
    ) {
    }
}
