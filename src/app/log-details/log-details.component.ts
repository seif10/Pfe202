import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ParsedLog } from '../model/parsed-log.model';
import { MatDialogModule} from '@angular/material/dialog';


@Component({
  selector: 'app-log-details',
  standalone: true,
  imports: [MatDialogModule],
  templateUrl: './log-details.component.html',
  styleUrl: './log-details.component.css'
})
export class LogDetailsComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { details: string }
  ) {}
}
