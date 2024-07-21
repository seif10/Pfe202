import { Component, ViewChild } from '@angular/core';
import { User } from '../model/user.model';
import { UserService } from '../user.service';
import { MatSort } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-show-users',
  standalone: true,
  imports: [CommonModule,MatTableModule,MatDialogModule,MatPaginatorModule,MatIconModule],
  templateUrl: './show-users.component.html',
  styleUrl: './show-users.component.css'
})
export class ShowUsersComponent {
  displayedColumns: string[] = ['id', 'username', 'email', 'firstName', 'lastName', 'delete'];
  dataSource: MatTableDataSource<User>;
  showUsers: boolean = true;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private userService: UserService ,  private router: Router) {
    this.dataSource = new MatTableDataSource<User>();
  }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getAllUsers().subscribe((users: User[]) => {
      this.dataSource.data = users;
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  toggleUsers(): void {
    this.showUsers = !this.showUsers;
  }

  deleteUser(username: string): void {
    this.userService.deleteUser(username).subscribe(
      () => {
        // Update data source after deletion
        this.dataSource.data = this.dataSource.data.filter(user => user.username !== username);
      },
      error => {
        console.error('Error deleting user:', error);
      }
    );
  }

  navigateToAddUser(): void {
    this.router.navigate(['/adduser']);
  }
  
}
