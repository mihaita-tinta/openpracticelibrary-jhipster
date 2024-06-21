import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MediaAssetComponent } from './list/media-asset.component';
import { MediaAssetDetailComponent } from './detail/media-asset-detail.component';
import { MediaAssetUpdateComponent } from './update/media-asset-update.component';
import MediaAssetResolve from './route/media-asset-routing-resolve.service';

const mediaAssetRoute: Routes = [
  {
    path: '',
    component: MediaAssetComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MediaAssetDetailComponent,
    resolve: {
      mediaAsset: MediaAssetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MediaAssetUpdateComponent,
    resolve: {
      mediaAsset: MediaAssetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MediaAssetUpdateComponent,
    resolve: {
      mediaAsset: MediaAssetResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default mediaAssetRoute;
