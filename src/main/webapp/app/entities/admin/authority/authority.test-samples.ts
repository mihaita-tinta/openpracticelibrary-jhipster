import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '8ddd943c-77ed-4c8c-b002-c2b1329f0bd9',
};

export const sampleWithPartialData: IAuthority = {
  name: '711a0461-ab89-418b-9ae4-4517c3c18955',
};

export const sampleWithFullData: IAuthority = {
  name: 'fa9cb9d5-f696-4fac-a9d8-0b2fcd9a3b12',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
