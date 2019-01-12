import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Role} from '../roles/Role';
import {FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-role-dialog',
  templateUrl: './role-dialog.component.html',
  styleUrls: ['./role-dialog.component.css']
})
export class RoleDialogComponent implements OnInit {
  data: Role;
  form: FormGroup;
  parents: String[];
  selectedParent: String;

  constructor(private fb: FormBuilder, private dialogRef: MatDialogRef<RoleDialogComponent>, @Inject(MAT_DIALOG_DATA)  data: Role) {
    this.data = Object.assign({}, data);
    this.selectedParent = this.data.parent;
    this.parents = [this.selectedParent, 'A', 'B'];

  }

  ngOnInit() {
    this.form = this.fb.group({
      roleName: this.data.name
    });
  }

/*  save() {
    // this.dialogRef.close(this.form.value);
    console.log(this.form.value.roleName);
  }*/
  submit(form) {
    // this.dialogRef.close(`${form.value.filename}`);
    console.log(this.form.value.roleName);
  }
}
