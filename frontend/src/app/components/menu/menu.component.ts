import { Component, OnInit } from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {LanguageService} from "../../service/language.service";

declare function main(): any;

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  constructor(private translate: TranslateService, private languageService: LanguageService){

  }

  ngOnInit() {
    //main();
    this.translate.setDefaultLang(this.languageService.getLanguage());

  }

  getLanguage(){
    return this.languageService.getLanguage();
  }

  useLanguage(language: string){
    this.languageService.setLanguage(language);
  }

}
