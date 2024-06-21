import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IMediaAsset } from 'app/entities/media-asset/media-asset.model';
import { MediaAssetService } from 'app/entities/media-asset/service/media-asset.service';
import { BlogItemService } from '../service/blog-item.service';
import { IBlogItem } from '../blog-item.model';
import { BlogItemFormService } from './blog-item-form.service';

import { BlogItemUpdateComponent } from './blog-item-update.component';

describe('BlogItem Management Update Component', () => {
  let comp: BlogItemUpdateComponent;
  let fixture: ComponentFixture<BlogItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let blogItemFormService: BlogItemFormService;
  let blogItemService: BlogItemService;
  let mediaAssetService: MediaAssetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BlogItemUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BlogItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BlogItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    blogItemFormService = TestBed.inject(BlogItemFormService);
    blogItemService = TestBed.inject(BlogItemService);
    mediaAssetService = TestBed.inject(MediaAssetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call jumbotronImage query and add missing value', () => {
      const blogItem: IBlogItem = { id: 456 };
      const jumbotronImage: IMediaAsset = { id: 26567 };
      blogItem.jumbotronImage = jumbotronImage;

      const jumbotronImageCollection: IMediaAsset[] = [{ id: 16085 }];
      jest.spyOn(mediaAssetService, 'query').mockReturnValue(of(new HttpResponse({ body: jumbotronImageCollection })));
      const expectedCollection: IMediaAsset[] = [jumbotronImage, ...jumbotronImageCollection];
      jest.spyOn(mediaAssetService, 'addMediaAssetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ blogItem });
      comp.ngOnInit();

      expect(mediaAssetService.query).toHaveBeenCalled();
      expect(mediaAssetService.addMediaAssetToCollectionIfMissing).toHaveBeenCalledWith(jumbotronImageCollection, jumbotronImage);
      expect(comp.jumbotronImagesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const blogItem: IBlogItem = { id: 456 };
      const jumbotronImage: IMediaAsset = { id: 19592 };
      blogItem.jumbotronImage = jumbotronImage;

      activatedRoute.data = of({ blogItem });
      comp.ngOnInit();

      expect(comp.jumbotronImagesCollection).toContain(jumbotronImage);
      expect(comp.blogItem).toEqual(blogItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBlogItem>>();
      const blogItem = { id: 123 };
      jest.spyOn(blogItemFormService, 'getBlogItem').mockReturnValue(blogItem);
      jest.spyOn(blogItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ blogItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: blogItem }));
      saveSubject.complete();

      // THEN
      expect(blogItemFormService.getBlogItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(blogItemService.update).toHaveBeenCalledWith(expect.objectContaining(blogItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBlogItem>>();
      const blogItem = { id: 123 };
      jest.spyOn(blogItemFormService, 'getBlogItem').mockReturnValue({ id: null });
      jest.spyOn(blogItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ blogItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: blogItem }));
      saveSubject.complete();

      // THEN
      expect(blogItemFormService.getBlogItem).toHaveBeenCalled();
      expect(blogItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBlogItem>>();
      const blogItem = { id: 123 };
      jest.spyOn(blogItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ blogItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(blogItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMediaAsset', () => {
      it('Should forward to mediaAssetService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(mediaAssetService, 'compareMediaAsset');
        comp.compareMediaAsset(entity, entity2);
        expect(mediaAssetService.compareMediaAsset).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
