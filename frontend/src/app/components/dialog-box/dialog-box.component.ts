import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {BooksService} from "../../service/books.service";
import {Router} from "@angular/router";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-dialog-box',
  templateUrl: './dialog-box.component.html',
  styleUrls: ['./dialog-box.component.css']
})
export class DialogBoxComponent implements OnInit {

  formFilter: FormGroup;

  constructor(private formBuilder: FormBuilder, private bookService: BooksService,
              private router: Router, private activeModal: NgbActiveModal) {
  }

  ngOnInit(): void {
    this.formFilter = this.formBuilder.group({});

    this.formFilter.addControl('condition', new FormControl('allConditions'));
  }

  formInitialized(name: string, form: FormGroup) {
    this.formFilter.setControl(name, form);
  }

  onSubmit() {

    this.bookService.save(this.formFilter.controls['filterForm'].value, this.formFilter.controls['condition'].value)
      .subscribe(
        () => {
          this.router.navigate(['books']);
          this.closeClick();
        },
        () => {
          this.router.navigate(['books']);
          this.closeClick();
        });
  }

  closeClick(): void {
    this.activeModal.close();
  }
}
