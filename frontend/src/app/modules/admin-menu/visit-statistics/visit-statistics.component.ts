import { Component, OnInit, ViewChild } from '@angular/core';
import { StatisticsService } from "../../../service/statistics.service";
import { Statistic } from "../../../models/statistic";
import { BaseChartDirective } from "ng2-charts";
import { ChartsModule } from 'ng2-charts';

@Component({
    selector: 'app-visit-statistics',
    templateUrl: './visit-statistics.component.html',
    styleUrls: ['./visit-statistics.component.css']
})
export class VisitStatisticsComponent implements OnInit {

    public SystemName: string = "MF1";
    firstCopy = false;
    time = "2018-01-29 10:08:30";
    indice;
    trovato = false;


    // data
    public lineChartData: Array<number> = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60];

    public lineChartDataBackup: Array<number> = [];

    public labelMFL: Array<any> = [
        {
            data: this.lineChartData,
            label: this.SystemName
        }
    ];
    // labels
    public lineChartLabels: Array<any> = ["2018-01-29 10:00:00", "2018-01-29 10:01:00", " 2018-01-29 10:02:00", " 2018-01-29 10:03:00", " 2018-01-29 10:04:00", " 2018-01-29 10:05:00", "2018-01-29 10:06:00", "2018-01-29 10:07:00", "2018-01-29 10:08:00", "2018-01-29 10:09:00", "2018-01-29 10:10:00", "2018-01-29 10:11:00", "2018-01-29 10:12:00", "2018-01-29 10:13:00", "2018-01-29 10:14:00", "2018-01-29 10:15:00", "2018-01-29 10:16:00", "2018-01-29 10:17:00", "2018-01-29 10:18:00", "2018-01-29 10:19:00", "2018-01-29 10:20:00", "2018-01-29 10:21:00", "2018-01-29 10:22:00", "2018-01-29 10:23:00", "2018-01-29 10:24:00", "2018-01-29 10:25:00", "2018-01-29 10:26:00", "2018-01-29 10:27:00", "2018-01-29 10:28:00", "2018-01-29 10:29:00", "2018-01-29 10:30:00", "2018-01-29 10:31:00", "2018-01-29 10:32:00", "2018-01-29 10:33:00", "2018-01-29 10:34:00", "2018-01-29 10:35:00", "2018-01-29 10:36:00", "2018-01-29 10:37:00", "2018-01-29 10:38:00", "2018-01-29 10:39:00", "2018-01-29 10:40:00", "2018-01-29 10:41:00", "2018-01-29 10:42:00", "2018-01-29 10:43:00", "2018-01-29 10:44:00", "2018-01-29 10:45:00", "2018-01-29 10:46:00", "2018-01-29 10:47:00", "2018-01-29 10:48:00", "2018-01-29 10:49:00", "2018-01-29 10:49:59", "2018-01-29 10:51:00", "2018-01-29 10:52:00", "2018-01-29 10:53:00", "2018-01-29 10:54:00", "2018-01-29 10:55:00", "2018-01-29 10:56:00", "2018-01-29 10:57:00", "2018-01-29 10:58:00", "2018-01-29 10:59:00"];
    public lineChartLabelsBackup: Array<any> = [];

    constructor() {
    }


    // array opzioni grafico
    public lineChartOptions: any = {
        responsive: true
    };

    // array colori grafico
    public lineChartColors: Array<any> = [
        {
            backgroundColor: 'rgba(148,159,177,0.2)',
            borderColor: 'rgba(148,159,177,1)',
            pointBackgroundColor: 'rgba(148,159,177,1)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(148,159,177,0.8)'
        }
    ];

    public lineChartType = 'line';

    public chartClicked(e: any): void {
        console.log(e);
    }

    public chartHovered(e: any): void {
        console.log(e);
    }

    ngOnInit() {
    }




}

/*  statistics: Statistic[];
  data: Date;
  chartLabels: Array<{ data: Date }> = [];

  constructor(private statisticsService: StatisticsService) {
  }

  ngOnInit() {
      window.scrollTo(0, 0);

      this.statisticsService.findAll().subscribe(data => {
          this.statistics = data
          console.log(this.statistics)
          console.log(this.statistics[0].lastVisit)
          this.chartLabels.push(new Date(this.statistics[0].lastVisit))
          /!*this.chartData.push(this.statistics[0].lastVisit)
          this.chartData.push(this.statistics[0].lastVisit)
          this.chartData.push(this.statistics[0].lastVisit)*!/
          console.log(this.statistics)
          console.log(this.chartData)
      });

      /!*this.chartData.push(this.statistics[0].lastVisit)
      this.chartData.push(this.statistics[0].lastVisit)
      this.chartData.push(this.statistics[0].lastVisit)
      this.chartData.push(this.statistics[0].lastVisit)*!/

  }


  getCharData() {

  }

  getGraphData() {

      console.log(this.statistics)
      return this.statistics;
  }

  /!* chartData = [
       { data: [330, 600, 260, 700], label: 'Account A' },
       { data: [120, 455, 100, 340], label: 'Account B' },
       { data: [45, 67, 800, 500], label: 'Account C' }
   ];*!/
 /!* chartLabels = ['January', 'February', 'Mars', 'April'];*!/

  chartOptions = {
      responsive: true,
      scales: {
          xAxes: [{
              stacked: true,
              type: 'time',
              time: {
                  unit: 'day',
                  displayFormats: {
                      day: 'dd MM YYYY', // This is the default
                  },
              },
          }], yAxes: [{
              stacked: true,
              ticks: {
                  beginAtZero: true,

              },
          }]
      },
      plugins: {
          datalabels: {
              anchor: 'end',
              align: 'end',
          }
      },
  };

}
*/
