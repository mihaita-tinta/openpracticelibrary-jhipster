import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../media-asset.test-samples';

import { MediaAssetFormService } from './media-asset-form.service';

describe('MediaAsset Form Service', () => {
  let service: MediaAssetFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MediaAssetFormService);
  });

  describe('Service methods', () => {
    describe('createMediaAssetFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMediaAssetFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            content: expect.any(Object),
            sortIndex: expect.any(Object),
            practices: expect.any(Object),
          }),
        );
      });

      it('passing IMediaAsset should create a new form with FormGroup', () => {
        const formGroup = service.createMediaAssetFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            content: expect.any(Object),
            sortIndex: expect.any(Object),
            practices: expect.any(Object),
          }),
        );
      });
    });

    describe('getMediaAsset', () => {
      it('should return NewMediaAsset for default MediaAsset initial value', () => {
        const formGroup = service.createMediaAssetFormGroup(sampleWithNewData);

        const mediaAsset = service.getMediaAsset(formGroup) as any;

        expect(mediaAsset).toMatchObject(sampleWithNewData);
      });

      it('should return NewMediaAsset for empty MediaAsset initial value', () => {
        const formGroup = service.createMediaAssetFormGroup();

        const mediaAsset = service.getMediaAsset(formGroup) as any;

        expect(mediaAsset).toMatchObject({});
      });

      it('should return IMediaAsset', () => {
        const formGroup = service.createMediaAssetFormGroup(sampleWithRequiredData);

        const mediaAsset = service.getMediaAsset(formGroup) as any;

        expect(mediaAsset).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMediaAsset should not enable id FormControl', () => {
        const formGroup = service.createMediaAssetFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMediaAsset should disable id FormControl', () => {
        const formGroup = service.createMediaAssetFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
