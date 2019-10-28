import { Component, OnInit } from '@angular/core';
import {TranslateService} from "@ngx-translate/core";
import {LanguageService} from "../../service/language.service";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  constructor(private translate: TranslateService, private languageService: LanguageService){

  }

  ngOnInit() {
    this.translate.setDefaultLang(this.languageService.getLanguage());

  }

  getLanguage(){
    return this.translate;
  }

}
