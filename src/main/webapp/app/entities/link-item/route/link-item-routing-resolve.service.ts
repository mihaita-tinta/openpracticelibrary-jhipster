import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILinkItem } from '../link-item.model';
import { LinkItemService } from '../service/link-item.service';

const linkItemResolve = (route: ActivatedRouteSnapshot): Observable<null | ILinkItem> => {
  const id = route.params['id'];
  if (id) {
    return inject(LinkItemService)
      .find(id)
      .pipe(
        mergeMap((linkItem: HttpResponse<ILinkItem>) => {
          if (linkItem.body) {
            return of(linkItem.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default linkItemResolve;
