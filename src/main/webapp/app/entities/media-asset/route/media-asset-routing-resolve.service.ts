import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMediaAsset } from '../media-asset.model';
import { MediaAssetService } from '../service/media-asset.service';

const mediaAssetResolve = (route: ActivatedRouteSnapshot): Observable<null | IMediaAsset> => {
  const id = route.params['id'];
  if (id) {
    return inject(MediaAssetService)
      .find(id)
      .pipe(
        mergeMap((mediaAsset: HttpResponse<IMediaAsset>) => {
          if (mediaAsset.body) {
            return of(mediaAsset.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default mediaAssetResolve;
