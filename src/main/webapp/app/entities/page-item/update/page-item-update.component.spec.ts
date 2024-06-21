import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IMediaAsset } from 'app/entities/media-asset/media-asset.model';
import { MediaAssetService } from 'app/entities/media-asset/service/media-asset.service';
import { PageItemService } from '../service/page-item.service';
import { IPageItem } from '../page-item.model';
import { PageItemFormService } from './page-item-form.service';

import { PageItemUpdateComponent } from './page-item-update.component';

describe('PageItem Management Update Component', () => {
  let comp: PageItemUpdateComponent;
  let fixture: ComponentFixture<PageItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pageItemFormService: PageItemFormService;
  let pageItemService: PageItemService;
  let mediaAssetService: MediaAssetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PageItemUpdateComponent],
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
      .overrideTemplate(PageItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PageItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pageItemFormService = TestBed.inject(PageItemFormService);
    pageItemService = TestBed.inject(PageItemService);
    mediaAssetService = TestBed.inject(MediaAssetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call jumbotronImage query and add missing value', () => {
      const pageItem: IPageItem = { id: 456 };
      const jumbotronImage: IMediaAsset = { id: 27618 };
      pageItem.jumbotronImage = jumbotronImage;

      const jumbotronImageCollection: IMediaAsset[] = [{ id: 784 }];
      jest.spyOn(mediaAssetService, 'query').mockReturnValue(of(new HttpResponse({ body: jumbotronImageCollection })));
      const expectedCollection: IMediaAsset[] = [jumbotronImage, ...jumbotronImageCollection];
      jest.spyOn(mediaAssetService, 'addMediaAssetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pageItem });
      comp.ngOnInit();

      expect(mediaAssetService.query).toHaveBeenCalled();
      expect(mediaAssetService.addMediaAssetToCollectionIfMissing).toHaveBeenCalledWith(jumbotronImageCollection, jumbotronImage);
      expect(comp.jumbotronImagesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pageItem: IPageItem = { id: 456 };
      const jumbotronImage: IMediaAsset = { id: 24373 };
      pageItem.jumbotronImage = jumbotronImage;

      activatedRoute.data = of({ pageItem });
      comp.ngOnInit();

      expect(comp.jumbotronImagesCollection).toContain(jumbotronImage);
      expect(comp.pageItem).toEqual(pageItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageItem>>();
      const pageItem = { id: 123 };
      jest.spyOn(pageItemFormService, 'getPageItem').mockReturnValue(pageItem);
      jest.spyOn(pageItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pageItem }));
      saveSubject.complete();

      // THEN
      expect(pageItemFormService.getPageItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pageItemService.update).toHaveBeenCalledWith(expect.objectContaining(pageItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageItem>>();
      const pageItem = { id: 123 };
      jest.spyOn(pageItemFormService, 'getPageItem').mockReturnValue({ id: null });
      jest.spyOn(pageItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pageItem }));
      saveSubject.complete();

      // THEN
      expect(pageItemFormService.getPageItem).toHaveBeenCalled();
      expect(pageItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPageItem>>();
      const pageItem = { id: 123 };
      jest.spyOn(pageItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pageItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pageItemService.update).toHaveBeenCalled();
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
