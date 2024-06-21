import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPracticeItem, NewPracticeItem } from '../practice-item.model';

export type PartialUpdatePracticeItem = Partial<IPracticeItem> & Pick<IPracticeItem, 'id'>;

type RestOf<T extends IPracticeItem | NewPracticeItem> = Omit<T, 'publishDate' | 'approvedDate'> & {
  publishDate?: string | null;
  approvedDate?: string | null;
};

export type RestPracticeItem = RestOf<IPracticeItem>;

export type NewRestPracticeItem = RestOf<NewPracticeItem>;

export type PartialUpdateRestPracticeItem = RestOf<PartialUpdatePracticeItem>;

export type EntityResponseType = HttpResponse<IPracticeItem>;
export type EntityArrayResponseType = HttpResponse<IPracticeItem[]>;

@Injectable({ providedIn: 'root' })
export class PracticeItemService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/practice-items');

  create(practiceItem: NewPracticeItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(practiceItem);
    return this.http
      .post<RestPracticeItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(practiceItem: IPracticeItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(practiceItem);
    return this.http
      .put<RestPracticeItem>(`${this.resourceUrl}/${this.getPracticeItemIdentifier(practiceItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(practiceItem: PartialUpdatePracticeItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(practiceItem);
    return this.http
      .patch<RestPracticeItem>(`${this.resourceUrl}/${this.getPracticeItemIdentifier(practiceItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPracticeItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPracticeItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPracticeItemIdentifier(practiceItem: Pick<IPracticeItem, 'id'>): number {
    return practiceItem.id;
  }

  comparePracticeItem(o1: Pick<IPracticeItem, 'id'> | null, o2: Pick<IPracticeItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getPracticeItemIdentifier(o1) === this.getPracticeItemIdentifier(o2) : o1 === o2;
  }

  addPracticeItemToCollectionIfMissing<Type extends Pick<IPracticeItem, 'id'>>(
    practiceItemCollection: Type[],
    ...practiceItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const practiceItems: Type[] = practiceItemsToCheck.filter(isPresent);
    if (practiceItems.length > 0) {
      const practiceItemCollectionIdentifiers = practiceItemCollection.map(practiceItemItem =>
        this.getPracticeItemIdentifier(practiceItemItem),
      );
      const practiceItemsToAdd = practiceItems.filter(practiceItemItem => {
        const practiceItemIdentifier = this.getPracticeItemIdentifier(practiceItemItem);
        if (practiceItemCollectionIdentifiers.includes(practiceItemIdentifier)) {
          return false;
        }
        practiceItemCollectionIdentifiers.push(practiceItemIdentifier);
        return true;
      });
      return [...practiceItemsToAdd, ...practiceItemCollection];
    }
    return practiceItemCollection;
  }

  protected convertDateFromClient<T extends IPracticeItem | NewPracticeItem | PartialUpdatePracticeItem>(practiceItem: T): RestOf<T> {
    return {
      ...practiceItem,
      publishDate: practiceItem.publishDate?.toJSON() ?? null,
      approvedDate: practiceItem.approvedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPracticeItem: RestPracticeItem): IPracticeItem {
    return {
      ...restPracticeItem,
      publishDate: restPracticeItem.publishDate ? dayjs(restPracticeItem.publishDate) : undefined,
      approvedDate: restPracticeItem.approvedDate ? dayjs(restPracticeItem.approvedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPracticeItem>): HttpResponse<IPracticeItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPracticeItem[]>): HttpResponse<IPracticeItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
