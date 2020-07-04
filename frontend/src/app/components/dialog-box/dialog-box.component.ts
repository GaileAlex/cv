import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { BooksService } from '../../service/books.service';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { BooksDataService } from '../../service/booksData';

@Component({
    selector: 'app-dialog-box',
    templateUrl: './dialog-box.component.html',
    styleUrls: ['./dialog-box.component.css']
})
export class DialogBoxComponent implements OnInit {
    formFilter: FormGroup;

    constructor(private formBuilder: FormBuilder, private bookService: BooksService,
                private router: Router, private activeModal: NgbActiveModal, private books: BooksDataService) {
    }

    ngOnInit(): void {
        this.formFilter = this.formBuilder.group({});

        this.formFilter.addControl('condition', new FormControl('allConditions'));
    }

    formInitialized(name: string, form: FormGroup) {
        this.formFilter.setControl(name, form);
    }

    onSubmit() {
        this.bookService.applyFilter(this.formFilter.controls.filterForm.value, this.formFilter.controls.condition.value).subscribe(
            () => {
                this.books.booksData = this.bookService.applyFilter(this.formFilter.controls.filterForm.value,
                    this.formFilter.controls.condition.value);
                this.router.navigate(['/books', 'filter']);
                this.closeClick();
            },
            () => {
                this.router.navigate(['/books', 'filter']);
                this.closeClick();
            });
    }

    findAll() {
        this.bookService.findAll().subscribe(
            () => {
                this.router.navigate(['books', 'all']);
                this.closeClick();
            },
            () => {
                this.router.navigate(['books', 'all']);
                this.closeClick();
            });
    }

    closeClick(): void {
        this.activeModal.close();
    }
}
