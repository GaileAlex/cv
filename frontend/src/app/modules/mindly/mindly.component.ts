import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { ConfirmationService, Message, SelectItem } from 'primeng/api';
import { MindlyService } from '../../service/mindly.service';
import { Mindly } from '../../models/mindly';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-mindly',
    templateUrl: './mindly.component.html',
    styleUrls: ['./mindly.component.css']
})

export class MindlyComponent implements OnInit {
    errorMessage = '';
    inputForm: FormGroup;
    portfolio: Mindly[];
    msgs: Message[];
    portfolioObject: Mindly;
    dateToday: Date;
    cryptocurrencyList: SelectItem [];
    cryptocurrencySelect = '';

    constructor(private route: ActivatedRoute, private router: Router, private confirmationService: ConfirmationService,
                private  portfolioService: MindlyService, private formBuilder: FormBuilder) {
    }

    ngOnInit() {
        window.scrollTo(0, 0);
        this.portfolioObject = new Mindly();
        this.cryptocurrencyList = [
            {label: 'Bitcoin', value: 'Bitcoin'},
            {label: 'Ethereum', value: 'Ethereum'},
            {label: 'Ripple', value: 'Ripple'},
        ];
        this.dateToday = new Date();

        this.portfolioService.findAll().subscribe(data => {
            this.portfolio = data;
        }, error => {
            this.errorMessage = error;
        });

        this.inputForm = this.formBuilder.group({
            cryptocurrency: ['Bitcoin'],
            amount: ['', Validators.required],
            dateOfPurchase: [''],
            walletLocation: ['', Validators.required]
        });
    }

    confirm(portfolioItem: Mindly) {
        this.confirmationService.confirm({
            message: 'Do you want to delete this record?',
            header: 'Delete Confirmation',
            icon: 'pi pi-info-circle',
            accept: () => {
                this.msgs = [{severity: 'info', summary: 'Confirmed', detail: 'Record deleted'}];
                this.deletePortfolio(portfolioItem);
            },
            reject: () => {
                this.msgs = [{severity: 'info', summary: 'Rejected', detail: 'You have rejected'}];
            }
        });
    }

    deletePortfolio(portfolio: Mindly): void {
        this.portfolioService.deletePortfolio(portfolio.id)
            .subscribe(() => {
                this.portfolio = this.portfolio.filter(u => u !== portfolio);
            }, error => {
                this.errorMessage = error;
            });
    }

    onSubmit() {
        this.portfolioObject = this.inputForm.value;
        if (this.inputForm.valid) {
            this.portfolioObject.cryptocurrency = this.portfolioObject.cryptocurrency.toString();
            this.portfolioService.save(this.portfolioObject).subscribe(() => this.ngOnInit());
        }
    }
}
