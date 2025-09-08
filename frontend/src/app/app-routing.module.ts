import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StartPageComponent } from './modules/start-page/start-page.component';
import { NotFoundComponent } from './modules/not-found/not-found.component';
import { BlogListComponent } from './modules/blog/blog-list/blog-list.component';
import { LoginComponent } from './modules/login/login.component';
import { RegistrationComponent } from './modules/registration/registration.component';
import { ProtectedGuardService } from './service/protectedGuard';
import { BlogAdminComponent } from './modules/admin-menu/blog-admin/blog-admin.component';
import { VisitStatisticsComponent } from './modules/admin-menu/visit-statistics/visit-statistics.component';
import { ProxyListComponent } from "./modules/proxy-list/proxy-list.component";
import { BlogArticleComponent } from "./modules/blog/blog-article/blog-article.component";
import { StudyEnglishComponent } from "./modules/study-english/study-english.component";
import { ChatComponent } from "./modules/chat/chat.component";


export const routes: Routes = [
    {path: '', component: StartPageComponent},
    {path: 'proxy-list', component: ProxyListComponent},
    {path: 'study-english', component: StudyEnglishComponent},
    {path: 'blog', component: BlogListComponent},
    {path: 'chat', component: ChatComponent},
    {path: 'article/:id', component: BlogArticleComponent},
    {path: 'login', component: LoginComponent},
    {path: 'registration', component: RegistrationComponent},
    {path: 'blog-admin', component: BlogAdminComponent, canActivate: [ProtectedGuardService]},
    {path: 'statistics', component: VisitStatisticsComponent},
    {path: '**', component: NotFoundComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
