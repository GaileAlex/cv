import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StartPageComponent } from './modules/start-page/start-page.component';
import { NotFoundComponent } from './modules/not-found/not-found.component';
import { MindlyComponent } from './modules/mindly/mindly.component';
import { BlogListComponent } from './modules/blog/blog-list/blog-list.component';
import { LoginComponent } from './modules/login/login.component';
import { RegistrationComponent } from './modules/registration/registration.component';
import { ProtectedGuardService } from './service/protectedGuard';
import { BlogAdminComponent } from './modules/admin-menu/blog-admin/blog-admin.component';
import { VisitStatisticsComponent } from './modules/admin-menu/visit-statistics/visit-statistics.component';
import { ProxyListComponent } from "./modules/proxy-list/proxy-list.component";


export const routes: Routes = [
    {path: '', component: StartPageComponent},
    {path: 'mindly', component: MindlyComponent},
    {path: 'proxy-list', component: ProxyListComponent},
    {path: 'blog', component: BlogListComponent},
    {path: 'login', component: LoginComponent},
    {path: 'registration', component: RegistrationComponent},
    {path: 'blog-admin', component: BlogAdminComponent, canActivate: [ProtectedGuardService]},
    {path: 'statistics', component: VisitStatisticsComponent},
    {path: '**', component: NotFoundComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
