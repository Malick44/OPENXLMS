import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AuthorService } from '../service/author.service';

import { AuthorComponent } from './author.component';

describe('Author Management Component', () => {
  let comp: AuthorComponent;
  let fixture: ComponentFixture<AuthorComponent>;
  let service: AuthorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'author', component: AuthorComponent }]), HttpClientTestingModule],
      declarations: [AuthorComponent],
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
      .overrideTemplate(AuthorComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AuthorComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AuthorService);

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
    expect(comp.authors?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to authorService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getAuthorIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAuthorIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
