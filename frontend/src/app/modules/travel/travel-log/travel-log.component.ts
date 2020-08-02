import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfirmationService, Message } from 'primeng/api';
import { Mindly } from '../../../models/mindly';
import { Log } from '../../../models/log';
import { LogService } from '../../../service/log.service';

@Component({
  selector: 'app-travel-log',
  templateUrl: './travel-log.component.html',
  styleUrls: ['./travel-log.component.css']
})
export class TravelLogComponent implements OnInit {

    inputForm: FormGroup;
    log: Log[];
    msgs: Message[];
    portfolioObject: Log;
    dateToday: Date;
    cryptocurrencySelect = 'Bitcoin';

    constructor(private route: ActivatedRoute, private router: Router, private confirmationService: ConfirmationService,
                private  logService: LogService, private formBuilder: FormBuilder) {
    }

    ngOnInit() {
        window.scrollTo(0, 0);
        this.portfolioObject = new Log();
        this.dateToday = new Date();

        this.logService.findAll().subscribe(data => {
            this.log = data;
        });

        this.inputForm = this.formBuilder.group({
            dateLog: [''],
            vrn: ['', Validators.required],
            username: ['', Validators.required],
            odometerBeginning: ['', Validators.required],
            odometerEnd: ['', Validators.required],
            routeLog: ['', Validators.required],
            description: ['', Validators.required]
        });
    }






    onSubmit() {

    }

}
