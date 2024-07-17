import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { SystemDataService } from '../system-data.service';
import { ParsedLog } from '../model/parsed-log.model';
import { CommonModule } from '@angular/common';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import { LogDetailsComponent } from '../log-details/log-details.component';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { animate, state, style, transition, trigger } from '@angular/animations';



@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css'],
  standalone:true,
  imports:[CommonModule,MatTableModule,MatDialogModule,MatPaginatorModule,MatIconModule],
  animations: [
    trigger('rotateIcon', [
      state('open', style({
        transform: 'rotate(180deg)'
      })),
      state('closed', style({
        transform: 'rotate(0)'
      })),
      transition('open <=> closed', animate('200ms ease-in-out'))
    ])
  ]
})
export class LogsComponent implements AfterViewInit{
  
  logs: ParsedLog[] = [];
  dataSource: MatTableDataSource<ParsedLog>;
  displayedColumns: string[] = ['id', 'date', 'component', 'module', 'process', 'action', 'details'];

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  showLogs = true; // Initial state to show the Grafana dashboards
  iconState = 'open'
  logFiles!: string[];

  constructor(
    private systemDataService: SystemDataService,
    private dialog: MatDialog
  ) {
    this.dataSource = new MatTableDataSource<ParsedLog>();
  }

  ngOnInit(): void {
    //this.loadLogFiles();
    this.getParsedLogs();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  getParsedLogs(): void {
    this.systemDataService.getParsedLogs().subscribe(
      (data) => {
        this.logs = data;
        this.dataSource.data = this.logs;
        this.dataSource.paginator = this.paginator;
      },
      (error) => {
        console.error('Error fetching logs', error);
      }
    );
  }

  openDetails(details: string): void {
    this.dialog.open(LogDetailsComponent, {
      data: { details },
      width: '400px'
    });
  }

  toggleLogs(): void {
    this.showLogs = !this.showLogs; // Toggles the visibility of Grafana dashboards
    this.iconState = this.showLogs ? 'open' : 'closed';
  } 


  loadLogFiles() {
    this.systemDataService.getRemoteLogFilesAndAddOrUpdateZabbixItems().subscribe(
      (data: string[]) => {
        this.logFiles = data;
        console.log('log file data : ',data);
        this.sendLogsDataToDB();      
      },
      (error) => {
        console.error('Error fetching log files', error);
      }
    );
  }

  sendLogsDataToDB() {
    const requestBody = {
      itemNames: this.logFiles, // Replace with actual item names
      limit: 2 // Replace with the desired limit
    };

    this.systemDataService.fetchAndSaveLogsValueByNamesInDB(requestBody).subscribe(
      () => {
        console.log('Logs data successfully sent and saved');
      },
      (error) => {
        console.error('Error sending logs data', error);
      }
    );
  }
}
