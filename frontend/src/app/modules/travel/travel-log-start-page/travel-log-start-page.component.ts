import { Component, OnInit } from '@angular/core';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';

@Component({
    selector: 'app-travel-log-start-page',
    templateUrl: './travel-log-start-page.component.html',
    styleUrls: ['./travel-log-start-page.component.css']
})
export class TravelLogStartPageComponent implements OnInit {
    ref: DynamicDialogRef;

    constructor(public dialogService: DialogService) {
    }

    ngOnInit(): void {
        window.scrollTo(0, 0);
    }


}
