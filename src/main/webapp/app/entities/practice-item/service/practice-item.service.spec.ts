import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPracticeItem } from '../practice-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../practice-item.test-samples';

import { PracticeItemService, RestPracticeItem } from './practice-item.service';

const requireRestSample: RestPracticeItem = {
  ...sampleWithRequiredData,
  publishDate: sampleWithRequiredData.publishDate?.toJSON(),
  approvedDate: sampleWithRequiredData.approvedDate?.toJSON(),
};

describe('PracticeItem Service', () => {
  let service: PracticeItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IPracticeItem | IPracticeItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PracticeItemService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PracticeItem', () => {
      const practiceItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(practiceItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PracticeItem', () => {
      const practiceItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(practiceItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PracticeItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PracticeItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PracticeItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPracticeItemToCollectionIfMissing', () => {
      it('should add a PracticeItem to an empty array', () => {
        const practiceItem: IPracticeItem = sampleWithRequiredData;
        expectedResult = service.addPracticeItemToCollectionIfMissing([], practiceItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(practiceItem);
      });

      it('should not add a PracticeItem to an array that contains it', () => {
        const practiceItem: IPracticeItem = sampleWithRequiredData;
        const practiceItemCollection: IPracticeItem[] = [
          {
            ...practiceItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPracticeItemToCollectionIfMissing(practiceItemCollection, practiceItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PracticeItem to an array that doesn't contain it", () => {
        const practiceItem: IPracticeItem = sampleWithRequiredData;
        const practiceItemCollection: IPracticeItem[] = [sampleWithPartialData];
        expectedResult = service.addPracticeItemToCollectionIfMissing(practiceItemCollection, practiceItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(practiceItem);
      });

      it('should add only unique PracticeItem to an array', () => {
        const practiceItemArray: IPracticeItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const practiceItemCollection: IPracticeItem[] = [sampleWithRequiredData];
        expectedResult = service.addPracticeItemToCollectionIfMissing(practiceItemCollection, ...practiceItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const practiceItem: IPracticeItem = sampleWithRequiredData;
        const practiceItem2: IPracticeItem = sampleWithPartialData;
        expectedResult = service.addPracticeItemToCollectionIfMissing([], practiceItem, practiceItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(practiceItem);
        expect(expectedResult).toContain(practiceItem2);
      });

      it('should accept null and undefined values', () => {
        const practiceItem: IPracticeItem = sampleWithRequiredData;
        expectedResult = service.addPracticeItemToCollectionIfMissing([], null, practiceItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(practiceItem);
      });

      it('should return initial array if no PracticeItem is added', () => {
        const practiceItemCollection: IPracticeItem[] = [sampleWithRequiredData];
        expectedResult = service.addPracticeItemToCollectionIfMissing(practiceItemCollection, undefined, null);
        expect(expectedResult).toEqual(practiceItemCollection);
      });
    });

    describe('comparePracticeItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePracticeItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePracticeItem(entity1, entity2);
        const compareResult2 = service.comparePracticeItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePracticeItem(entity1, entity2);
        const compareResult2 = service.comparePracticeItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePracticeItem(entity1, entity2);
        const compareResult2 = service.comparePracticeItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
