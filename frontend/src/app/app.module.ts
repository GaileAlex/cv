import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule, routes } from './app-routing.module';
import { AppComponent } from './app.component';
import { FooterComponent } from './components/footer/footer.component';
import { StartPageComponent } from './modules/start-page/start-page.component';
import { MenuComponent } from './components/menu/menu.component';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { NotFoundComponent } from './modules/not-found/not-found.component';
import { FormControlValidationMsgDirective } from './components/form-validation/formcontrol-validation-msg.directive';
import { FormSubmitValidationMsgDirective } from './components/form-validation/formsubmit-validation-msg.directive';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ValidationMsgService } from './components/form-validation/validation-msg.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { InputMaskModule } from 'primeng/inputmask';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { defineLocale, enGbLocale, ruLocale } from 'ngx-bootstrap/chronos';
import { NgbActiveModal, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { RouterModule } from '@angular/router';
import { LightboxModule } from 'primeng/lightbox';
import { LoginComponent } from './modules/login/login.component';
import { RegistrationComponent } from './modules/registration/registration.component';
import { BlogArticleComponent } from './modules/blog/blog-article/blog-article.component';
import { BlogAdminComponent } from './modules/admin-menu/blog-admin/blog-admin.component';
import { JwtInterceptor } from './service/interceptors/jwt.interceptor';
import { ErrorInterceptor } from './service/interceptors/error.interceptor';
import { NgxPageScrollCoreModule } from 'ngx-page-scroll-core';
import { NgxPageScrollModule } from 'ngx-page-scroll';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { TooltipModule } from 'primeng/tooltip';
import { DialogService, DynamicDialogModule } from 'primeng/dynamicdialog';
import { ToastModule } from 'primeng/toast';
import { CommonModule } from '@angular/common';
import { BlogListComponent } from './modules/blog/blog-list/blog-list.component';
import { StickyNavModule } from 'ng2-sticky-nav';
import { VisitStatisticsComponent } from './modules/admin-menu/visit-statistics/visit-statistics.component';
import { ChartsModule, ThemeService } from 'ng2-charts';
import { ProxyListComponent } from "./modules/proxy-list/proxy-list.component";
import { MatTableModule } from "@angular/material/table";
import { MatSortModule } from "@angular/material/sort";
import { MatPaginatorModule } from "@angular/material/paginator";
import { ErrorModalComponent } from "./components/error-modal/error-modal.component";
import { MatButtonModule } from "@angular/material/button";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatNativeDateModule } from "@angular/material/core";
import { StudyEnglishComponent } from "./modules/study-english/study-english.component";
import { ChatComponent } from "./modules/chat/chat.component";

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
        FormControlValidationMsgDirective,
        FormSubmitValidationMsgDirective,
        LoginComponent,
        RegistrationComponent,
        BlogArticleComponent,
        BlogListComponent,
        ChatComponent,
        BlogAdminComponent,
        VisitStatisticsComponent,
        ProxyListComponent,
        StudyEnglishComponent,
        ErrorModalComponent,
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
        ButtonModule,
        TableModule,
        InputMaskModule,
        ConfirmDialogModule,
        DialogModule,
        InputTextModule,
        InputTextareaModule,
        MessagesModule,
        MessageModule,
        FormsModule,
        MatButtonModule,
        MatDialogModule,
        BrowserAnimationsModule,
        MatFormFieldModule,
        MatInputModule,
        MatNativeDateModule,
        ReactiveFormsModule,
        RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' }),
        NgbModule,
        LightboxModule,
        StickyNavModule,
        NgxPageScrollModule,
        CalendarModule,
        TooltipModule,
        DropdownModule,
        DynamicDialogModule,
        ToastModule,
        CommonModule,
        ChartsModule,
        MatTableModule,
        MatSortModule,
        MatPaginatorModule,
    ],
    providers: [ConfirmationService, ValidationMsgService, NgbActiveModal, DialogService, MessageService, ThemeService,
        {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
        {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}],
    bootstrap: [AppComponent]
})
export class AppModule {
}


