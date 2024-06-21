import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMediaAsset, NewMediaAsset } from '../media-asset.model';

export type PartialUpdateMediaAsset = Partial<IMediaAsset> & Pick<IMediaAsset, 'id'>;

export type EntityResponseType = HttpResponse<IMediaAsset>;
export type EntityArrayResponseType = HttpResponse<IMediaAsset[]>;

@Injectable({ providedIn: 'root' })
export class MediaAssetService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/media-assets');

  create(mediaAsset: NewMediaAsset): Observable<EntityResponseType> {
    return this.http.post<IMediaAsset>(this.resourceUrl, mediaAsset, { observe: 'response' });
  }

  update(mediaAsset: IMediaAsset): Observable<EntityResponseType> {
    return this.http.put<IMediaAsset>(`${this.resourceUrl}/${this.getMediaAssetIdentifier(mediaAsset)}`, mediaAsset, {
      observe: 'response',
    });
  }

  partialUpdate(mediaAsset: PartialUpdateMediaAsset): Observable<EntityResponseType> {
    return this.http.patch<IMediaAsset>(`${this.resourceUrl}/${this.getMediaAssetIdentifier(mediaAsset)}`, mediaAsset, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMediaAsset>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMediaAsset[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMediaAssetIdentifier(mediaAsset: Pick<IMediaAsset, 'id'>): number {
    return mediaAsset.id;
  }

  compareMediaAsset(o1: Pick<IMediaAsset, 'id'> | null, o2: Pick<IMediaAsset, 'id'> | null): boolean {
    return o1 && o2 ? this.getMediaAssetIdentifier(o1) === this.getMediaAssetIdentifier(o2) : o1 === o2;
  }

  addMediaAssetToCollectionIfMissing<Type extends Pick<IMediaAsset, 'id'>>(
    mediaAssetCollection: Type[],
    ...mediaAssetsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mediaAssets: Type[] = mediaAssetsToCheck.filter(isPresent);
    if (mediaAssets.length > 0) {
      const mediaAssetCollectionIdentifiers = mediaAssetCollection.map(mediaAssetItem => this.getMediaAssetIdentifier(mediaAssetItem));
      const mediaAssetsToAdd = mediaAssets.filter(mediaAssetItem => {
        const mediaAssetIdentifier = this.getMediaAssetIdentifier(mediaAssetItem);
        if (mediaAssetCollectionIdentifiers.includes(mediaAssetIdentifier)) {
          return false;
        }
        mediaAssetCollectionIdentifiers.push(mediaAssetIdentifier);
        return true;
      });
      return [...mediaAssetsToAdd, ...mediaAssetCollection];
    }
    return mediaAssetCollection;
  }
}
