import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LanguageService } from '../../service/language.service';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth/auth.service';
import { UserDataService } from '../../service/user-data.service';
import { StatisticsService } from '../../service/statistics.service';

@Component({
    selector: 'app-menu',
    templateUrl: './menu.component.html',
    styleUrls: ['./menu.component.css']
})

export class MenuComponent implements OnInit {
    collapsed = true;
    pageId: string;
    username: string;
    isLoggedIn: boolean;

    constructor(private translate: TranslateService, private languageService: LanguageService, private router: Router,
                private authService: AuthService, private userDataService: UserDataService, private statisticsService: StatisticsService) {
        this.isLoggedIn = this.userDataService.isAuthenticated();
        this.username = userDataService.getUserName();

        window.onbeforeunload = () => {
            localStorage.setItem('isSameSession', 'false');
        };
    }

    ngOnInit() {
        this.pageId = this.router.routerState.snapshot.url;
        if (this.router.routerState.snapshot.url.startsWith('/#')) {
            this.pageId = '';
        }

        this.translate.setDefaultLang(this.languageService.getLanguage());
        if ('false' === localStorage.getItem('isSameSession')) {
            setTimeout(() => {
                this.statisticsService.userSpy().subscribe();
            }, 1000);
            localStorage.setItem('isSameSession', 'true');
        }
    }

    getLanguage() {
        return this.languageService.getLanguage();
    }

    getUserRole() {
        return this.userDataService.getUserRole();
    }

    useLanguage(language: string) {
        this.languageService.setLanguage(language);
    }

    logout() {
        this.authService.logout();
    }
}
