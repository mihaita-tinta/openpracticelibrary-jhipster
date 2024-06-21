import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../blog-item.test-samples';

import { BlogItemFormService } from './blog-item-form.service';

describe('BlogItem Form Service', () => {
  let service: BlogItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BlogItemFormService);
  });

  describe('Service methods', () => {
    describe('createBlogItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBlogItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            subtitle: expect.any(Object),
            publishDate: expect.any(Object),
            publishedBy: expect.any(Object),
            status: expect.any(Object),
            approvedBy: expect.any(Object),
            approvedDate: expect.any(Object),
            authors: expect.any(Object),
            jumbotronAltText: expect.any(Object),
            body: expect.any(Object),
            jumbotronImage: expect.any(Object),
          }),
        );
      });

      it('passing IBlogItem should create a new form with FormGroup', () => {
        const formGroup = service.createBlogItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            subtitle: expect.any(Object),
            publishDate: expect.any(Object),
            publishedBy: expect.any(Object),
            status: expect.any(Object),
            approvedBy: expect.any(Object),
            approvedDate: expect.any(Object),
            authors: expect.any(Object),
            jumbotronAltText: expect.any(Object),
            body: expect.any(Object),
            jumbotronImage: expect.any(Object),
          }),
        );
      });
    });

    describe('getBlogItem', () => {
      it('should return NewBlogItem for default BlogItem initial value', () => {
        const formGroup = service.createBlogItemFormGroup(sampleWithNewData);

        const blogItem = service.getBlogItem(formGroup) as any;

        expect(blogItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewBlogItem for empty BlogItem initial value', () => {
        const formGroup = service.createBlogItemFormGroup();

        const blogItem = service.getBlogItem(formGroup) as any;

        expect(blogItem).toMatchObject({});
      });

      it('should return IBlogItem', () => {
        const formGroup = service.createBlogItemFormGroup(sampleWithRequiredData);

        const blogItem = service.getBlogItem(formGroup) as any;

        expect(blogItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBlogItem should not enable id FormControl', () => {
        const formGroup = service.createBlogItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBlogItem should disable id FormControl', () => {
        const formGroup = service.createBlogItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
