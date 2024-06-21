import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPracticeItem } from '../practice-item.model';
import { PracticeItemService } from '../service/practice-item.service';

const practiceItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IPracticeItem> => {
  const id = route.params['id'];
  if (id) {
    return inject(PracticeItemService)
      .find(id)
      .pipe(
        mergeMap((practiceItem: HttpResponse<IPracticeItem>) => {
          if (practiceItem.body) {
            return of(practiceItem.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default practiceItemResolve;
