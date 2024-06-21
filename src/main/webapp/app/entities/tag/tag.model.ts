import { IPracticeItem } from 'app/entities/practice-item/practice-item.model';

export interface ITag {
  id: number;
  content?: string | null;
  practiceItem?: Pick<IPracticeItem, 'id'> | null;
}

export type NewTag = Omit<ITag, 'id'> & { id: null };
