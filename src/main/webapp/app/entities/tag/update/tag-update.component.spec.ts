import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IPracticeItem } from 'app/entities/practice-item/practice-item.model';
import { PracticeItemService } from 'app/entities/practice-item/service/practice-item.service';
import { TagService } from '../service/tag.service';
import { ITag } from '../tag.model';
import { TagFormService } from './tag-form.service';

import { TagUpdateComponent } from './tag-update.component';

describe('Tag Management Update Component', () => {
  let comp: TagUpdateComponent;
  let fixture: ComponentFixture<TagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tagFormService: TagFormService;
  let tagService: TagService;
  let practiceItemService: PracticeItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TagUpdateComponent],
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
      .overrideTemplate(TagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tagFormService = TestBed.inject(TagFormService);
    tagService = TestBed.inject(TagService);
    practiceItemService = TestBed.inject(PracticeItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PracticeItem query and add missing value', () => {
      const tag: ITag = { id: 456 };
      const practiceItem: IPracticeItem = { id: 31219 };
      tag.practiceItem = practiceItem;

      const practiceItemCollection: IPracticeItem[] = [{ id: 4651 }];
      jest.spyOn(practiceItemService, 'query').mockReturnValue(of(new HttpResponse({ body: practiceItemCollection })));
      const additionalPracticeItems = [practiceItem];
      const expectedCollection: IPracticeItem[] = [...additionalPracticeItems, ...practiceItemCollection];
      jest.spyOn(practiceItemService, 'addPracticeItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tag });
      comp.ngOnInit();

      expect(practiceItemService.query).toHaveBeenCalled();
      expect(practiceItemService.addPracticeItemToCollectionIfMissing).toHaveBeenCalledWith(
        practiceItemCollection,
        ...additionalPracticeItems.map(expect.objectContaining),
      );
      expect(comp.practiceItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tag: ITag = { id: 456 };
      const practiceItem: IPracticeItem = { id: 10713 };
      tag.practiceItem = practiceItem;

      activatedRoute.data = of({ tag });
      comp.ngOnInit();

      expect(comp.practiceItemsSharedCollection).toContain(practiceItem);
      expect(comp.tag).toEqual(tag);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITag>>();
      const tag = { id: 123 };
      jest.spyOn(tagFormService, 'getTag').mockReturnValue(tag);
      jest.spyOn(tagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tag }));
      saveSubject.complete();

      // THEN
      expect(tagFormService.getTag).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tagService.update).toHaveBeenCalledWith(expect.objectContaining(tag));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITag>>();
      const tag = { id: 123 };
      jest.spyOn(tagFormService, 'getTag').mockReturnValue({ id: null });
      jest.spyOn(tagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tag: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tag }));
      saveSubject.complete();

      // THEN
      expect(tagFormService.getTag).toHaveBeenCalled();
      expect(tagService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITag>>();
      const tag = { id: 123 };
      jest.spyOn(tagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tagService.update).toHaveBeenCalled();
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
