import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../author.test-samples';

import { AuthorFormService } from './author-form.service';

describe('Author Form Service', () => {
  let service: AuthorFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthorFormService);
  });

  describe('Service methods', () => {
    describe('createAuthorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAuthorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            email: expect.any(Object),
            password: expect.any(Object),
            createDate: expect.any(Object),
            avatarUrl: expect.any(Object),
            activated: expect.any(Object),
            langKey: expect.any(Object),
            resetDate: expect.any(Object),
            resetKey: expect.any(Object),
            authorities: expect.any(Object),
            createdBy: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            courseId: expect.any(Object),
          })
        );
      });

      it('passing IAuthor should create a new form with FormGroup', () => {
        const formGroup = service.createAuthorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstName: expect.any(Object),
            lastName: expect.any(Object),
            email: expect.any(Object),
            password: expect.any(Object),
            createDate: expect.any(Object),
            avatarUrl: expect.any(Object),
            activated: expect.any(Object),
            langKey: expect.any(Object),
            resetDate: expect.any(Object),
            resetKey: expect.any(Object),
            authorities: expect.any(Object),
            createdBy: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            courseId: expect.any(Object),
          })
        );
      });
    });

    describe('getAuthor', () => {
      it('should return NewAuthor for default Author initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAuthorFormGroup(sampleWithNewData);

        const author = service.getAuthor(formGroup) as any;

        expect(author).toMatchObject(sampleWithNewData);
      });

      it('should return NewAuthor for empty Author initial value', () => {
        const formGroup = service.createAuthorFormGroup();

        const author = service.getAuthor(formGroup) as any;

        expect(author).toMatchObject({});
      });

      it('should return IAuthor', () => {
        const formGroup = service.createAuthorFormGroup(sampleWithRequiredData);

        const author = service.getAuthor(formGroup) as any;

        expect(author).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAuthor should not enable id FormControl', () => {
        const formGroup = service.createAuthorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAuthor should disable id FormControl', () => {
        const formGroup = service.createAuthorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
