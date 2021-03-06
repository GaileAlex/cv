import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
    providedIn: 'root'
})
export class LanguageService {

    // default language
    private language = 'en';

    constructor(private translateService: TranslateService) {
    }

    setDefault() {
        this.translateService.setDefaultLang(this.language);
    }

    getLanguage() {
        return this.language;
    }

    onLanguageChange() {
        return this.translateService.onLangChange;
    }

    setLanguage(lang: string) {
        this.language = lang;
        this.translateService.use(lang);
    }
}
