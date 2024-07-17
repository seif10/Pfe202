import { Component } from '@angular/core';
import { MatDialogActions, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-empty-thread',
  standalone: true,
  imports: [MatDialogActions,MatDialogModule],
  templateUrl: './empty-thread.component.html',
  styleUrl: './empty-thread.component.css'
})
export class EmptyThreadComponent {
  constructor(public dialogRef: MatDialogRef<EmptyThreadComponent>) {}

  closeDialog() {
    this.dialogRef.close();
  }
}
