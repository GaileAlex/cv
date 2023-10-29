import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../../../service/statistics.service';
import * as moment from 'moment';
import { VisitStatisticsTables } from "../../../models/visitStatisticsTables";

@Component({
    selector: 'app-visit-statistics',
    templateUrl: './visit-statistics.component.html',
    styleUrls: ['./visit-statistics.component.css']
})
export class VisitStatisticsComponent implements OnInit {
    rangeDates: Date[] | undefined;
    newUsers: number[] = [];
    totalVisits: number[] = [];
    dates: Date[] = [];
    visitGraphName = 'Visits';
    newVisitGraphName = 'New Visitors';
    visitStatisticsTables: VisitStatisticsTables[] = [];
    total: number;
    page = 0;
    pageSize = 10;

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
                    labelString: 'Visits',
                    fontColor: 'green'
                },
            }]
        },
    };

    constructor(private statisticsService: StatisticsService) {
    }

    ngOnInit() {
        window.scrollTo(0, 0);
        this.sendRequest(this.page);
    }

    formatDate(date) {
        return moment(date).format('yyyy-MM-DDTHH:mm:ss');
    }

    msToTime(ms) {
        const time = Number(ms) % 1000;
        ms = (Number(ms) - time) / 1000;
        const secs = Number(ms) % 60;
        ms = (Number(ms) - secs) / 60;
        const mins = Number(ms) % 60;
        const hrs = (Number(ms) - mins) / 60;

        return hrs + 'h ' + mins + 'm ' + secs + 's';
    }

    changePage(event) {
        this.sendRequest(event.pageIndex);
    }

    sendRequest(pageIndex) {
        let date = new Date();
        date.setMonth(date.getMonth() - 2);
        let fromDate: Date;
        let toDate: Date;
        if (!this.rangeDates || this.rangeDates[1] === null) {
            fromDate = new Date(date.getFullYear(), date.getMonth(), 1);
            toDate = new Date();
        } else {
            fromDate = this.rangeDates[0];
            toDate = this.rangeDates[1];
        }

        this.statisticsService.findAll(this.formatDate(fromDate), this.formatDate(toDate),
            this.pageSize, pageIndex).subscribe(data => {
            this.newUsers.length = 0;
            this.totalVisits.length = 0;
            data.newUsers.forEach((c) => {
                this.newUsers.push(c);
            });
            data.totalVisits.forEach((c) => {
                this.totalVisits.push(c);
            });
            this.dates = data.dates
            this.visitStatisticsTables = data.visitStatisticsTables;
            this.total = data.total;
            this.visitStatisticsTables.forEach((c) => c.totalTimeOnSite = this.msToTime(c.totalTimeOnSite))
        });
    }

}

