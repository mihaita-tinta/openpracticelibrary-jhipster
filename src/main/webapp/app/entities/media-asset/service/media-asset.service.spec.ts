import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMediaAsset } from '../media-asset.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../media-asset.test-samples';

import { MediaAssetService } from './media-asset.service';

const requireRestSample: IMediaAsset = {
  ...sampleWithRequiredData,
};

describe('MediaAsset Service', () => {
  let service: MediaAssetService;
  let httpMock: HttpTestingController;
  let expectedResult: IMediaAsset | IMediaAsset[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MediaAssetService);
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

    it('should create a MediaAsset', () => {
      const mediaAsset = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(mediaAsset).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MediaAsset', () => {
      const mediaAsset = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(mediaAsset).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MediaAsset', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MediaAsset', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MediaAsset', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMediaAssetToCollectionIfMissing', () => {
      it('should add a MediaAsset to an empty array', () => {
        const mediaAsset: IMediaAsset = sampleWithRequiredData;
        expectedResult = service.addMediaAssetToCollectionIfMissing([], mediaAsset);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mediaAsset);
      });

      it('should not add a MediaAsset to an array that contains it', () => {
        const mediaAsset: IMediaAsset = sampleWithRequiredData;
        const mediaAssetCollection: IMediaAsset[] = [
          {
            ...mediaAsset,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMediaAssetToCollectionIfMissing(mediaAssetCollection, mediaAsset);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MediaAsset to an array that doesn't contain it", () => {
        const mediaAsset: IMediaAsset = sampleWithRequiredData;
        const mediaAssetCollection: IMediaAsset[] = [sampleWithPartialData];
        expectedResult = service.addMediaAssetToCollectionIfMissing(mediaAssetCollection, mediaAsset);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mediaAsset);
      });

      it('should add only unique MediaAsset to an array', () => {
        const mediaAssetArray: IMediaAsset[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const mediaAssetCollection: IMediaAsset[] = [sampleWithRequiredData];
        expectedResult = service.addMediaAssetToCollectionIfMissing(mediaAssetCollection, ...mediaAssetArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mediaAsset: IMediaAsset = sampleWithRequiredData;
        const mediaAsset2: IMediaAsset = sampleWithPartialData;
        expectedResult = service.addMediaAssetToCollectionIfMissing([], mediaAsset, mediaAsset2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mediaAsset);
        expect(expectedResult).toContain(mediaAsset2);
      });

      it('should accept null and undefined values', () => {
        const mediaAsset: IMediaAsset = sampleWithRequiredData;
        expectedResult = service.addMediaAssetToCollectionIfMissing([], null, mediaAsset, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mediaAsset);
      });

      it('should return initial array if no MediaAsset is added', () => {
        const mediaAssetCollection: IMediaAsset[] = [sampleWithRequiredData];
        expectedResult = service.addMediaAssetToCollectionIfMissing(mediaAssetCollection, undefined, null);
        expect(expectedResult).toEqual(mediaAssetCollection);
      });
    });

    describe('compareMediaAsset', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMediaAsset(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMediaAsset(entity1, entity2);
        const compareResult2 = service.compareMediaAsset(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMediaAsset(entity1, entity2);
        const compareResult2 = service.compareMediaAsset(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMediaAsset(entity1, entity2);
        const compareResult2 = service.compareMediaAsset(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
