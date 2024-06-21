import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAuthor, NewAuthor } from '../author.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAuthor for edit and NewAuthorFormGroupInput for create.
 */
type AuthorFormGroupInput = IAuthor | PartialWithRequiredKeyOf<NewAuthor>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAuthor | NewAuthor> = Omit<T, 'publishDate' | 'approvedDate'> & {
  publishDate?: string | null;
  approvedDate?: string | null;
};

type AuthorFormRawValue = FormValueOf<IAuthor>;

type NewAuthorFormRawValue = FormValueOf<NewAuthor>;

type AuthorFormDefaults = Pick<NewAuthor, 'id' | 'publishDate' | 'approvedDate'>;

type AuthorFormGroupContent = {
  id: FormControl<AuthorFormRawValue['id'] | NewAuthor['id']>;
  name: FormControl<AuthorFormRawValue['name']>;
  githubUsername: FormControl<AuthorFormRawValue['githubUsername']>;
  aboutYou: FormControl<AuthorFormRawValue['aboutYou']>;
  location: FormControl<AuthorFormRawValue['location']>;
  website: FormControl<AuthorFormRawValue['website']>;
  publishDate: FormControl<AuthorFormRawValue['publishDate']>;
  publishedBy: FormControl<AuthorFormRawValue['publishedBy']>;
  status: FormControl<AuthorFormRawValue['status']>;
  approvedBy: FormControl<AuthorFormRawValue['approvedBy']>;
  approvedDate: FormControl<AuthorFormRawValue['approvedDate']>;
};

export type AuthorFormGroup = FormGroup<AuthorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AuthorFormService {
  createAuthorFormGroup(author: AuthorFormGroupInput = { id: null }): AuthorFormGroup {
    const authorRawValue = this.convertAuthorToAuthorRawValue({
      ...this.getFormDefaults(),
      ...author,
    });
    return new FormGroup<AuthorFormGroupContent>({
      id: new FormControl(
        { value: authorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(authorRawValue.name),
      githubUsername: new FormControl(authorRawValue.githubUsername),
      aboutYou: new FormControl(authorRawValue.aboutYou),
      location: new FormControl(authorRawValue.location),
      website: new FormControl(authorRawValue.website),
      publishDate: new FormControl(authorRawValue.publishDate),
      publishedBy: new FormControl(authorRawValue.publishedBy),
      status: new FormControl(authorRawValue.status),
      approvedBy: new FormControl(authorRawValue.approvedBy),
      approvedDate: new FormControl(authorRawValue.approvedDate),
    });
  }

  getAuthor(form: AuthorFormGroup): IAuthor | NewAuthor {
    return this.convertAuthorRawValueToAuthor(form.getRawValue() as AuthorFormRawValue | NewAuthorFormRawValue);
  }

  resetForm(form: AuthorFormGroup, author: AuthorFormGroupInput): void {
    const authorRawValue = this.convertAuthorToAuthorRawValue({ ...this.getFormDefaults(), ...author });
    form.reset(
      {
        ...authorRawValue,
        id: { value: authorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AuthorFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      publishDate: currentTime,
      approvedDate: currentTime,
    };
  }

  private convertAuthorRawValueToAuthor(rawAuthor: AuthorFormRawValue | NewAuthorFormRawValue): IAuthor | NewAuthor {
    return {
      ...rawAuthor,
      publishDate: dayjs(rawAuthor.publishDate, DATE_TIME_FORMAT),
      approvedDate: dayjs(rawAuthor.approvedDate, DATE_TIME_FORMAT),
    };
  }

  private convertAuthorToAuthorRawValue(
    author: IAuthor | (Partial<NewAuthor> & AuthorFormDefaults),
  ): AuthorFormRawValue | PartialWithRequiredKeyOf<NewAuthorFormRawValue> {
    return {
      ...author,
      publishDate: author.publishDate ? author.publishDate.format(DATE_TIME_FORMAT) : undefined,
      approvedDate: author.approvedDate ? author.approvedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
