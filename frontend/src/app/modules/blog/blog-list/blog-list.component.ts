import { Component, OnInit } from '@angular/core';
import { BlogService } from '../../../service/blog.service';
import { Blog } from '../../../models/blog';

@Component({
    selector: 'app-blog-list',
    templateUrl: './blog-list.component.html',
    styleUrls: ['./blog-list.component.css']
})
export class BlogListComponent implements OnInit {
    blogs: Blog[];
    errorMessage = '';

    constructor(private blogService: BlogService) {
    }

    ngOnInit() {
        window.scrollTo(0, 0);

        this.blogService.findAll().subscribe(data => {
            this.blogs = data;
        }, error => {
            this.errorMessage = error;
        });
    }
}
