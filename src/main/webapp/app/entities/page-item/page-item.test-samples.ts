import dayjs from 'dayjs/esm';

import { IPageItem, NewPageItem } from './page-item.model';

export const sampleWithRequiredData: IPageItem = {
  id: 27467,
};

export const sampleWithPartialData: IPageItem = {
  id: 31977,
  authors: 'fun',
  publishDate: dayjs('2024-06-20T10:36'),
  publishedBy: 'riffle',
  approvedDate: dayjs('2024-06-20T10:30'),
  jumbotronAltText: 'opposite amazing',
  body: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IPageItem = {
  id: 16172,
  title: 'bandy',
  authors: 'sedately',
  menu: 'oof shore than',
  menuWeight: 'yuck',
  publishDate: dayjs('2024-06-21T05:07'),
  publishedBy: 'what low wherever',
  status: 'DRAFT',
  approvedBy: 'out',
  approvedDate: dayjs('2024-06-20T10:28'),
  jumbotronAltText: 'shakily giddy',
  body: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewPageItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
