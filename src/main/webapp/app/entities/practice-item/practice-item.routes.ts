import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PracticeItemComponent } from './list/practice-item.component';
import { PracticeItemDetailComponent } from './detail/practice-item-detail.component';
import { PracticeItemUpdateComponent } from './update/practice-item-update.component';
import PracticeItemResolve from './route/practice-item-routing-resolve.service';

const practiceItemRoute: Routes = [
  {
    path: '',
    component: PracticeItemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PracticeItemDetailComponent,
    resolve: {
      practiceItem: PracticeItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PracticeItemUpdateComponent,
    resolve: {
      practiceItem: PracticeItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PracticeItemUpdateComponent,
    resolve: {
      practiceItem: PracticeItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default practiceItemRoute;
