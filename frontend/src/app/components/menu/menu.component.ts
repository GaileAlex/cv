import {AfterViewInit, Component, OnInit} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {LanguageService} from "../../service/language.service";
import {ScrollSpyService} from "ngx-scrollspy";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit, AfterViewInit  {

  constructor(private translate: TranslateService, private languageService: LanguageService,private scrollSpyService: ScrollSpyService) {

  }

  ngOnInit() {
    this.translate.setDefaultLang(this.languageService.getLanguage());

  }
  ngAfterViewInit() {
    this.scrollSpyService.getObservable('test').subscribe((e: any) => {
      console.log('ScrollSpy::test: ', e);
    });
  }

  getLanguage() {
    return this.languageService.getLanguage();
  }

  useLanguage(language: string) {
    this.languageService.setLanguage(language);
  }

}
