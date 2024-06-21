import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PageItemComponent } from './list/page-item.component';
import { PageItemDetailComponent } from './detail/page-item-detail.component';
import { PageItemUpdateComponent } from './update/page-item-update.component';
import PageItemResolve from './route/page-item-routing-resolve.service';

const pageItemRoute: Routes = [
  {
    path: '',
    component: PageItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PageItemDetailComponent,
    resolve: {
      pageItem: PageItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PageItemUpdateComponent,
    resolve: {
      pageItem: PageItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PageItemUpdateComponent,
    resolve: {
      pageItem: PageItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pageItemRoute;
