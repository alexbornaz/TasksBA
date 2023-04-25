import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MyTasksComponent} from './my-tasks/my-tasks.component';
import {RouterModule, Routes} from "@angular/router";


const routes: Routes = [
  {path: "", component: MyTasksComponent}
]

@NgModule({
  declarations: [
    MyTasksComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  exports: [
    MyTasksComponent
  ],
})
export class TasksModule {
}
