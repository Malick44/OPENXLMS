import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { QuizzDetailComponent } from './quizz-detail.component';

describe('Quizz Management Detail Component', () => {
  let comp: QuizzDetailComponent;
  let fixture: ComponentFixture<QuizzDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [QuizzDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ quizz: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(QuizzDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(QuizzDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load quizz on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.quizz).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
