import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../../../service/statistics.service';
import * as moment from 'moment';

@Component({
    selector: 'app-visit-statistics',
    templateUrl: './visit-statistics.component.html',
    styleUrls: ['./visit-statistics.component.css']
})
export class VisitStatisticsComponent implements OnInit {
    newUsers: number[] = [];
    totalVisits: number[] = [];
    dates: Date[] = [];
    visitGraphName = 'Visitors';
    newVisitGraphName = 'New Visitors';

    public graphData: Array<any> = [
        {
            data: this.totalVisits,
            label: this.visitGraphName
        },
        {
            data: this.newUsers,
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
        let date = new Date();
        date.setMonth(date.getMonth() - 1);
        const fromDate = new Date(date.getFullYear(), date.getMonth(), 1);
        let toDate = new Date();

        this.statisticsService.findAll(this.formatDate(fromDate), this.formatDate(toDate)).subscribe(data => {
            data.newUsers.forEach((c) => {
                this.newUsers.push(c);
            });
            data.totalVisits.forEach((c) => {
                this.totalVisits.push(c);
            });
            this.dates = data.dates
        });
    }

    formatDate(date) {
        return moment(date).format('yyyy-MM-DDTHH:mm:ss');
    }
}

