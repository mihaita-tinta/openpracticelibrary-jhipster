import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILinkItem } from '../link-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../link-item.test-samples';

import { LinkItemService } from './link-item.service';

const requireRestSample: ILinkItem = {
  ...sampleWithRequiredData,
};

describe('LinkItem Service', () => {
  let service: LinkItemService;
  let httpMock: HttpTestingController;
  let expectedResult: ILinkItem | ILinkItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LinkItemService);
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

    it('should create a LinkItem', () => {
      const linkItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(linkItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LinkItem', () => {
      const linkItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(linkItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LinkItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LinkItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LinkItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLinkItemToCollectionIfMissing', () => {
      it('should add a LinkItem to an empty array', () => {
        const linkItem: ILinkItem = sampleWithRequiredData;
        expectedResult = service.addLinkItemToCollectionIfMissing([], linkItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(linkItem);
      });

      it('should not add a LinkItem to an array that contains it', () => {
        const linkItem: ILinkItem = sampleWithRequiredData;
        const linkItemCollection: ILinkItem[] = [
          {
            ...linkItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLinkItemToCollectionIfMissing(linkItemCollection, linkItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LinkItem to an array that doesn't contain it", () => {
        const linkItem: ILinkItem = sampleWithRequiredData;
        const linkItemCollection: ILinkItem[] = [sampleWithPartialData];
        expectedResult = service.addLinkItemToCollectionIfMissing(linkItemCollection, linkItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(linkItem);
      });

      it('should add only unique LinkItem to an array', () => {
        const linkItemArray: ILinkItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const linkItemCollection: ILinkItem[] = [sampleWithRequiredData];
        expectedResult = service.addLinkItemToCollectionIfMissing(linkItemCollection, ...linkItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const linkItem: ILinkItem = sampleWithRequiredData;
        const linkItem2: ILinkItem = sampleWithPartialData;
        expectedResult = service.addLinkItemToCollectionIfMissing([], linkItem, linkItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(linkItem);
        expect(expectedResult).toContain(linkItem2);
      });

      it('should accept null and undefined values', () => {
        const linkItem: ILinkItem = sampleWithRequiredData;
        expectedResult = service.addLinkItemToCollectionIfMissing([], null, linkItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(linkItem);
      });

      it('should return initial array if no LinkItem is added', () => {
        const linkItemCollection: ILinkItem[] = [sampleWithRequiredData];
        expectedResult = service.addLinkItemToCollectionIfMissing(linkItemCollection, undefined, null);
        expect(expectedResult).toEqual(linkItemCollection);
      });
    });

    describe('compareLinkItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLinkItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLinkItem(entity1, entity2);
        const compareResult2 = service.compareLinkItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLinkItem(entity1, entity2);
        const compareResult2 = service.compareLinkItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLinkItem(entity1, entity2);
        const compareResult2 = service.compareLinkItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
