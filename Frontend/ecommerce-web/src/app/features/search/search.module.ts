import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SearchResultsComponent } from './pages/search-results.component';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', component: SearchResultsComponent }
];

@NgModule({
  declarations: [SearchResultsComponent],
  imports: [CommonModule, RouterModule.forChild(routes)],
  exports: [SearchResultsComponent]
})
export class SearchModule {}
