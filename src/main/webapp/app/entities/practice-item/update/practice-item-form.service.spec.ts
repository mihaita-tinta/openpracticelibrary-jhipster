import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../practice-item.test-samples';

import { PracticeItemFormService } from './practice-item-form.service';

describe('PracticeItem Form Service', () => {
  let service: PracticeItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PracticeItemFormService);
  });

  describe('Service methods', () => {
    describe('createPracticeItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPracticeItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            objective: expect.any(Object),
            publishDate: expect.any(Object),
            publishedBy: expect.any(Object),
            status: expect.any(Object),
            approvedBy: expect.any(Object),
            approvedDate: expect.any(Object),
            authors: expect.any(Object),
            facilitationDifficulty: expect.any(Object),
            mobiusLoopTag: expect.any(Object),
            what: expect.any(Object),
            why: expect.any(Object),
            how: expect.any(Object),
            numberOfPeopleRequired: expect.any(Object),
            timeLength: expect.any(Object),
            expectedParticipants: expect.any(Object),
            coverImage: expect.any(Object),
          }),
        );
      });

      it('passing IPracticeItem should create a new form with FormGroup', () => {
        const formGroup = service.createPracticeItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            objective: expect.any(Object),
            publishDate: expect.any(Object),
            publishedBy: expect.any(Object),
            status: expect.any(Object),
            approvedBy: expect.any(Object),
            approvedDate: expect.any(Object),
            authors: expect.any(Object),
            facilitationDifficulty: expect.any(Object),
            mobiusLoopTag: expect.any(Object),
            what: expect.any(Object),
            why: expect.any(Object),
            how: expect.any(Object),
            numberOfPeopleRequired: expect.any(Object),
            timeLength: expect.any(Object),
            expectedParticipants: expect.any(Object),
            coverImage: expect.any(Object),
          }),
        );
      });
    });

    describe('getPracticeItem', () => {
      it('should return NewPracticeItem for default PracticeItem initial value', () => {
        const formGroup = service.createPracticeItemFormGroup(sampleWithNewData);

        const practiceItem = service.getPracticeItem(formGroup) as any;

        expect(practiceItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewPracticeItem for empty PracticeItem initial value', () => {
        const formGroup = service.createPracticeItemFormGroup();

        const practiceItem = service.getPracticeItem(formGroup) as any;

        expect(practiceItem).toMatchObject({});
      });

      it('should return IPracticeItem', () => {
        const formGroup = service.createPracticeItemFormGroup(sampleWithRequiredData);

        const practiceItem = service.getPracticeItem(formGroup) as any;

        expect(practiceItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPracticeItem should not enable id FormControl', () => {
        const formGroup = service.createPracticeItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPracticeItem should disable id FormControl', () => {
        const formGroup = service.createPracticeItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
