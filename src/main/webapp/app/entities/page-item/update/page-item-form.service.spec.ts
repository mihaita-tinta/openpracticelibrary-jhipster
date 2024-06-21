import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../page-item.test-samples';

import { PageItemFormService } from './page-item-form.service';

describe('PageItem Form Service', () => {
  let service: PageItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PageItemFormService);
  });

  describe('Service methods', () => {
    describe('createPageItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPageItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            authors: expect.any(Object),
            menu: expect.any(Object),
            menuWeight: expect.any(Object),
            publishDate: expect.any(Object),
            publishedBy: expect.any(Object),
            status: expect.any(Object),
            approvedBy: expect.any(Object),
            approvedDate: expect.any(Object),
            jumbotronAltText: expect.any(Object),
            body: expect.any(Object),
            jumbotronImage: expect.any(Object),
          }),
        );
      });

      it('passing IPageItem should create a new form with FormGroup', () => {
        const formGroup = service.createPageItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            authors: expect.any(Object),
            menu: expect.any(Object),
            menuWeight: expect.any(Object),
            publishDate: expect.any(Object),
            publishedBy: expect.any(Object),
            status: expect.any(Object),
            approvedBy: expect.any(Object),
            approvedDate: expect.any(Object),
            jumbotronAltText: expect.any(Object),
            body: expect.any(Object),
            jumbotronImage: expect.any(Object),
          }),
        );
      });
    });

    describe('getPageItem', () => {
      it('should return NewPageItem for default PageItem initial value', () => {
        const formGroup = service.createPageItemFormGroup(sampleWithNewData);

        const pageItem = service.getPageItem(formGroup) as any;

        expect(pageItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewPageItem for empty PageItem initial value', () => {
        const formGroup = service.createPageItemFormGroup();

        const pageItem = service.getPageItem(formGroup) as any;

        expect(pageItem).toMatchObject({});
      });

      it('should return IPageItem', () => {
        const formGroup = service.createPageItemFormGroup(sampleWithRequiredData);

        const pageItem = service.getPageItem(formGroup) as any;

        expect(pageItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPageItem should not enable id FormControl', () => {
        const formGroup = service.createPageItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPageItem should disable id FormControl', () => {
        const formGroup = service.createPageItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
