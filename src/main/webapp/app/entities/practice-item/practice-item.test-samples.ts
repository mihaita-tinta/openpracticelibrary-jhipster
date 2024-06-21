import dayjs from 'dayjs/esm';

import { IPracticeItem, NewPracticeItem } from './practice-item.model';

export const sampleWithRequiredData: IPracticeItem = {
  id: 10614,
};

export const sampleWithPartialData: IPracticeItem = {
  id: 8491,
  title: 'lest knottily scourge',
  approvedDate: dayjs('2024-06-20T16:57'),
  facilitationDifficulty: 'MODERATE',
  what: '../fake-data/blob/hipster.txt',
  expectedParticipants: 'readily um novel',
};

export const sampleWithFullData: IPracticeItem = {
  id: 27981,
  title: 'although total aha',
  objective: 'repeat aha whether',
  publishDate: dayjs('2024-06-21T09:37'),
  publishedBy: 'apud intensely',
  status: 'READY',
  approvedBy: 'except infamous calculus',
  approvedDate: dayjs('2024-06-20T18:10'),
  authors: 'yippee hence myth',
  facilitationDifficulty: 'EASY',
  mobiusLoopTag: 'DISCOVERY',
  what: '../fake-data/blob/hipster.txt',
  why: '../fake-data/blob/hipster.txt',
  how: '../fake-data/blob/hipster.txt',
  numberOfPeopleRequired: 'kind risk',
  timeLength: 'concerning while',
  expectedParticipants: 'immense devolve given',
};

export const sampleWithNewData: NewPracticeItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
