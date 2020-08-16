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

    // array colori grafico
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
                stacked: true,
                type: 'time',
                time: {
                    unit: 'day',
                    displayFormats: {
                        day: 'DD MM YYYY',
                    },
                },
            }], yAxes: [{
                stacked: true,
                ticks: {
                    beginAtZero: true,

                },
            }]
        },
    };

    constructor(private statisticsService: StatisticsService) {
    }

    ngOnInit() {
        this.statisticsService.findAll().subscribe(data => {
            console.log(data);
            data.countedVisit.forEach((c) => {
                this.chartLabels.push(c.visitDate);
                this.lineChartData.push(c.countVisit);
            });
        });
    }
}

