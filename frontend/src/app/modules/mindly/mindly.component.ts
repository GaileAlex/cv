import {ActivatedRoute, Router} from '@angular/router';
import {Component, OnInit} from '@angular/core';
import {ConfirmationService, Message} from 'primeng/api';
import {PortfolioService} from '../../service/portfolio.service';
import {Mindly} from '../../models/mindly';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-mindly',
  templateUrl: './mindly.component.html',
  styleUrls: ['./mindly.component.css']
})

export class MindlyComponent implements OnInit {

  inputForm: FormGroup;
  portfolio: Mindly[];
  msgs: Message[] = [];
  portfolioObject: Mindly;
  dateToday: Date;

  constructor(private route: ActivatedRoute, private router: Router, private confirmationService: ConfirmationService,
              private  portfolioService: PortfolioService, private formBuilder: FormBuilder) {

    this.portfolioObject = new Mindly();
  }

  ngOnInit() {
    window.scrollTo(0, 0);
this.dateToday=new Date();


    this.portfolioService.findAll().subscribe(data => {
      this.portfolio = data;
    });


    this.inputForm = this.formBuilder.group({
      cryptocurrency: ['Bitcoin'],
      amount: ['', Validators.required],
      dateOfPurchase: [''],
      walletLocation: ['', Validators.required]
    });
  }

  /**
   * confirmation of deletion and delete request element Portfolio
   * @param portfolioItem
   */
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

  /**
   * delete element Portfolio
   * @param portfolio
   */
  deletePortfolio(portfolio: Mindly): void {
    this.portfolioService.deletePortfolio(portfolio.id)
      .subscribe(data => {
        this.portfolio = this.portfolio.filter(u => u !== portfolio);
      });
  }

  /**
   * saving a new item Portfolio
   */
  onSubmit() {
    this.portfolioObject = this.inputForm.value;
    if (this.inputForm.valid) {
      this.portfolioService.save(this.portfolioObject).subscribe(result => this.ngOnInit());
    }
  }
}
