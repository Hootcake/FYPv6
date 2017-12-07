import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { UserProfile } from './user-profile.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class UserProfileService {

    private resourceUrl = SERVER_API_URL + 'api/user-profiles';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/user-profiles';

    constructor(private http: Http) { }

    create(userProfile: UserProfile): Observable<UserProfile> {
        const copy = this.convert(userProfile);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(userProfile: UserProfile): Observable<UserProfile> {
        const copy = this.convert(userProfile);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<UserProfile> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to UserProfile.
     */
    private convertItemFromServer(json: any): UserProfile {
        const entity: UserProfile = Object.assign(new UserProfile(), json);
        return entity;
    }

    /**
     * Convert a UserProfile to a JSON which can be sent to the server.
     */
    private convert(userProfile: UserProfile): UserProfile {
        const copy: UserProfile = Object.assign({}, userProfile);
        return copy;
    }
}
