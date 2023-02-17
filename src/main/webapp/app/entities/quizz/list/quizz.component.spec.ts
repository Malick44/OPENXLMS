import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { QuizzService } from '../service/quizz.service';

import { QuizzComponent } from './quizz.component';

describe('Quizz Management Component', () => {
  let comp: QuizzComponent;
  let fixture: ComponentFixture<QuizzComponent>;
  let service: QuizzService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'quizz', component: QuizzComponent }]), HttpClientTestingModule],
      declarations: [QuizzComponent],
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
      .overrideTemplate(QuizzComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuizzComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(QuizzService);

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
    expect(comp.quizzes?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });

  describe('trackId', () => {
    it('Should forward to quizzService', () => {
      const entity = { id: 'ABC' };
      jest.spyOn(service, 'getQuizzIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getQuizzIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
