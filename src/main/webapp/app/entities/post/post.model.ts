import { BaseEntity } from './../../shared';

export class Post implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public content?: string,
        public created_on?: any,
        public userProfile?: BaseEntity,
    ) {
    }
}
