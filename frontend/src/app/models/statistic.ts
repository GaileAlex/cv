import { VisitStatisticUser } from "./visitStatisticUser";

export class Statistic {
    id: string;
    userIP: string;
    userLocation: string;
    firstVisit: Date;
    lastVisit: Date;
    totalVisits: string;
    username: string;
    visitStatisticUser: VisitStatisticUser[];
}
