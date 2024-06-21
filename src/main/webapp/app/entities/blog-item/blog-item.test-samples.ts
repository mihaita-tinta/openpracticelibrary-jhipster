import dayjs from 'dayjs/esm';

import { IBlogItem, NewBlogItem } from './blog-item.model';

export const sampleWithRequiredData: IBlogItem = {
  id: 16916,
};

export const sampleWithPartialData: IBlogItem = {
  id: 22718,
  subtitle: 'guy',
  status: 'PUBLISHED',
  authors: 'old yearly um',
  jumbotronAltText: 'once',
};

export const sampleWithFullData: IBlogItem = {
  id: 12457,
  title: 'witch anti',
  subtitle: 'nor',
  publishDate: dayjs('2024-06-20T15:38'),
  publishedBy: 'peer-to-peer into',
  status: 'READY',
  approvedBy: 'oh meanwhile inasmuch',
  approvedDate: dayjs('2024-06-21T03:25'),
  authors: 'sans old-fashioned likewise',
  jumbotronAltText: 'tenderly phew horsewhip',
  body: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewBlogItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
