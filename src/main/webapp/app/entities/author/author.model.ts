import dayjs from 'dayjs/esm';
import { Status } from 'app/entities/enumerations/status.model';

export interface IAuthor {
  id: number;
  name?: string | null;
  githubUsername?: string | null;
  aboutYou?: string | null;
  location?: string | null;
  website?: string | null;
  publishDate?: dayjs.Dayjs | null;
  publishedBy?: string | null;
  status?: keyof typeof Status | null;
  approvedBy?: string | null;
  approvedDate?: dayjs.Dayjs | null;
}

export type NewAuthor = Omit<IAuthor, 'id'> & { id: null };
