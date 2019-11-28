import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {StartPageComponent} from "./modules/start-page/start-page.component";
import {NotFoundComponent} from "./modules/not-found/not-found.component";
import {MindlyComponent} from "./modules/mindly/mindly.component";
import {LibrarianComponent} from "./modules/librarian/librarian.component";
import {BooksComponent} from "./modules/books/books.component";


export const routes: Routes = [
  {path: '', component: StartPageComponent},
  {path: 'mindly', component: MindlyComponent,},
  {path: 'librarian', component: LibrarianComponent},
  {path: 'books/:param', component: BooksComponent},
  {path: '**', component: NotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
