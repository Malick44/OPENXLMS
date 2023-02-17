import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EnrollmentService } from '../service/enrollment.service';

import { EnrollmentComponent } from './enrollment.component';

describe('Enrollment Management Component', () => {
  let comp: EnrollmentComponent;
  let fixture: ComponentFixture<EnrollmentComponent>;
  let service: EnrollmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'enrollment', component: EnrollmentComponent }]), HttpClientTestingModule],
      declarations: [EnrollmentComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(EnrollmentComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EnrollmentComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EnrollmentService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.enrollments?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to enrollmentService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getEnrollmentIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getEnrollmentIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
