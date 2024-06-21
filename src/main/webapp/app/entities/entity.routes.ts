import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'openPracticeLibraryApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'practice-item',
    data: { pageTitle: 'openPracticeLibraryApp.practiceItem.home.title' },
    loadChildren: () => import('./practice-item/practice-item.routes'),
  },
  {
    path: 'blog-item',
    data: { pageTitle: 'openPracticeLibraryApp.blogItem.home.title' },
    loadChildren: () => import('./blog-item/blog-item.routes'),
  },
  {
    path: 'page-item',
    data: { pageTitle: 'openPracticeLibraryApp.pageItem.home.title' },
    loadChildren: () => import('./page-item/page-item.routes'),
  },
  {
    path: 'author',
    data: { pageTitle: 'openPracticeLibraryApp.author.home.title' },
    loadChildren: () => import('./author/author.routes'),
  },
  {
    path: 'tag',
    data: { pageTitle: 'openPracticeLibraryApp.tag.home.title' },
    loadChildren: () => import('./tag/tag.routes'),
  },
  {
    path: 'media-asset',
    data: { pageTitle: 'openPracticeLibraryApp.mediaAsset.home.title' },
    loadChildren: () => import('./media-asset/media-asset.routes'),
  },
  {
    path: 'link-item',
    data: { pageTitle: 'openPracticeLibraryApp.linkItem.home.title' },
    loadChildren: () => import('./link-item/link-item.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
