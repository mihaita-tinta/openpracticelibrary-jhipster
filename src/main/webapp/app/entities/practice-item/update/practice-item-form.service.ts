import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPracticeItem, NewPracticeItem } from '../practice-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPracticeItem for edit and NewPracticeItemFormGroupInput for create.
 */
type PracticeItemFormGroupInput = IPracticeItem | PartialWithRequiredKeyOf<NewPracticeItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPracticeItem | NewPracticeItem> = Omit<T, 'publishDate' | 'approvedDate'> & {
  publishDate?: string | null;
  approvedDate?: string | null;
};

type PracticeItemFormRawValue = FormValueOf<IPracticeItem>;

type NewPracticeItemFormRawValue = FormValueOf<NewPracticeItem>;

type PracticeItemFormDefaults = Pick<NewPracticeItem, 'id' | 'publishDate' | 'approvedDate'>;

type PracticeItemFormGroupContent = {
  id: FormControl<PracticeItemFormRawValue['id'] | NewPracticeItem['id']>;
  title: FormControl<PracticeItemFormRawValue['title']>;
  objective: FormControl<PracticeItemFormRawValue['objective']>;
  publishDate: FormControl<PracticeItemFormRawValue['publishDate']>;
  publishedBy: FormControl<PracticeItemFormRawValue['publishedBy']>;
  status: FormControl<PracticeItemFormRawValue['status']>;
  approvedBy: FormControl<PracticeItemFormRawValue['approvedBy']>;
  approvedDate: FormControl<PracticeItemFormRawValue['approvedDate']>;
  authors: FormControl<PracticeItemFormRawValue['authors']>;
  facilitationDifficulty: FormControl<PracticeItemFormRawValue['facilitationDifficulty']>;
  mobiusLoopTag: FormControl<PracticeItemFormRawValue['mobiusLoopTag']>;
  what: FormControl<PracticeItemFormRawValue['what']>;
  why: FormControl<PracticeItemFormRawValue['why']>;
  how: FormControl<PracticeItemFormRawValue['how']>;
  numberOfPeopleRequired: FormControl<PracticeItemFormRawValue['numberOfPeopleRequired']>;
  timeLength: FormControl<PracticeItemFormRawValue['timeLength']>;
  expectedParticipants: FormControl<PracticeItemFormRawValue['expectedParticipants']>;
  coverImage: FormControl<PracticeItemFormRawValue['coverImage']>;
};

export type PracticeItemFormGroup = FormGroup<PracticeItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PracticeItemFormService {
  createPracticeItemFormGroup(practiceItem: PracticeItemFormGroupInput = { id: null }): PracticeItemFormGroup {
    const practiceItemRawValue = this.convertPracticeItemToPracticeItemRawValue({
      ...this.getFormDefaults(),
      ...practiceItem,
    });
    return new FormGroup<PracticeItemFormGroupContent>({
      id: new FormControl(
        { value: practiceItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(practiceItemRawValue.title),
      objective: new FormControl(practiceItemRawValue.objective),
      publishDate: new FormControl(practiceItemRawValue.publishDate),
      publishedBy: new FormControl(practiceItemRawValue.publishedBy),
      status: new FormControl(practiceItemRawValue.status),
      approvedBy: new FormControl(practiceItemRawValue.approvedBy),
      approvedDate: new FormControl(practiceItemRawValue.approvedDate),
      authors: new FormControl(practiceItemRawValue.authors),
      facilitationDifficulty: new FormControl(practiceItemRawValue.facilitationDifficulty),
      mobiusLoopTag: new FormControl(practiceItemRawValue.mobiusLoopTag),
      what: new FormControl(practiceItemRawValue.what),
      why: new FormControl(practiceItemRawValue.why),
      how: new FormControl(practiceItemRawValue.how),
      numberOfPeopleRequired: new FormControl(practiceItemRawValue.numberOfPeopleRequired),
      timeLength: new FormControl(practiceItemRawValue.timeLength),
      expectedParticipants: new FormControl(practiceItemRawValue.expectedParticipants),
      coverImage: new FormControl(practiceItemRawValue.coverImage),
    });
  }

  getPracticeItem(form: PracticeItemFormGroup): IPracticeItem | NewPracticeItem {
    return this.convertPracticeItemRawValueToPracticeItem(form.getRawValue() as PracticeItemFormRawValue | NewPracticeItemFormRawValue);
  }

  resetForm(form: PracticeItemFormGroup, practiceItem: PracticeItemFormGroupInput): void {
    const practiceItemRawValue = this.convertPracticeItemToPracticeItemRawValue({ ...this.getFormDefaults(), ...practiceItem });
    form.reset(
      {
        ...practiceItemRawValue,
        id: { value: practiceItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PracticeItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      publishDate: currentTime,
      approvedDate: currentTime,
    };
  }

  private convertPracticeItemRawValueToPracticeItem(
    rawPracticeItem: PracticeItemFormRawValue | NewPracticeItemFormRawValue,
  ): IPracticeItem | NewPracticeItem {
    return {
      ...rawPracticeItem,
      publishDate: dayjs(rawPracticeItem.publishDate, DATE_TIME_FORMAT),
      approvedDate: dayjs(rawPracticeItem.approvedDate, DATE_TIME_FORMAT),
    };
  }

  private convertPracticeItemToPracticeItemRawValue(
    practiceItem: IPracticeItem | (Partial<NewPracticeItem> & PracticeItemFormDefaults),
  ): PracticeItemFormRawValue | PartialWithRequiredKeyOf<NewPracticeItemFormRawValue> {
    return {
      ...practiceItem,
      publishDate: practiceItem.publishDate ? practiceItem.publishDate.format(DATE_TIME_FORMAT) : undefined,
      approvedDate: practiceItem.approvedDate ? practiceItem.approvedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
