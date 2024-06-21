import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBlogItem, NewBlogItem } from '../blog-item.model';

export type PartialUpdateBlogItem = Partial<IBlogItem> & Pick<IBlogItem, 'id'>;

type RestOf<T extends IBlogItem | NewBlogItem> = Omit<T, 'publishDate' | 'approvedDate'> & {
  publishDate?: string | null;
  approvedDate?: string | null;
};

export type RestBlogItem = RestOf<IBlogItem>;

export type NewRestBlogItem = RestOf<NewBlogItem>;

export type PartialUpdateRestBlogItem = RestOf<PartialUpdateBlogItem>;

export type EntityResponseType = HttpResponse<IBlogItem>;
export type EntityArrayResponseType = HttpResponse<IBlogItem[]>;

@Injectable({ providedIn: 'root' })
export class BlogItemService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/blog-items');

  create(blogItem: NewBlogItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blogItem);
    return this.http
      .post<RestBlogItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(blogItem: IBlogItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blogItem);
    return this.http
      .put<RestBlogItem>(`${this.resourceUrl}/${this.getBlogItemIdentifier(blogItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(blogItem: PartialUpdateBlogItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blogItem);
    return this.http
      .patch<RestBlogItem>(`${this.resourceUrl}/${this.getBlogItemIdentifier(blogItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBlogItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBlogItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBlogItemIdentifier(blogItem: Pick<IBlogItem, 'id'>): number {
    return blogItem.id;
  }

  compareBlogItem(o1: Pick<IBlogItem, 'id'> | null, o2: Pick<IBlogItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getBlogItemIdentifier(o1) === this.getBlogItemIdentifier(o2) : o1 === o2;
  }

  addBlogItemToCollectionIfMissing<Type extends Pick<IBlogItem, 'id'>>(
    blogItemCollection: Type[],
    ...blogItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const blogItems: Type[] = blogItemsToCheck.filter(isPresent);
    if (blogItems.length > 0) {
      const blogItemCollectionIdentifiers = blogItemCollection.map(blogItemItem => this.getBlogItemIdentifier(blogItemItem));
      const blogItemsToAdd = blogItems.filter(blogItemItem => {
        const blogItemIdentifier = this.getBlogItemIdentifier(blogItemItem);
        if (blogItemCollectionIdentifiers.includes(blogItemIdentifier)) {
          return false;
        }
        blogItemCollectionIdentifiers.push(blogItemIdentifier);
        return true;
      });
      return [...blogItemsToAdd, ...blogItemCollection];
    }
    return blogItemCollection;
  }

  protected convertDateFromClient<T extends IBlogItem | NewBlogItem | PartialUpdateBlogItem>(blogItem: T): RestOf<T> {
    return {
      ...blogItem,
      publishDate: blogItem.publishDate?.toJSON() ?? null,
      approvedDate: blogItem.approvedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBlogItem: RestBlogItem): IBlogItem {
    return {
      ...restBlogItem,
      publishDate: restBlogItem.publishDate ? dayjs(restBlogItem.publishDate) : undefined,
      approvedDate: restBlogItem.approvedDate ? dayjs(restBlogItem.approvedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBlogItem>): HttpResponse<IBlogItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBlogItem[]>): HttpResponse<IBlogItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
