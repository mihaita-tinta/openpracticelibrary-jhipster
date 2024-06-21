import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IPracticeItem } from 'app/entities/practice-item/practice-item.model';
import { PracticeItemService } from 'app/entities/practice-item/service/practice-item.service';
import { LinkItemService } from '../service/link-item.service';
import { ILinkItem } from '../link-item.model';
import { LinkItemFormService } from './link-item-form.service';

import { LinkItemUpdateComponent } from './link-item-update.component';

describe('LinkItem Management Update Component', () => {
  let comp: LinkItemUpdateComponent;
  let fixture: ComponentFixture<LinkItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let linkItemFormService: LinkItemFormService;
  let linkItemService: LinkItemService;
  let practiceItemService: PracticeItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, LinkItemUpdateComponent],
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
      .overrideTemplate(LinkItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LinkItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    linkItemFormService = TestBed.inject(LinkItemFormService);
    linkItemService = TestBed.inject(LinkItemService);
    practiceItemService = TestBed.inject(PracticeItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call PracticeItem query and add missing value', () => {
      const linkItem: ILinkItem = { id: 456 };
      const practiceItem: IPracticeItem = { id: 31471 };
      linkItem.practiceItem = practiceItem;

      const practiceItemCollection: IPracticeItem[] = [{ id: 31127 }];
      jest.spyOn(practiceItemService, 'query').mockReturnValue(of(new HttpResponse({ body: practiceItemCollection })));
      const additionalPracticeItems = [practiceItem];
      const expectedCollection: IPracticeItem[] = [...additionalPracticeItems, ...practiceItemCollection];
      jest.spyOn(practiceItemService, 'addPracticeItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ linkItem });
      comp.ngOnInit();

      expect(practiceItemService.query).toHaveBeenCalled();
      expect(practiceItemService.addPracticeItemToCollectionIfMissing).toHaveBeenCalledWith(
        practiceItemCollection,
        ...additionalPracticeItems.map(expect.objectContaining),
      );
      expect(comp.practiceItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const linkItem: ILinkItem = { id: 456 };
      const practiceItem: IPracticeItem = { id: 21678 };
      linkItem.practiceItem = practiceItem;

      activatedRoute.data = of({ linkItem });
      comp.ngOnInit();

      expect(comp.practiceItemsSharedCollection).toContain(practiceItem);
      expect(comp.linkItem).toEqual(linkItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILinkItem>>();
      const linkItem = { id: 123 };
      jest.spyOn(linkItemFormService, 'getLinkItem').mockReturnValue(linkItem);
      jest.spyOn(linkItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ linkItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: linkItem }));
      saveSubject.complete();

      // THEN
      expect(linkItemFormService.getLinkItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(linkItemService.update).toHaveBeenCalledWith(expect.objectContaining(linkItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILinkItem>>();
      const linkItem = { id: 123 };
      jest.spyOn(linkItemFormService, 'getLinkItem').mockReturnValue({ id: null });
      jest.spyOn(linkItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ linkItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: linkItem }));
      saveSubject.complete();

      // THEN
      expect(linkItemFormService.getLinkItem).toHaveBeenCalled();
      expect(linkItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILinkItem>>();
      const linkItem = { id: 123 };
      jest.spyOn(linkItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ linkItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(linkItemService.update).toHaveBeenCalled();
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
