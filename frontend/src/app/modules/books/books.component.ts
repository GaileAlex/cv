import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Books} from "../../models/books";
import {BooksService} from "../../service/books.service";
import {BooksDataService} from "../../service/booksData";

@Component({
  selector: 'books',
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {
  books: Books[];

  constructor(private route: ActivatedRoute, private router: Router, private  booksService: BooksService,
              private booksData: BooksDataService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params.param === "filter") {
        this.booksData.booksData.subscribe(data => {
          this.books = data;
        });
      }
      if (params.param === "all") {
        this.booksService.findAll().subscribe(data => {
          this.books = data;
        });
      }
    });
  }
}
