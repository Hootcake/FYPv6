import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Ingredient } from './ingredient.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class IngredientService {

    private resourceUrl = SERVER_API_URL + 'api/ingredients';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/ingredients';

    constructor(private http: Http) { }

    create(ingredient: Ingredient): Observable<Ingredient> {
        const copy = this.convert(ingredient);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(ingredient: Ingredient): Observable<Ingredient> {
        const copy = this.convert(ingredient);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Ingredient> {
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
     * Convert a returned JSON object to Ingredient.
     */
    private convertItemFromServer(json: any): Ingredient {
        const entity: Ingredient = Object.assign(new Ingredient(), json);
        return entity;
    }

    /**
     * Convert a Ingredient to a JSON which can be sent to the server.
     */
    private convert(ingredient: Ingredient): Ingredient {
        const copy: Ingredient = Object.assign({}, ingredient);
        return copy;
    }
}
