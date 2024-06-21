import { IMediaAsset, NewMediaAsset } from './media-asset.model';

export const sampleWithRequiredData: IMediaAsset = {
  id: 15147,
};

export const sampleWithPartialData: IMediaAsset = {
  id: 18076,
  type: 'IMAGE',
};

export const sampleWithFullData: IMediaAsset = {
  id: 27608,
  type: 'IMAGE',
  content: '../fake-data/blob/hipster.png',
  contentContentType: 'unknown',
  sortIndex: 3919,
};

export const sampleWithNewData: NewMediaAsset = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
