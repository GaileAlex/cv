import {Component, OnInit} from '@angular/core';
import {Blog} from "../../models/blog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-blog-admin',
  templateUrl: './blog-admin.component.html',
  styleUrls: ['./blog-admin.component.css']
})
export class BlogAdminComponent implements OnInit {
  blog: Blog[];
  inputForm: FormGroup;

  constructor(private route: ActivatedRoute, private router: Router, private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.inputForm = this.formBuilder.group({
      id:[''],
      headline: ['', Validators.required],
      article: ['', Validators.required],
      image: ['', Validators.required],
      date: [''],
    });
  }

}
