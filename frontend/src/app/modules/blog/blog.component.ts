import {Component, OnInit} from '@angular/core';
import {BlogService} from '../../service/blog.service';

@Component({
  selector: 'app-blog',
  templateUrl: './blog.component.html',
  styleUrls: ['./blog.component.css']
})
export class BlogComponent implements OnInit {
  books: string;

  constructor(private blogService: BlogService) {
  }

  ngOnInit() {
      window.scrollTo(0, 0);
  }
}
