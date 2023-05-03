import {NgModule} from '@angular/core';
import {CommonModule, Location} from '@angular/common';
import {MyTasksComponent} from './my-tasks/my-tasks.component';
import {RouterModule, Routes} from "@angular/router";
import { TaskDetailComponent } from './task-detail/task-detail.component';
import { AllTasksComponent } from './all-tasks/all-tasks.component';
import {ReactiveFormsModule} from "@angular/forms";
import { NewTaskComponent } from './new-task/new-task.component';
import { SearchTaskComponent } from './search-task/search-task.component';


const routes: Routes = [
  {path:"",redirectTo:"my-tasks", pathMatch: "full"},
  {path: "my-tasks", component: MyTasksComponent},
  {path:"tasks/:id", component:TaskDetailComponent},
  {path:"all-tasks", component:AllTasksComponent}

]

@NgModule({
  declarations: [
    MyTasksComponent,
    TaskDetailComponent,
    AllTasksComponent,
    NewTaskComponent,
    SearchTaskComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    ReactiveFormsModule,
  ],
  exports: [
    MyTasksComponent,
    TaskDetailComponent,
    AllTasksComponent,
    NewTaskComponent,
    SearchTaskComponent,
  ],
})
export class TasksModule {
}
