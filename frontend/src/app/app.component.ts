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
    private SCROLL_COUNT: number = 150;
    title = 'frontend';
    errorMessage = '';
    sessionId: string;
    condition: number = 0;

    constructor(private languageService: LanguageService, private userDataService: UserDataService,
                private statisticsService: StatisticsService) {
        languageService.setDefault();

        this.statisticsService.userSpy()
    }

    @HostListener('window:beforeunload', ['$event'])
    beforeUnloadHandler(event) {
        this.statisticsService.userOut();
    }

    @HostListener("window:scroll", []) scrolling() {
        if (this.conditions() === this.SCROLL_COUNT) {
            this.statisticsService.sentEvent("scroll");
        }
    }

    /*@HostListener('click') clicking() {
        this.statisticsService.sentEvent("click");
    }*/

    conditions() {
        this.condition++;
        if (this.condition > this.SCROLL_COUNT) {
            this.condition = 0;
        }
        return this.condition;
    }

}
