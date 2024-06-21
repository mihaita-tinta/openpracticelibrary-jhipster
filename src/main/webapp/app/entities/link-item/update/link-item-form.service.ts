import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILinkItem, NewLinkItem } from '../link-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILinkItem for edit and NewLinkItemFormGroupInput for create.
 */
type LinkItemFormGroupInput = ILinkItem | PartialWithRequiredKeyOf<NewLinkItem>;

type LinkItemFormDefaults = Pick<NewLinkItem, 'id'>;

type LinkItemFormGroupContent = {
  id: FormControl<ILinkItem['id'] | NewLinkItem['id']>;
  url: FormControl<ILinkItem['url']>;
  sortIndex: FormControl<ILinkItem['sortIndex']>;
  practiceItem: FormControl<ILinkItem['practiceItem']>;
};

export type LinkItemFormGroup = FormGroup<LinkItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LinkItemFormService {
  createLinkItemFormGroup(linkItem: LinkItemFormGroupInput = { id: null }): LinkItemFormGroup {
    const linkItemRawValue = {
      ...this.getFormDefaults(),
      ...linkItem,
    };
    return new FormGroup<LinkItemFormGroupContent>({
      id: new FormControl(
        { value: linkItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      url: new FormControl(linkItemRawValue.url),
      sortIndex: new FormControl(linkItemRawValue.sortIndex),
      practiceItem: new FormControl(linkItemRawValue.practiceItem),
    });
  }

  getLinkItem(form: LinkItemFormGroup): ILinkItem | NewLinkItem {
    return form.getRawValue() as ILinkItem | NewLinkItem;
  }

  resetForm(form: LinkItemFormGroup, linkItem: LinkItemFormGroupInput): void {
    const linkItemRawValue = { ...this.getFormDefaults(), ...linkItem };
    form.reset(
      {
        ...linkItemRawValue,
        id: { value: linkItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LinkItemFormDefaults {
    return {
      id: null,
    };
  }
}
