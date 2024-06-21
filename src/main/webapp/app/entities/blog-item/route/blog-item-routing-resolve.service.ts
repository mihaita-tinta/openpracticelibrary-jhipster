import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBlogItem } from '../blog-item.model';
import { BlogItemService } from '../service/blog-item.service';

const blogItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IBlogItem> => {
  const id = route.params['id'];
  if (id) {
    return inject(BlogItemService)
      .find(id)
      .pipe(
        mergeMap((blogItem: HttpResponse<IBlogItem>) => {
          if (blogItem.body) {
            return of(blogItem.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default blogItemResolve;
