import dayjs from 'dayjs/esm';
import { IMediaAsset } from 'app/entities/media-asset/media-asset.model';
import { Status } from 'app/entities/enumerations/status.model';
import { FacilitationDifficulty } from 'app/entities/enumerations/facilitation-difficulty.model';
import { MobiusLoopTag } from 'app/entities/enumerations/mobius-loop-tag.model';

export interface IPracticeItem {
  id: number;
  title?: string | null;
  objective?: string | null;
  publishDate?: dayjs.Dayjs | null;
  publishedBy?: string | null;
  status?: keyof typeof Status | null;
  approvedBy?: string | null;
  approvedDate?: dayjs.Dayjs | null;
  authors?: string | null;
  facilitationDifficulty?: keyof typeof FacilitationDifficulty | null;
  mobiusLoopTag?: keyof typeof MobiusLoopTag | null;
  what?: string | null;
  why?: string | null;
  how?: string | null;
  numberOfPeopleRequired?: string | null;
  timeLength?: string | null;
  expectedParticipants?: string | null;
  coverImage?: Pick<IMediaAsset, 'id'> | null;
}

export type NewPracticeItem = Omit<IPracticeItem, 'id'> & { id: null };
