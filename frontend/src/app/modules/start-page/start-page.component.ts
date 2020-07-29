import { Component, OnInit } from '@angular/core';
import { ViewportScroller } from '@angular/common';

@Component({
    selector: 'app-start-page',
    templateUrl: './start-page.component.html',
    styleUrls: ['./start-page.component.css'],

})

export class StartPageComponent implements OnInit {
    constructor(private viewportScroller: ViewportScroller) {
    }

    ngOnInit() {

    }

}
