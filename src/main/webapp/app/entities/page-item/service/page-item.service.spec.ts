import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPageItem } from '../page-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../page-item.test-samples';

import { PageItemService, RestPageItem } from './page-item.service';

const requireRestSample: RestPageItem = {
  ...sampleWithRequiredData,
  publishDate: sampleWithRequiredData.publishDate?.toJSON(),
  approvedDate: sampleWithRequiredData.approvedDate?.toJSON(),
};

describe('PageItem Service', () => {
  let service: PageItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IPageItem | IPageItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PageItemService);
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

    it('should create a PageItem', () => {
      const pageItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pageItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PageItem', () => {
      const pageItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pageItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PageItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PageItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PageItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPageItemToCollectionIfMissing', () => {
      it('should add a PageItem to an empty array', () => {
        const pageItem: IPageItem = sampleWithRequiredData;
        expectedResult = service.addPageItemToCollectionIfMissing([], pageItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pageItem);
      });

      it('should not add a PageItem to an array that contains it', () => {
        const pageItem: IPageItem = sampleWithRequiredData;
        const pageItemCollection: IPageItem[] = [
          {
            ...pageItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPageItemToCollectionIfMissing(pageItemCollection, pageItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PageItem to an array that doesn't contain it", () => {
        const pageItem: IPageItem = sampleWithRequiredData;
        const pageItemCollection: IPageItem[] = [sampleWithPartialData];
        expectedResult = service.addPageItemToCollectionIfMissing(pageItemCollection, pageItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pageItem);
      });

      it('should add only unique PageItem to an array', () => {
        const pageItemArray: IPageItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pageItemCollection: IPageItem[] = [sampleWithRequiredData];
        expectedResult = service.addPageItemToCollectionIfMissing(pageItemCollection, ...pageItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pageItem: IPageItem = sampleWithRequiredData;
        const pageItem2: IPageItem = sampleWithPartialData;
        expectedResult = service.addPageItemToCollectionIfMissing([], pageItem, pageItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pageItem);
        expect(expectedResult).toContain(pageItem2);
      });

      it('should accept null and undefined values', () => {
        const pageItem: IPageItem = sampleWithRequiredData;
        expectedResult = service.addPageItemToCollectionIfMissing([], null, pageItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pageItem);
      });

      it('should return initial array if no PageItem is added', () => {
        const pageItemCollection: IPageItem[] = [sampleWithRequiredData];
        expectedResult = service.addPageItemToCollectionIfMissing(pageItemCollection, undefined, null);
        expect(expectedResult).toEqual(pageItemCollection);
      });
    });

    describe('comparePageItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePageItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePageItem(entity1, entity2);
        const compareResult2 = service.comparePageItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePageItem(entity1, entity2);
        const compareResult2 = service.comparePageItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePageItem(entity1, entity2);
        const compareResult2 = service.comparePageItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
