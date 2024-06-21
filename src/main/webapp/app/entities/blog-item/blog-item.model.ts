import dayjs from 'dayjs/esm';
import { IMediaAsset } from 'app/entities/media-asset/media-asset.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IBlogItem {
  id: number;
  title?: string | null;
  subtitle?: string | null;
  publishDate?: dayjs.Dayjs | null;
  publishedBy?: string | null;
  status?: keyof typeof Status | null;
  approvedBy?: string | null;
  approvedDate?: dayjs.Dayjs | null;
  authors?: string | null;
  jumbotronAltText?: string | null;
  body?: string | null;
  jumbotronImage?: Pick<IMediaAsset, 'id'> | null;
}

export type NewBlogItem = Omit<IBlogItem, 'id'> & { id: null };
