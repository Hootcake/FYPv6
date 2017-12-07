import { BaseEntity, User } from './../../shared';

export class Recipe implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public time?: string,
        public prep?: string,
        public method?: string,
        public notes?: string,
        public created_by?: User,
        public favorited_by?: User,
        public ingredients?: BaseEntity[],
    ) {
    }
}
