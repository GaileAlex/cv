import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule, routes} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {StartPageComponent} from './modules/start-page/start-page.component';
import {MenuComponent} from './components/menu/menu.component';
import {LightboxModule} from "ngx-lightbox";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {NotFoundComponent} from './modules/not-found/not-found.component';
import {MindlyComponent} from './modules/mindly/mindly.component';
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
import {BooksComponent} from './modules/books/books.component';
import {LibrarianComponent} from './modules/librarian/librarian.component';
import {NgbActiveModal, NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {ComboboxComponent} from './components/combo-box/combo-box.component';
import {DialogBoxComponent} from './components/dialog-box/dialog-box.component';
import {BooksService} from "./service/books.service";
import {
  MatButtonModule,
  MatDialogModule,
  MatFormFieldModule,
  MatInputModule,
  MatNativeDateModule
} from "@angular/material";
import {DialogModule} from "primeng/dialog";
import {InputTextModule} from "primeng/inputtext";
import {InputTextareaModule} from "primeng/inputtextarea";
import {MessagesModule} from "primeng/messages";
import {MessageModule} from "primeng/message";
import {RouterModule} from "@angular/router";

defineLocale('ru', ruLocale);
defineLocale('en', enGbLocale);

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

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
    DialogBoxComponent,
    ComboboxComponent
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
    NgbModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    DialogModule,
    InputTextModule,
    InputTextareaModule,
    MessagesModule,
    MessageModule,
    FormsModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatDialogModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes),
    NgbModule
  ],
  providers: [ConfirmationService, PortfolioService, ValidationMsgService, BooksService, NgbActiveModal],
  bootstrap: [AppComponent],
  entryComponents: [DialogBoxComponent]
})
export class AppModule {
}


