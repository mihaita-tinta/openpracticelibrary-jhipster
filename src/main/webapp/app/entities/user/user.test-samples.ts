import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 31946,
  login: 'O+@j9b\\"ke\\?Rbd3h',
};

export const sampleWithPartialData: IUser = {
  id: 7294,
  login: '!9U@jOP7r\\aYpKjr\\V5C4M\\Ic',
};

export const sampleWithFullData: IUser = {
  id: 28891,
  login: 'I',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
