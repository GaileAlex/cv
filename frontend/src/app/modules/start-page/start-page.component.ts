import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { StatisticsService } from "../../service/statistics.service";

@Component({
    selector: 'app-start-page',
    templateUrl: './start-page.component.html',
    styleUrls: ['./start-page.component.css'],

})

export class StartPageComponent implements OnInit {

    constructor(private router: Router, private statisticsService: StatisticsService) {
    }

    ngOnInit() {
    }

    eventCv() {
        this.statisticsService.sentEvent("download CV")
    }
}
