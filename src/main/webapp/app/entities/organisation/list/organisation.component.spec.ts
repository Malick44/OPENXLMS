import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrganisationService } from '../service/organisation.service';

import { OrganisationComponent } from './organisation.component';

describe('Organisation Management Component', () => {
  let comp: OrganisationComponent;
  let fixture: ComponentFixture<OrganisationComponent>;
  let service: OrganisationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'organisation', component: OrganisationComponent }]), HttpClientTestingModule],
      declarations: [OrganisationComponent],
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
      .overrideTemplate(OrganisationComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrganisationComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrganisationService);

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
    expect(comp.organisations?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to organisationService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getOrganisationIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOrganisationIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
