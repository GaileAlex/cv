import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule, routes} from './app-routing.module';
import {AppComponent} from './app.component';
import {FooterComponent} from './components/footer/footer.component';
import {StartPageComponent} from './modules/start-page/start-page.component';
import {MenuComponent} from './components/menu/menu.component';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {NotFoundComponent} from './modules/not-found/not-found.component';
import {MindlyComponent} from './modules/mindly/mindly.component';
import {FormControlValidationMsgDirective} from './components/form-validation/formcontrol-validation-msg.directive';
import {FormSubmitValidationMsgDirective} from './components/form-validation/formsubmit-validation-msg.directive';
import {ConfirmationService} from 'primeng/api';
import {PortfolioService} from './service/portfolio.service';
import {ValidationMsgService} from './components/form-validation/validation-msg.service';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ButtonModule} from 'primeng/button';
import {TableModule} from 'primeng/table';
import {InputMaskModule} from 'primeng/inputmask';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {defineLocale, enGbLocale, ruLocale} from 'ngx-bootstrap/chronos';
import {BooksComponent} from './modules/books/books.component';
import {LibrarianComponent} from './modules/librarian/librarian.component';
import {NgbActiveModal, NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {ComboboxComponent} from './components/combo-box/combo-box.component';
import {DialogBoxComponent} from './components/dialog-box/dialog-box.component';
import {BooksService} from './service/books.service';
import {
  MatButtonModule,
  MatDialogModule,
  MatFormFieldModule,
  MatInputModule,
  MatNativeDateModule
} from '@angular/material';
import {DialogModule} from 'primeng/dialog';
import {InputTextModule} from 'primeng/inputtext';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {MessagesModule} from 'primeng/messages';
import {MessageModule} from 'primeng/message';
import {RouterModule} from '@angular/router';
import {LightboxModule} from 'primeng/lightbox';
import {LoginComponent} from './modules/login/login.component';
import {RegistrationComponent} from './modules/registration/registration.component';
import {BlogArticleComponent} from './modules/blog-article/blog-article.component';
import {BlogComponent} from './modules/blog/blog.component';
import {BlogAdminComponent} from './modules/blog-admin/blog-admin.component';
import {BooksDataService} from './service/booksData';
import {JwtInterceptor} from './service/auth/jwt.interceptor';
import {ErrorInterceptor} from './service/auth/error.interceptor';
import {StickyNavModule} from 'ng2-sticky-nav';
import {NgxPageScrollCoreModule} from 'ngx-page-scroll-core';
import {NgxPageScrollModule} from 'ngx-page-scroll';
import {CalendarModule} from "primeng/calendar";
import {DropdownModule, TooltipModule} from "primeng/primeng";

defineLocale('ru', ruLocale);
defineLocale('en', enGbLocale);

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    AppComponent,
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
    ComboboxComponent,
    LoginComponent,
    RegistrationComponent,
    BlogArticleComponent,
    BlogComponent,
    BlogAdminComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    NgxPageScrollCoreModule,
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
    NgbModule,
    LightboxModule,
    StickyNavModule,
    NgxPageScrollModule,
    CalendarModule,
    TooltipModule,
    DropdownModule
  ],
  providers: [ConfirmationService, PortfolioService, ValidationMsgService, BooksService, NgbActiveModal,
    BooksDataService,
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}],
  bootstrap: [AppComponent],
  entryComponents: [DialogBoxComponent]
})
export class AppModule {
}


