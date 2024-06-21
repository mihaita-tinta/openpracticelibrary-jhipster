import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPageItem, NewPageItem } from '../page-item.model';

export type PartialUpdatePageItem = Partial<IPageItem> & Pick<IPageItem, 'id'>;

type RestOf<T extends IPageItem | NewPageItem> = Omit<T, 'publishDate' | 'approvedDate'> & {
  publishDate?: string | null;
  approvedDate?: string | null;
};

export type RestPageItem = RestOf<IPageItem>;

export type NewRestPageItem = RestOf<NewPageItem>;

export type PartialUpdateRestPageItem = RestOf<PartialUpdatePageItem>;

export type EntityResponseType = HttpResponse<IPageItem>;
export type EntityArrayResponseType = HttpResponse<IPageItem[]>;

@Injectable({ providedIn: 'root' })
export class PageItemService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/page-items');

  create(pageItem: NewPageItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pageItem);
    return this.http
      .post<RestPageItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pageItem: IPageItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pageItem);
    return this.http
      .put<RestPageItem>(`${this.resourceUrl}/${this.getPageItemIdentifier(pageItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pageItem: PartialUpdatePageItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pageItem);
    return this.http
      .patch<RestPageItem>(`${this.resourceUrl}/${this.getPageItemIdentifier(pageItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPageItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPageItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPageItemIdentifier(pageItem: Pick<IPageItem, 'id'>): number {
    return pageItem.id;
  }

  comparePageItem(o1: Pick<IPageItem, 'id'> | null, o2: Pick<IPageItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getPageItemIdentifier(o1) === this.getPageItemIdentifier(o2) : o1 === o2;
  }

  addPageItemToCollectionIfMissing<Type extends Pick<IPageItem, 'id'>>(
    pageItemCollection: Type[],
    ...pageItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pageItems: Type[] = pageItemsToCheck.filter(isPresent);
    if (pageItems.length > 0) {
      const pageItemCollectionIdentifiers = pageItemCollection.map(pageItemItem => this.getPageItemIdentifier(pageItemItem));
      const pageItemsToAdd = pageItems.filter(pageItemItem => {
        const pageItemIdentifier = this.getPageItemIdentifier(pageItemItem);
        if (pageItemCollectionIdentifiers.includes(pageItemIdentifier)) {
          return false;
        }
        pageItemCollectionIdentifiers.push(pageItemIdentifier);
        return true;
      });
      return [...pageItemsToAdd, ...pageItemCollection];
    }
    return pageItemCollection;
  }

  protected convertDateFromClient<T extends IPageItem | NewPageItem | PartialUpdatePageItem>(pageItem: T): RestOf<T> {
    return {
      ...pageItem,
      publishDate: pageItem.publishDate?.toJSON() ?? null,
      approvedDate: pageItem.approvedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPageItem: RestPageItem): IPageItem {
    return {
      ...restPageItem,
      publishDate: restPageItem.publishDate ? dayjs(restPageItem.publishDate) : undefined,
      approvedDate: restPageItem.approvedDate ? dayjs(restPageItem.approvedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPageItem>): HttpResponse<IPageItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPageItem[]>): HttpResponse<IPageItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
