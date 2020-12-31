import { Component, HostListener } from '@angular/core';
import { LanguageService } from './service/language.service';
import { StatisticsService } from "./service/statistics.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})

export class AppComponent {
    title = 'frontend';
    errorMessage = '';

    constructor(private languageService: LanguageService, private statisticsService: StatisticsService) {
        languageService.setDefault();
    }

    @HostListener('window:beforeunload', ['$event'])
    beforeUnloadHandler(event) {
        this.statisticsService.userOut().subscribe(data => {

        }, error => {
            this.errorMessage = error;
        });

    }

}
