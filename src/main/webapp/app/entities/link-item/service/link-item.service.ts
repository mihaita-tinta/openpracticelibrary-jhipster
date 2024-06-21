import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILinkItem, NewLinkItem } from '../link-item.model';

export type PartialUpdateLinkItem = Partial<ILinkItem> & Pick<ILinkItem, 'id'>;

export type EntityResponseType = HttpResponse<ILinkItem>;
export type EntityArrayResponseType = HttpResponse<ILinkItem[]>;

@Injectable({ providedIn: 'root' })
export class LinkItemService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/link-items');

  create(linkItem: NewLinkItem): Observable<EntityResponseType> {
    return this.http.post<ILinkItem>(this.resourceUrl, linkItem, { observe: 'response' });
  }

  update(linkItem: ILinkItem): Observable<EntityResponseType> {
    return this.http.put<ILinkItem>(`${this.resourceUrl}/${this.getLinkItemIdentifier(linkItem)}`, linkItem, { observe: 'response' });
  }

  partialUpdate(linkItem: PartialUpdateLinkItem): Observable<EntityResponseType> {
    return this.http.patch<ILinkItem>(`${this.resourceUrl}/${this.getLinkItemIdentifier(linkItem)}`, linkItem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILinkItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILinkItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLinkItemIdentifier(linkItem: Pick<ILinkItem, 'id'>): number {
    return linkItem.id;
  }

  compareLinkItem(o1: Pick<ILinkItem, 'id'> | null, o2: Pick<ILinkItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getLinkItemIdentifier(o1) === this.getLinkItemIdentifier(o2) : o1 === o2;
  }

  addLinkItemToCollectionIfMissing<Type extends Pick<ILinkItem, 'id'>>(
    linkItemCollection: Type[],
    ...linkItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const linkItems: Type[] = linkItemsToCheck.filter(isPresent);
    if (linkItems.length > 0) {
      const linkItemCollectionIdentifiers = linkItemCollection.map(linkItemItem => this.getLinkItemIdentifier(linkItemItem));
      const linkItemsToAdd = linkItems.filter(linkItemItem => {
        const linkItemIdentifier = this.getLinkItemIdentifier(linkItemItem);
        if (linkItemCollectionIdentifiers.includes(linkItemIdentifier)) {
          return false;
        }
        linkItemCollectionIdentifiers.push(linkItemIdentifier);
        return true;
      });
      return [...linkItemsToAdd, ...linkItemCollection];
    }
    return linkItemCollection;
  }
}
