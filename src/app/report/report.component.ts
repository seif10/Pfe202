import { Component } from '@angular/core';
import { LogReportService } from '../log-report.service';
import { FormsModule, NgModel } from '@angular/forms';
import { CommonModule, NgIf } from '@angular/common';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatOption, MatSelect } from '@angular/material/select';
import { ReportDialogComponent } from '../report-dialog/report-dialog.component';
import { EmptyThreadComponent } from '../empty-thread/empty-thread.component';

@Component({
  selector: 'app-report',
  standalone: true,
  imports: [FormsModule,CommonModule,MatDialogModule,MatFormField,MatLabel,MatSelect,MatOption],
  templateUrl: './report.component.html',
  styleUrl: './report.component.css'
})
export class ReportComponent {

  selectedComponent!: string;
  components: string[] = [
    'SIBJMSRAThreadPool', 
    'Thread-',
    'SwiftInProdCor', 
    'server.startup',
    'Default',
    'WebContainer',
    'Palmyra Scheduler Thread',
    'Palmyra Work Jobs Loader Thread'
  ];
  reportResult!: string;

  constructor(
    private logReportService: LogReportService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    
  }

  exportReport(): void {
    if (!this.selectedComponent) {
      this.dialog.open(EmptyThreadComponent);
      return;
    }

    this.openExportDialog();
    
    this.logReportService.exportLogReport(this.selectedComponent).subscribe(
      (result: string) => {
        this.reportResult = result;
        console.log('Report generated:', result);
      },
      (error) => {
        console.error('Error generating report', error);
      }
    );
    
  }

  openExportDialog(): void {
    const dialogRef = this.dialog.open(ReportDialogComponent, {
      data: { selectedComponent: this.selectedComponent }
    });
  
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      // Handle actions after dialog closes if needed
    });
  }
  
}
