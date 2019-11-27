import {Component, OnInit} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {LanguageService} from "../../service/language.service";
import {Router} from "@angular/router";

declare function main(): any;

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})

export class MenuComponent implements OnInit {
  pageId: string;

  constructor(private translate: TranslateService, private languageService: LanguageService, private router: Router) {
  }

  ngOnInit() {
    main();
    this.pageId = this.router.routerState.snapshot.url;
   if(this.router.routerState.snapshot.url.startsWith("/#")){
     this.pageId="";
   }
    this.translate.setDefaultLang(this.languageService.getLanguage());
  }

  getLanguage() {
    return this.languageService.getLanguage();
  }

  useLanguage(language: string) {
    this.languageService.setLanguage(language);
  }
}
