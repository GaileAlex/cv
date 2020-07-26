import { Component, OnInit } from '@angular/core';
import { Blog } from '../../models/blog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BlogAdminService } from '../../service/blog-admin.service';

@Component({
    selector: 'app-blog-admin',
    templateUrl: './blog-admin.component.html',
    styleUrls: ['./blog-admin.component.css']
})
export class BlogAdminComponent implements OnInit {
    blog: Blog;
    inputForm: FormGroup;
    image: File;

    constructor(private route: ActivatedRoute, private router: Router, private formBuilder: FormBuilder,
                private blogAdminService: BlogAdminService) {
    }

    ngOnInit() {
        this.inputForm = this.formBuilder.group({
            id: [''],
            headline: ['', Validators.required],
            article: ['', Validators.required],
            date: [''],
        });
    }

    onSubmit() {
        const formData = new FormData();
        formData.append('image', this.inputForm.value.image);
        this.inputForm.value.addControl(formData);
        this.blogAdminService.save(this.inputForm.value)
            .subscribe(() => this.ngOnInit());
    }

}
