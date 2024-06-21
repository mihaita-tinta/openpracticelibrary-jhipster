import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPageItem } from '../page-item.model';
import { PageItemService } from '../service/page-item.service';

const pageItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IPageItem> => {
  const id = route.params['id'];
  if (id) {
    return inject(PageItemService)
      .find(id)
      .pipe(
        mergeMap((pageItem: HttpResponse<IPageItem>) => {
          if (pageItem.body) {
            return of(pageItem.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default pageItemResolve;
