import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { LinkItemComponent } from './list/link-item.component';
import { LinkItemDetailComponent } from './detail/link-item-detail.component';
import { LinkItemUpdateComponent } from './update/link-item-update.component';
import LinkItemResolve from './route/link-item-routing-resolve.service';

const linkItemRoute: Routes = [
  {
    path: '',
    component: LinkItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LinkItemDetailComponent,
    resolve: {
      linkItem: LinkItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LinkItemUpdateComponent,
    resolve: {
      linkItem: LinkItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LinkItemUpdateComponent,
    resolve: {
      linkItem: LinkItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default linkItemRoute;
