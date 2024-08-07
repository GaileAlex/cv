import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Blog } from '../../../models/blog';
import { Comment } from '../../../models/comment';
import { BlogService } from '../../../service/blog.service';
import { AuthService } from '../../../service/auth/auth.service';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { UserDataService } from '../../../service/user-data.service';

@Component({
    selector: 'app-blog-article',
    templateUrl: './blog-article.component.html',
    styleUrls: ['./blog-article.component.css']
})
export class BlogArticleComponent implements OnInit {
    isLoggedIn: boolean;
    blog: Blog;
    inputForm: UntypedFormGroup;
    comment: Comment;
    comments: Comment[];
    id = this.router.snapshot.params.id;

    constructor(private router: ActivatedRoute, private blogService: BlogService, private authService: AuthService,
                private formBuilder: UntypedFormBuilder, private userDataService: UserDataService) {
    }

    ngOnInit() {
        window.scrollTo(0, 0);
        this.isLoggedIn = this.userDataService.isAuthenticated();
        this.blog = this.getBlogData();

        this.inputForm = this.formBuilder.group({
            comment: ['', Validators.required],
            blogId: [''],
        });
    }

    getBlogData() {
        this.blogService.findBlogById(this.id).subscribe(data => {
            this.blog = data;
        });
        return this.blog;
    }

    onSubmit() {
        this.comment = new Comment();
        this.inputForm.get('blogId').setValue(this.id);
        this.comment = this.inputForm.value;

        this.blogService.saveComment(this.comment).subscribe(() => {
            this.getBlogData();
            this.inputForm.reset();
        });
    }
}
