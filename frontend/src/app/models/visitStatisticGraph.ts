export class VisitStatisticGraph {
    countedVisit: [{
        countVisit: number
        visitDate: Date;
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
