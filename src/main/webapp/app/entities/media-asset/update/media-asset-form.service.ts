import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMediaAsset, NewMediaAsset } from '../media-asset.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMediaAsset for edit and NewMediaAssetFormGroupInput for create.
 */
type MediaAssetFormGroupInput = IMediaAsset | PartialWithRequiredKeyOf<NewMediaAsset>;

type MediaAssetFormDefaults = Pick<NewMediaAsset, 'id'>;

type MediaAssetFormGroupContent = {
  id: FormControl<IMediaAsset['id'] | NewMediaAsset['id']>;
  type: FormControl<IMediaAsset['type']>;
  content: FormControl<IMediaAsset['content']>;
  contentContentType: FormControl<IMediaAsset['contentContentType']>;
  sortIndex: FormControl<IMediaAsset['sortIndex']>;
  practices: FormControl<IMediaAsset['practices']>;
};

export type MediaAssetFormGroup = FormGroup<MediaAssetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MediaAssetFormService {
  createMediaAssetFormGroup(mediaAsset: MediaAssetFormGroupInput = { id: null }): MediaAssetFormGroup {
    const mediaAssetRawValue = {
      ...this.getFormDefaults(),
      ...mediaAsset,
    };
    return new FormGroup<MediaAssetFormGroupContent>({
      id: new FormControl(
        { value: mediaAssetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(mediaAssetRawValue.type),
      content: new FormControl(mediaAssetRawValue.content),
      contentContentType: new FormControl(mediaAssetRawValue.contentContentType),
      sortIndex: new FormControl(mediaAssetRawValue.sortIndex),
      practices: new FormControl(mediaAssetRawValue.practices),
    });
  }

  getMediaAsset(form: MediaAssetFormGroup): IMediaAsset | NewMediaAsset {
    return form.getRawValue() as IMediaAsset | NewMediaAsset;
  }

  resetForm(form: MediaAssetFormGroup, mediaAsset: MediaAssetFormGroupInput): void {
    const mediaAssetRawValue = { ...this.getFormDefaults(), ...mediaAsset };
    form.reset(
      {
        ...mediaAssetRawValue,
        id: { value: mediaAssetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MediaAssetFormDefaults {
    return {
      id: null,
    };
  }
}
