import dayjs from 'dayjs/esm';
import { IMediaAsset } from 'app/entities/media-asset/media-asset.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IPageItem {
  id: number;
  title?: string | null;
  authors?: string | null;
  menu?: string | null;
  menuWeight?: string | null;
  publishDate?: dayjs.Dayjs | null;
  publishedBy?: string | null;
  status?: keyof typeof Status | null;
  approvedBy?: string | null;
  approvedDate?: dayjs.Dayjs | null;
  jumbotronAltText?: string | null;
  body?: string | null;
  jumbotronImage?: Pick<IMediaAsset, 'id'> | null;
}

export type NewPageItem = Omit<IPageItem, 'id'> & { id: null };
