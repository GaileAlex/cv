import {ActivatedRoute, Router} from "@angular/router";
import {Component, OnInit} from "@angular/core";
import {ConfirmationService, Message} from "primeng/api";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Portfolio} from "../../models/portfolio";
import {PortfolioService} from "../../service/portfolio.service";

@Component({
  selector: 'mindly',
  templateUrl: './mindly.component.html',
  styleUrls: ['./mindly.component.css']
})

export class MindlyComponent implements OnInit {

  inputForm: FormGroup;
  portfolio: Portfolio[];
  msgs: Message[] = [];
  portfolioObject: Portfolio;

  constructor(private route: ActivatedRoute, private router: Router, private confirmationService: ConfirmationService,
              private  portfolioService: PortfolioService, private formBuilder: FormBuilder) {

    this.portfolioObject = new Portfolio();
  }

  ngOnInit() {
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
  confirm(portfolioItem: Portfolio) {
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
   * @param portfolioItem
   */
  deletePortfolio(portfolioItem: Portfolio): void {
    this.portfolioService.deletePortfolio(portfolioItem)
      .subscribe(data => {
        this.portfolio = this.portfolio.filter(u => u !== portfolioItem);
      })
  };

  /**
   * saving a new item Portfolio
   */
  onSubmit() {
    this.portfolioObject = this.inputForm.value;
    if (this.inputForm.valid) {
      this.portfolioService.save(this.portfolioObject).subscribe(result => this.ngOnInit());
      console.log('form submitted');
    }
  }
}

