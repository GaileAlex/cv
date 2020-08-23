export class VisitStatisticGraph {
    countedVisit: [{
        countVisits: number
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
