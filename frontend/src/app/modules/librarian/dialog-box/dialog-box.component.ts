import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { LibrarianService } from '../../../service/librarian.service';
import { Router } from '@angular/router';
import { BooksDataService } from '../../../service/booksData';
import { DialogService } from 'primeng/dynamicdialog';

@Component({
    selector: 'app-dialog-box',
    templateUrl: './dialog-box.component.html',
    styleUrls: ['./dialog-box.component.css']
})
export class DialogBoxComponent implements OnInit {
    formFilter: FormGroup;

    constructor(private formBuilder: FormBuilder, private bookService: LibrarianService,
                private router: Router, private dialogService: DialogService, private books: BooksDataService) {
    }

    ngOnInit(): void {
        this.formFilter = this.formBuilder.group({});

        this.formFilter.addControl('condition', new FormControl('allConditions'));
    }

    formInitialized(name: string, form: FormGroup) {
        this.formFilter.setControl(name, form);
    }

    onSubmit() {
        this.books.booksData = this.bookService.applyFilter(this.formFilter.controls.filterForm.value,
            this.formFilter.controls.condition.value);
        this.router.navigate(['/books', 'filter']);
        this.closeClick();
    }

    findAll() {
        this.router.navigate(['books', 'all']);
        this.closeClick();
    }

    closeClick(): void {
        this.dialogService.dialogComponentRef.destroy();
    }
}
