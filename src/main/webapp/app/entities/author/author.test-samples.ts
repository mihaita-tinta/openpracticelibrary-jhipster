import dayjs from 'dayjs/esm';

import { IAuthor, NewAuthor } from './author.model';

export const sampleWithRequiredData: IAuthor = {
  id: 17346,
};

export const sampleWithPartialData: IAuthor = {
  id: 21988,
  name: 'beautifully apropos',
  githubUsername: 'spill besides',
  website: 'finally',
  publishDate: dayjs('2024-06-20T16:01'),
  publishedBy: 'ouch',
  approvedBy: 'apud brr unscramble',
};

export const sampleWithFullData: IAuthor = {
  id: 25634,
  name: 'uh-huh upbeat',
  githubUsername: 'although hard-to-find sedately',
  aboutYou: '../fake-data/blob/hipster.txt',
  location: 'less gobble',
  website: 'stage swindle beside',
  publishDate: dayjs('2024-06-21T02:25'),
  publishedBy: 'acidly sojourn',
  status: 'READY',
  approvedBy: 'loyally',
  approvedDate: dayjs('2024-06-20T23:40'),
};

export const sampleWithNewData: NewAuthor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
