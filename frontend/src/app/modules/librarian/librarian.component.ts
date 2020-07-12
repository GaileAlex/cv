import { Component, OnInit } from '@angular/core';
import { DialogBoxComponent } from '../../components/dialog-box/dialog-box.component';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';

@Component({
    selector: 'app-librarian',
    templateUrl: './librarian.component.html',
    styleUrls: ['./librarian.component.css']
})
export class LibrarianComponent implements OnInit {

    constructor(public dialogService: DialogService) {
    }

    ref: DynamicDialogRef;

    ngOnInit(): void {
        window.scrollTo(0, 0);
    }

    show() {
        this.ref = this.dialogService.open(DialogBoxComponent, {
            width: '60%',
            height: '80%',
            contentStyle: {'max-height': '350px', overflow: 'auto'}
        });
    }
}
