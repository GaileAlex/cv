import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../../../service/statistics.service';

@Component({
    selector: 'app-visit-statistics',
    templateUrl: './visit-statistics.component.html',
    styleUrls: ['./visit-statistics.component.css']
})
export class VisitStatisticsComponent implements OnInit {
    lineChartData: number[] = [];
    chartLabels: Date[] = [];
    public graphName = 'Visitors';

    public labelMFL: Array<any> = [
        {
            data: this.lineChartData,
            label: this.graphName
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
                this.chartLabels.push(c.visitDate);
                this.lineChartData.push(c.countVisits);
            });
        });
    }
}

