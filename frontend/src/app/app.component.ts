import { Component, HostListener } from '@angular/core';
import { LanguageService } from './service/language.service';
import { StatisticsService } from "./service/statistics.service";
import { UserDataService } from "./service/user-data.service";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})

export class AppComponent {
    title = 'frontend';
    errorMessage = '';
    sessionId: string;

    constructor(private languageService: LanguageService, private userDataService: UserDataService,
                private statisticsService: StatisticsService) {
        languageService.setDefault();

        this.statisticsService.userSpy()


    }

    @HostListener('window:beforeunload', ['$event'])
    beforeUnloadHandler(event) {
        this.statisticsService.userOut().subscribe(data => {
        }, error => {
            this.errorMessage = error;
        });
    }

    @HostListener('window:unload', ['$event'])
    unloadHandler(event) {
        this.statisticsService.userOut().subscribe(data => {
        }, error => {
            this.errorMessage = error;
        });
    }

}
