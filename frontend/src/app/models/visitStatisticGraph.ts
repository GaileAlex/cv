export class VisitStatisticGraph {
    countedVisit: [{
        countVisits: number
        visitDate: Date;
    }];

    visitStatisticsNewUsers: [{
        countVisits: number
        firstVisit: Date;
    }];

    statistic: [{
        id: string;
        userIP: string;
        userLocation: string;
        firstVisit: Date;
        lastVisit: Date;
        totalVisits: string;
        username: string;
    }];

}
