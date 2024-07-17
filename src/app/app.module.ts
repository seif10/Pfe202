import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LogsComponent } from './logs/logs.component';
import { NgChartsModule } from 'ng2-charts';
import { SystemDataService } from './system-data.service';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    NgChartsModule,
    
  ],
  providers: [SystemDataService],
  bootstrap: [] // Bootstrap AppComponent as the root component
})
export class AppModule { }
