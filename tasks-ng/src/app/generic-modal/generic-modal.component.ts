import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-generic-modal',
  templateUrl: './generic-modal.component.html',
  styleUrls: ['./generic-modal.component.css']
})
export class GenericModalComponent implements OnInit {
  title?: string;
  content: any;

  constructor(public activeModal: NgbActiveModal) {
  }

  ngOnInit(): void {
    if (!this.activeModal) {
      console.error('Active modal is undefined');
    }
  }

  public onCancel(): void {
    this.activeModal.dismiss();
  }

  public onConfirm(): void {
    this.activeModal.close()
  }
}
