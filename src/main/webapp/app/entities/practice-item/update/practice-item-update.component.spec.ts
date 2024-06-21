import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IMediaAsset } from 'app/entities/media-asset/media-asset.model';
import { MediaAssetService } from 'app/entities/media-asset/service/media-asset.service';
import { PracticeItemService } from '../service/practice-item.service';
import { IPracticeItem } from '../practice-item.model';
import { PracticeItemFormService } from './practice-item-form.service';

import { PracticeItemUpdateComponent } from './practice-item-update.component';

describe('PracticeItem Management Update Component', () => {
  let comp: PracticeItemUpdateComponent;
  let fixture: ComponentFixture<PracticeItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let practiceItemFormService: PracticeItemFormService;
  let practiceItemService: PracticeItemService;
  let mediaAssetService: MediaAssetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PracticeItemUpdateComponent],
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
      .overrideTemplate(PracticeItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PracticeItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    practiceItemFormService = TestBed.inject(PracticeItemFormService);
    practiceItemService = TestBed.inject(PracticeItemService);
    mediaAssetService = TestBed.inject(MediaAssetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call coverImage query and add missing value', () => {
      const practiceItem: IPracticeItem = { id: 456 };
      const coverImage: IMediaAsset = { id: 13965 };
      practiceItem.coverImage = coverImage;

      const coverImageCollection: IMediaAsset[] = [{ id: 1553 }];
      jest.spyOn(mediaAssetService, 'query').mockReturnValue(of(new HttpResponse({ body: coverImageCollection })));
      const expectedCollection: IMediaAsset[] = [coverImage, ...coverImageCollection];
      jest.spyOn(mediaAssetService, 'addMediaAssetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ practiceItem });
      comp.ngOnInit();

      expect(mediaAssetService.query).toHaveBeenCalled();
      expect(mediaAssetService.addMediaAssetToCollectionIfMissing).toHaveBeenCalledWith(coverImageCollection, coverImage);
      expect(comp.coverImagesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const practiceItem: IPracticeItem = { id: 456 };
      const coverImage: IMediaAsset = { id: 12155 };
      practiceItem.coverImage = coverImage;

      activatedRoute.data = of({ practiceItem });
      comp.ngOnInit();

      expect(comp.coverImagesCollection).toContain(coverImage);
      expect(comp.practiceItem).toEqual(practiceItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPracticeItem>>();
      const practiceItem = { id: 123 };
      jest.spyOn(practiceItemFormService, 'getPracticeItem').mockReturnValue(practiceItem);
      jest.spyOn(practiceItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ practiceItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: practiceItem }));
      saveSubject.complete();

      // THEN
      expect(practiceItemFormService.getPracticeItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(practiceItemService.update).toHaveBeenCalledWith(expect.objectContaining(practiceItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPracticeItem>>();
      const practiceItem = { id: 123 };
      jest.spyOn(practiceItemFormService, 'getPracticeItem').mockReturnValue({ id: null });
      jest.spyOn(practiceItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ practiceItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: practiceItem }));
      saveSubject.complete();

      // THEN
      expect(practiceItemFormService.getPracticeItem).toHaveBeenCalled();
      expect(practiceItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPracticeItem>>();
      const practiceItem = { id: 123 };
      jest.spyOn(practiceItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ practiceItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(practiceItemService.update).toHaveBeenCalled();
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
