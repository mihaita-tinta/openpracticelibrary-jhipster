import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 4655,
  login: 'm@u\\)g\\UJ2xG\\ZP5sSZp\\.6',
};

export const sampleWithPartialData: IUser = {
  id: 12832,
  login: 'iVe',
};

export const sampleWithFullData: IUser = {
  id: 14945,
  login: 'J5mD@vUP\\q1OLeW\\5FfRF2g\\_YFw',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
