import { ILinkItem, NewLinkItem } from './link-item.model';

export const sampleWithRequiredData: ILinkItem = {
  id: 2224,
};

export const sampleWithPartialData: ILinkItem = {
  id: 30361,
};

export const sampleWithFullData: ILinkItem = {
  id: 15992,
  url: 'https://attentive-clave.org/',
  sortIndex: 23704,
};

export const sampleWithNewData: NewLinkItem = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
