import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBlogItem } from '../blog-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../blog-item.test-samples';

import { BlogItemService, RestBlogItem } from './blog-item.service';

const requireRestSample: RestBlogItem = {
  ...sampleWithRequiredData,
  publishDate: sampleWithRequiredData.publishDate?.toJSON(),
  approvedDate: sampleWithRequiredData.approvedDate?.toJSON(),
};

describe('BlogItem Service', () => {
  let service: BlogItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IBlogItem | IBlogItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BlogItemService);
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

    it('should create a BlogItem', () => {
      const blogItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(blogItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BlogItem', () => {
      const blogItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(blogItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BlogItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BlogItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BlogItem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBlogItemToCollectionIfMissing', () => {
      it('should add a BlogItem to an empty array', () => {
        const blogItem: IBlogItem = sampleWithRequiredData;
        expectedResult = service.addBlogItemToCollectionIfMissing([], blogItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(blogItem);
      });

      it('should not add a BlogItem to an array that contains it', () => {
        const blogItem: IBlogItem = sampleWithRequiredData;
        const blogItemCollection: IBlogItem[] = [
          {
            ...blogItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBlogItemToCollectionIfMissing(blogItemCollection, blogItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BlogItem to an array that doesn't contain it", () => {
        const blogItem: IBlogItem = sampleWithRequiredData;
        const blogItemCollection: IBlogItem[] = [sampleWithPartialData];
        expectedResult = service.addBlogItemToCollectionIfMissing(blogItemCollection, blogItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(blogItem);
      });

      it('should add only unique BlogItem to an array', () => {
        const blogItemArray: IBlogItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const blogItemCollection: IBlogItem[] = [sampleWithRequiredData];
        expectedResult = service.addBlogItemToCollectionIfMissing(blogItemCollection, ...blogItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const blogItem: IBlogItem = sampleWithRequiredData;
        const blogItem2: IBlogItem = sampleWithPartialData;
        expectedResult = service.addBlogItemToCollectionIfMissing([], blogItem, blogItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(blogItem);
        expect(expectedResult).toContain(blogItem2);
      });

      it('should accept null and undefined values', () => {
        const blogItem: IBlogItem = sampleWithRequiredData;
        expectedResult = service.addBlogItemToCollectionIfMissing([], null, blogItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(blogItem);
      });

      it('should return initial array if no BlogItem is added', () => {
        const blogItemCollection: IBlogItem[] = [sampleWithRequiredData];
        expectedResult = service.addBlogItemToCollectionIfMissing(blogItemCollection, undefined, null);
        expect(expectedResult).toEqual(blogItemCollection);
      });
    });

    describe('compareBlogItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBlogItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBlogItem(entity1, entity2);
        const compareResult2 = service.compareBlogItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBlogItem(entity1, entity2);
        const compareResult2 = service.compareBlogItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBlogItem(entity1, entity2);
        const compareResult2 = service.compareBlogItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
