import { Component, OnInit } from '@angular/core';

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
