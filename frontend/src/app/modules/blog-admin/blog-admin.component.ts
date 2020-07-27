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
            headline: ['', Validators.required],
            article: ['', Validators.required],
            image: ['', Validators.required]
        });
    }

    onFileSelect(event) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];

            this.inputForm.get('image').setValue(file);
        }
    }

    onSubmit() {
        const formData = new FormData();
        formData.append('image', this.inputForm.get('image').value);
        formData.append('headline', this.inputForm.get('headline').value);
        formData.append('article', this.inputForm.get('article').value);
        this.blogAdminService.save(formData)
            .subscribe(() => this.ngOnInit());
    }

}
