import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../service/auth/auth.service';

@Component({
    selector: 'app-start-page',
    templateUrl: './start-page.component.html',
    styleUrls: ['./start-page.component.css'],

})

export class StartPageComponent implements OnInit {

    constructor(private authService: AuthService) {
        window.onbeforeunload = () => {
            localStorage.setItem('isSameSession', JSON.stringify(false));
        };
    }

    ngOnInit() {
        if (localStorage.getItem('isSameSession') === 'false') {
            setTimeout(() => {
                this.authService.userSpy().subscribe();
            }, 2000);
        } else {
            localStorage.setItem('isSameSession', JSON.stringify(true));
        }
    }
}
