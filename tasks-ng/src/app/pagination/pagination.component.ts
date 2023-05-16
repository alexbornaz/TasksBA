import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent {

  @Input() totalPages?: number
  @Input() currentPage?: number
  @Output() askedPage = new EventEmitter<number>


  getNumberArray(totalPages: number): number[] {
    return Array(totalPages).fill(0).map((x, i) => i + 1);
  }

  onClick(asked: number) {
    this.currentPage = asked
    this.askedPage.emit(asked)
  }
}
