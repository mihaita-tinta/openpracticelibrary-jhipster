import { ITag, NewTag } from './tag.model';

export const sampleWithRequiredData: ITag = {
  id: 26397,
};

export const sampleWithPartialData: ITag = {
  id: 2974,
};

export const sampleWithFullData: ITag = {
  id: 11345,
  content: 'chicken',
};

export const sampleWithNewData: NewTag = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
