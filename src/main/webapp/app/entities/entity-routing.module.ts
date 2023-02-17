import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'course',
        data: { pageTitle: 'opnLmsApp.course.home.title' },
        loadChildren: () => import('./course/course.module').then(m => m.CourseModule),
      },
      {
        path: 'enrollment',
        data: { pageTitle: 'opnLmsApp.enrollment.home.title' },
        loadChildren: () => import('./enrollment/enrollment.module').then(m => m.EnrollmentModule),
      },
      {
        path: 'section',
        data: { pageTitle: 'opnLmsApp.section.home.title' },
        loadChildren: () => import('./section/section.module').then(m => m.SectionModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'opnLmsApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'sub-category',
        data: { pageTitle: 'opnLmsApp.subCategory.home.title' },
        loadChildren: () => import('./sub-category/sub-category.module').then(m => m.SubCategoryModule),
      },
      {
        path: 'author',
        data: { pageTitle: 'opnLmsApp.author.home.title' },
        loadChildren: () => import('./author/author.module').then(m => m.AuthorModule),
      },
      {
        path: 'assignment',
        data: { pageTitle: 'opnLmsApp.assignment.home.title' },
        loadChildren: () => import('./assignment/assignment.module').then(m => m.AssignmentModule),
      },
      {
        path: 'assessment',
        data: { pageTitle: 'opnLmsApp.assessment.home.title' },
        loadChildren: () => import('./assessment/assessment.module').then(m => m.AssessmentModule),
      },
      {
        path: 'quizz',
        data: { pageTitle: 'opnLmsApp.quizz.home.title' },
        loadChildren: () => import('./quizz/quizz.module').then(m => m.QuizzModule),
      },
      {
        path: 'question',
        data: { pageTitle: 'opnLmsApp.question.home.title' },
        loadChildren: () => import('./question/question.module').then(m => m.QuestionModule),
      },
      {
        path: 'option',
        data: { pageTitle: 'opnLmsApp.option.home.title' },
        loadChildren: () => import('./option/option.module').then(m => m.OptionModule),
      },
      {
        path: 'organisation',
        data: { pageTitle: 'opnLmsApp.organisation.home.title' },
        loadChildren: () => import('./organisation/organisation.module').then(m => m.OrganisationModule),
      },
      {
        path: 'rating',
        data: { pageTitle: 'opnLmsApp.rating.home.title' },
        loadChildren: () => import('./rating/rating.module').then(m => m.RatingModule),
      },
      {
        path: 'instructor',
        data: { pageTitle: 'opnLmsApp.instructor.home.title' },
        loadChildren: () => import('./instructor/instructor.module').then(m => m.InstructorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
