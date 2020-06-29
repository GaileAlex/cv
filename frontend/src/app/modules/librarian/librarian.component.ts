import {Component, OnInit} from '@angular/core';
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {DialogBoxComponent} from "../../components/dialog-box/dialog-box.component";
import {main} from "../../../assets/js/main.js"

declare function main(): any;

@Component({
  selector: 'app-librarian',
  templateUrl: './librarian.component.html',
  styleUrls: ['./librarian.component.css']
})
export class LibrarianComponent implements OnInit {

  constructor(private modalService: NgbModal) {
  }

  ngOnInit(): void {
      main();
    window.scrollTo(0, 0);
  }

  openDialog() {
    this.modalService.open(DialogBoxComponent, {size: 'xl'});
  }
}
