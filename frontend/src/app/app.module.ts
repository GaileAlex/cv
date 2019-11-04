import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { StartPageComponent } from './modules/start-page/start-page.component';
import { MenuComponent } from './components/menu/menu.component';
import {LightboxModule} from "ngx-lightbox";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import { NotFoundComponent } from './modules/not-found/not-found.component';
import { MindlyComponent } from './modules/mindly/mindly.component';
import {FormControlValidationMsgDirective} from "./components/form-validation/formcontrol-validation-msg.directive";
import {FormSubmitValidationMsgDirective} from "./components/form-validation/formsubmit-validation-msg.directive";
import {ConfirmationService} from "primeng/api";
import {PortfolioService} from "./service/portfolio.service";
import {ValidationMsgService} from "./components/form-validation/validation-msg.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import {TableModule} from "primeng/table";
import {InputMaskModule} from "primeng/inputmask";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {defineLocale, enGbLocale, ruLocale} from 'ngx-bootstrap/chronos';
import { BooksComponent } from './modules/books/books.component';
import { LibrarianComponent } from './modules/librarian/librarian.component';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import { ComboBoxComponent } from './components/combo-box/combo-box.component';
import { DialogBoxComponent } from './components/dialog-box/dialog-box.component';

defineLocale('ru', ruLocale);
defineLocale('en', enGbLocale);

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);}

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    StartPageComponent,
    MenuComponent,
    NotFoundComponent,
    MindlyComponent,
    FormControlValidationMsgDirective,
    FormSubmitValidationMsgDirective,
    BooksComponent,
    LibrarianComponent,
    ComboBoxComponent,
    DialogBoxComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    LightboxModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    ReactiveFormsModule,
    FormsModule,
    ButtonModule,
    TableModule,
    InputMaskModule,
    ConfirmDialogModule,
    BrowserAnimationsModule,
    NgbModule
  ],
  providers: [ConfirmationService, PortfolioService, ValidationMsgService],
  bootstrap: [AppComponent]
})
export class AppModule { }


