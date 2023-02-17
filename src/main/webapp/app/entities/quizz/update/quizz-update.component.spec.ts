import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuizzFormService } from './quizz-form.service';
import { QuizzService } from '../service/quizz.service';
import { IQuizz } from '../quizz.model';

import { QuizzUpdateComponent } from './quizz-update.component';

describe('Quizz Management Update Component', () => {
  let comp: QuizzUpdateComponent;
  let fixture: ComponentFixture<QuizzUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let quizzFormService: QuizzFormService;
  let quizzService: QuizzService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuizzUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(QuizzUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuizzUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    quizzFormService = TestBed.inject(QuizzFormService);
    quizzService = TestBed.inject(QuizzService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const quizz: IQuizz = { id: 'CBA' };

      activatedRoute.data = of({ quizz });
      comp.ngOnInit();

      expect(comp.quizz).toEqual(quizz);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizz>>();
      const quizz = { id: 'ABC' };
      jest.spyOn(quizzFormService, 'getQuizz').mockReturnValue(quizz);
      jest.spyOn(quizzService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizz }));
      saveSubject.complete();

      // THEN
      expect(quizzFormService.getQuizz).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(quizzService.update).toHaveBeenCalledWith(expect.objectContaining(quizz));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizz>>();
      const quizz = { id: 'ABC' };
      jest.spyOn(quizzFormService, 'getQuizz').mockReturnValue({ id: null });
      jest.spyOn(quizzService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizz: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: quizz }));
      saveSubject.complete();

      // THEN
      expect(quizzFormService.getQuizz).toHaveBeenCalled();
      expect(quizzService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQuizz>>();
      const quizz = { id: 'ABC' };
      jest.spyOn(quizzService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ quizz });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(quizzService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
