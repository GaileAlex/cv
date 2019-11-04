import { Component, OnInit } from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DialogBoxComponent} from "../../components/dialog-box/dialog-box.component";

@Component({
  selector: 'app-librarian',
  templateUrl: './librarian.component.html',
  styleUrls: ['./librarian.component.css']
})
export class LibrarianComponent  {

  constructor(private modalService: NgbModal) {
  }

  openDialog() {
    this.modalService.open(DialogBoxComponent, {size: 'xl'});
  }
}
