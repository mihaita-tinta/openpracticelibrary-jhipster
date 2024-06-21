import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '0ffc4775-230e-4030-83a2-52aa339bfaea',
};

export const sampleWithPartialData: IAuthority = {
  name: '245a9fb9-1c2d-419f-a5c5-cf6e3f9febb0',
};

export const sampleWithFullData: IAuthority = {
  name: 'dffe6887-eb5e-4612-bec2-097804745f2f',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
