import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Status} from "../../Status";
import {Observable} from "rxjs";
import {UserDTO} from "../../interfaces/UserDTO";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-search-task',
  templateUrl: './search-task.component.html',
  styleUrls: ['./search-task.component.css']
})
export class SearchTaskComponent implements OnInit {
  @Output() search = new EventEmitter<FormGroup>();
  searchForm: FormGroup;
  statuses = Object.values(Status)
  users?: Observable<UserDTO[]>;

  constructor(private formBuilder: FormBuilder, private userService: UserService) {
    this.searchForm = this.formBuilder.group({
      assignedTo: undefined,
      dueDate: undefined,
      status: undefined,
      subject: undefined
    });
  }

  ngOnInit(): void {
    this.users = this.userService.getUsernames();
  }

  onSubmit(event: Event) {
    event.preventDefault();
    this.search.emit(this.searchForm);
  }

  onReset() {
    this.searchForm.reset()
  }
}
