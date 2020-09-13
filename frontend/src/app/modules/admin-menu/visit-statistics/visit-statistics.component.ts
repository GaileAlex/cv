import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../../../service/statistics.service';

@Component({
    selector: 'app-visit-statistics',
    templateUrl: './visit-statistics.component.html',
    styleUrls: ['./visit-statistics.component.css']
})
export class VisitStatisticsComponent implements OnInit {
    countedVisitData: number[] = [];
    countedNewVisitData: number[] = [];
    countedVisitDates: Date[] = [];
    public visitGraphName = 'Visitors';
    public newVisitGraphName = 'New Visitors';

    public graphData: Array<any> = [
        {
            data: this.countedVisitData,
            label: this.visitGraphName
        },
        {
            data: this.countedNewVisitData,
            label: this.newVisitGraphName
        }
    ];

    public lineChartColors: Array<any> = [
        {
            backgroundColor: 'rgb(175,203,199)',
            borderColor: 'rgb(47,79,129)',
            pointBackgroundColor: 'rgb(50,98,177)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(63,105,174,0.8)'
        }
    ];

    chartOptions = {
        responsive: true,
        scales: {
            xAxes: [{
                type: 'time',
                time: {
                    unit: 'day',
                    tooltipFormat: 'DD.MM.YYYY',
                    displayFormats: {
                        day: 'DD.MM.YYYY',
                    },

                },

            }], yAxes: [{
                stacked: true,
                ticks: {
                    beginAtZero: true,
                },
                scaleLabel: {
                    display: true,
                    labelString: 'Visitors',
                    fontColor: 'green'
                },
            }]
        },
    };

    constructor(private statisticsService: StatisticsService) {
    }

    ngOnInit() {
        this.statisticsService.findAll().subscribe(data => {
            data.countedVisit.forEach((c) => {
                this.countedVisitData.push(c.countVisits);
                this.countedVisitDates.push(c.visitDate);
            });
            data.visitStatisticsNewUsers.forEach((c) => {
                this.countedNewVisitData.push(c.countVisits);
            });
            console.log(data.visitStatisticsNewUsers)
            console.log(data.countedVisit)
        });
    }
}

