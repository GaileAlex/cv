import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Books } from '../../../models/books';
import { LibrarianService } from '../../../service/librarian.service';
import { BooksDataService } from '../../../service/booksData';

@Component({
    selector: 'app-books',
    templateUrl: './books.component.html',
    styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {
    errorMessage = '';
    books: Books[];

    constructor(private route: ActivatedRoute, private router: Router, private  booksService: LibrarianService,
                private booksData: BooksDataService) {
    }

    ngOnInit() {
        this.route.params.subscribe(params => {
            if (params.param === 'filter') {
                this.booksData.booksData.subscribe(data => {
                    this.books = data;
                }, error => {
                    this.errorMessage = error;
                });
            }
            if (params.param === 'all') {
                this.booksService.findAll().subscribe(data => {
                    this.books = data;
                }, error => {
                    this.errorMessage = error;
                    console.log(this.errorMessage)
                });
            }
        });
    }
}
