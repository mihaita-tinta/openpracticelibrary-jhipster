import { IPracticeItem } from 'app/entities/practice-item/practice-item.model';

export interface ILinkItem {
  id: number;
  url?: string | null;
  sortIndex?: number | null;
  practiceItem?: Pick<IPracticeItem, 'id'> | null;
}

export type NewLinkItem = Omit<ILinkItem, 'id'> & { id: null };
