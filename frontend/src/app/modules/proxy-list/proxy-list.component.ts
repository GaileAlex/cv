import { Component } from '@angular/core';
import { ProxyService } from "../../service/proxy.service";
import { ProxyLists } from "../../models/proxyLists";

@Component({
    selector: 'app-proxy-list',
    templateUrl: './proxy-list.component.html',
    styleUrls: ['./proxy-list.component.css']
})
export class ProxyListComponent {
    proxyList: ProxyLists[];
    total: number;
    page = 0;
    pageSize = 10;
    padding = '100px';

    constructor(private proxyService: ProxyService) {
        window.scrollTo(0, 0);

        this.findAllProxies(this.pageSize, this.page)
    }

    changePage(event) {
        this.findAllProxies(this.pageSize, event.pageIndex)
    }

    findAllProxies(pageSize, pageIndex) {
        this.proxyService.findAll(pageSize, pageIndex).subscribe(data => {
            this.proxyList = data.proxyLists;
            this.total = data.total;
            if (this.proxyList.length < 5) {
                this.padding = '200px';
            } else {
                this.padding = '100px';
            }
        }, error => {
            this.proxyList = error;
        });
    }
}
