import {Component, Input} from '@angular/core';
import {Task} from "../../interfaces/Task";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-task-detail',
  templateUrl: './task-detail.component.html',
  styleUrls: ['./task-detail.component.css']
})
export class TaskDetailComponent {
  @Input() task?: Task

  constructor(private route:ActivatedRoute,) {
  }
}
