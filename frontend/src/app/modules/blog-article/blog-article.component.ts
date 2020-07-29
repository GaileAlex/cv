import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { Blog } from "../../models/blog";
import { BlogService } from "../../service/blog.service";

@Component({
    selector: 'app-blog-article',
    templateUrl: './blog-article.component.html',
    styleUrls: ['./blog-article.component.css']
})
export class BlogArticleComponent implements OnInit {
    blog: Blog;

    constructor(private router: ActivatedRoute, private blogService: BlogService) {
    }

    ngOnInit() {
        window.scrollTo(0, 0);
        let id = this.router.snapshot.params["id"];
        this.blogService.findBlogById(id).subscribe(data => {
            this.blog = data;
        });
    }



}
