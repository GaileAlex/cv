import { Component, OnInit } from '@angular/core';
import { StatisticsService } from "../../../service/statistics.service";
import { Statistic } from "../../../models/statistic";

@Component({
    selector: 'app-visit-statistics',
    templateUrl: './visit-statistics.component.html',
    styleUrls: ['./visit-statistics.component.css']
})
export class VisitStatisticsComponent implements OnInit {
    statistics: Statistic[];
   /* chartData: Date[];*/

    constructor(private statisticsService: StatisticsService) {
    }

    ngOnInit() {
        window.scrollTo(0, 0);
        this.getCharData();

    }


getCharData(){
    this.statisticsService.findAll().subscribe(data => {
        console.log(data)

        /*data.forEach((c)=>{
            this.chartData.push(c.lastVisit)
        })

        console.log(this.chartData)*/
    });
}
    chartData = [
        { data: [330, 600, 260, 700], label: 'Account A' },
        { data: [120, 455, 100, 340], label: 'Account B' },
        { data: [45, 67, 800, 500], label: 'Account C' }
    ];
    chartLabels = ['January', 'February', 'Mars', 'April'];

    chartOptions = {
        responsive: true,
        scales: {
            xAxes: [{
                stacked: true,
                time: {
                    unit: 'hour'
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
