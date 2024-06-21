import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../link-item.test-samples';

import { LinkItemFormService } from './link-item-form.service';

describe('LinkItem Form Service', () => {
  let service: LinkItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LinkItemFormService);
  });

  describe('Service methods', () => {
    describe('createLinkItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLinkItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            url: expect.any(Object),
            sortIndex: expect.any(Object),
            practiceItem: expect.any(Object),
          }),
        );
      });

      it('passing ILinkItem should create a new form with FormGroup', () => {
        const formGroup = service.createLinkItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            url: expect.any(Object),
            sortIndex: expect.any(Object),
            practiceItem: expect.any(Object),
          }),
        );
      });
    });

    describe('getLinkItem', () => {
      it('should return NewLinkItem for default LinkItem initial value', () => {
        const formGroup = service.createLinkItemFormGroup(sampleWithNewData);

        const linkItem = service.getLinkItem(formGroup) as any;

        expect(linkItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewLinkItem for empty LinkItem initial value', () => {
        const formGroup = service.createLinkItemFormGroup();

        const linkItem = service.getLinkItem(formGroup) as any;

        expect(linkItem).toMatchObject({});
      });

      it('should return ILinkItem', () => {
        const formGroup = service.createLinkItemFormGroup(sampleWithRequiredData);

        const linkItem = service.getLinkItem(formGroup) as any;

        expect(linkItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILinkItem should not enable id FormControl', () => {
        const formGroup = service.createLinkItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLinkItem should disable id FormControl', () => {
        const formGroup = service.createLinkItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
