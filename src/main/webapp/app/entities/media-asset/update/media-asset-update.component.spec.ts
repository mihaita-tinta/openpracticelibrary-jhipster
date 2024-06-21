import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IPracticeItem } from 'app/entities/practice-item/practice-item.model';
import { PracticeItemService } from 'app/entities/practice-item/service/practice-item.service';
import { MediaAssetService } from '../service/media-asset.service';
import { IMediaAsset } from '../media-asset.model';
import { MediaAssetFormService } from './media-asset-form.service';

import { MediaAssetUpdateComponent } from './media-asset-update.component';

describe('MediaAsset Management Update Component', () => {
  let comp: MediaAssetUpdateComponent;
  let fixture: ComponentFixture<MediaAssetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mediaAssetFormService: MediaAssetFormService;
  let mediaAssetService: MediaAssetService;
  let practiceItemService: PracticeItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MediaAssetUpdateComponent],
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
      .overrideTemplate(MediaAssetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MediaAssetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mediaAssetFormService = TestBed.inject(MediaAssetFormService);
    mediaAssetService = TestBed.inject(MediaAssetService);
    practiceItemService = TestBed.inject(PracticeItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PracticeItem query and add missing value', () => {
      const mediaAsset: IMediaAsset = { id: 456 };
      const practices: IPracticeItem = { id: 5581 };
      mediaAsset.practices = practices;

      const practiceItemCollection: IPracticeItem[] = [{ id: 15991 }];
      jest.spyOn(practiceItemService, 'query').mockReturnValue(of(new HttpResponse({ body: practiceItemCollection })));
      const additionalPracticeItems = [practices];
      const expectedCollection: IPracticeItem[] = [...additionalPracticeItems, ...practiceItemCollection];
      jest.spyOn(practiceItemService, 'addPracticeItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mediaAsset });
      comp.ngOnInit();

      expect(practiceItemService.query).toHaveBeenCalled();
      expect(practiceItemService.addPracticeItemToCollectionIfMissing).toHaveBeenCalledWith(
        practiceItemCollection,
        ...additionalPracticeItems.map(expect.objectContaining),
      );
      expect(comp.practiceItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const mediaAsset: IMediaAsset = { id: 456 };
      const practices: IPracticeItem = { id: 27606 };
      mediaAsset.practices = practices;

      activatedRoute.data = of({ mediaAsset });
      comp.ngOnInit();

      expect(comp.practiceItemsSharedCollection).toContain(practices);
      expect(comp.mediaAsset).toEqual(mediaAsset);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMediaAsset>>();
      const mediaAsset = { id: 123 };
      jest.spyOn(mediaAssetFormService, 'getMediaAsset').mockReturnValue(mediaAsset);
      jest.spyOn(mediaAssetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mediaAsset });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mediaAsset }));
      saveSubject.complete();

      // THEN
      expect(mediaAssetFormService.getMediaAsset).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mediaAssetService.update).toHaveBeenCalledWith(expect.objectContaining(mediaAsset));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMediaAsset>>();
      const mediaAsset = { id: 123 };
      jest.spyOn(mediaAssetFormService, 'getMediaAsset').mockReturnValue({ id: null });
      jest.spyOn(mediaAssetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mediaAsset: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mediaAsset }));
      saveSubject.complete();

      // THEN
      expect(mediaAssetFormService.getMediaAsset).toHaveBeenCalled();
      expect(mediaAssetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMediaAsset>>();
      const mediaAsset = { id: 123 };
      jest.spyOn(mediaAssetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mediaAsset });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mediaAssetService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePracticeItem', () => {
      it('Should forward to practiceItemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(practiceItemService, 'comparePracticeItem');
        comp.comparePracticeItem(entity, entity2);
        expect(practiceItemService.comparePracticeItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
