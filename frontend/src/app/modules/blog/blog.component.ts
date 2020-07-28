import {Component, OnInit} from '@angular/core';
import {BlogService} from '../../service/blog.service';
import {Blog} from "../../models/blog";

@Component({
  selector: 'app-blog',
  templateUrl: './blog.component.html',
  styleUrls: ['./blog.component.css']
})
export class BlogComponent implements OnInit {
  blogs: Blog[];

  constructor(private blogService: BlogService) {
  }

  ngOnInit() {
    window.scrollTo(0, 0);

    this.blogService.findAll().subscribe(data => {
      this.blogs = data;
    });
  }
}
