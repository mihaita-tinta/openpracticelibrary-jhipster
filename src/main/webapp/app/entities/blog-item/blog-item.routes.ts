import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BlogItemComponent } from './list/blog-item.component';
import { BlogItemDetailComponent } from './detail/blog-item-detail.component';
import { BlogItemUpdateComponent } from './update/blog-item-update.component';
import BlogItemResolve from './route/blog-item-routing-resolve.service';

const blogItemRoute: Routes = [
  {
    path: '',
    component: BlogItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BlogItemDetailComponent,
    resolve: {
      blogItem: BlogItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BlogItemUpdateComponent,
    resolve: {
      blogItem: BlogItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BlogItemUpdateComponent,
    resolve: {
      blogItem: BlogItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default blogItemRoute;
