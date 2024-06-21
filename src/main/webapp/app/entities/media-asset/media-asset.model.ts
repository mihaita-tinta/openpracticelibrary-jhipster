import { IPracticeItem } from 'app/entities/practice-item/practice-item.model';
import { MediaAssetType } from 'app/entities/enumerations/media-asset-type.model';

export interface IMediaAsset {
  id: number;
  type?: keyof typeof MediaAssetType | null;
  content?: string | null;
  contentContentType?: string | null;
  sortIndex?: number | null;
  practices?: Pick<IPracticeItem, 'id'> | null;
}

export type NewMediaAsset = Omit<IMediaAsset, 'id'> & { id: null };
