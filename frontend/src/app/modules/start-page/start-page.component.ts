import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth/auth.service';

@Component({
    selector: 'app-start-page',
    templateUrl: './start-page.component.html',
    styleUrls: ['./start-page.component.css'],

})

export class StartPageComponent implements OnInit {

    constructor(private authService: AuthService) {
    }

    ngOnInit() {
        this.authService.userSpy().subscribe();
    }



}
