import * as moment from 'moment-timezone';

export class Utils {

    static FORMAT_DATE = 'DD.MM.YYYY';
    static FORMAT_DATE_TIME = 'DD.MM.YYYY HH:mm';
    static FORMAT_DATE_TIME_S = 'DD.MM.YYYY HH:mm:ss';

    static parseQuery(queryString: string): any {
        var query = {};
        var pairs = (queryString[0] === '?' ? queryString.substr(1) : queryString).split('&');
        for (var i = 0; i < pairs.length; i++) {
            var pair = pairs[i].split('=');
            query[decodeURIComponent(pair[0])] = decodeURIComponent(pair[1] || '');
        }
        return query;
    }

    static getMomentValue(date: moment.Moment, format: string): string {
        return (!!date && moment.isMoment(date)) ? date.format(format) : '';
    }

    static formatDate(date: any, format: string = Utils.FORMAT_DATE): string {
        return moment(date).format(format);
    }

    static buildQueryString(args: any): string {
        let res = '';
        Object.keys(args || {}).forEach(key => {
            if (args[key] !== null && args[key] !== undefined && args[key] !== '') {
                if (Array.isArray(args[key])) {
                    args[key].forEach(arg => {
                        res += res ? '&' : '';
                        res += [encodeURIComponent(key), '=', encodeURIComponent(arg)].join('');
                    });
                } else {
                    res += res ? '&' : '';
                    res += [encodeURIComponent(key), '=', encodeURIComponent(args[key])].join('');
                }
            }
        });
        return res;
    }

    static compileURL(url: string, args: any): string {
        let res = String(url);
        const queryString = Utils.buildQueryString(args);
        if (queryString) {
            res += -1 === url.indexOf('\?') ? '\?' : '&';
        }
        res += queryString;
        return res;
    }

    static formatString(str: string, args: any): string {
        let result = str;
        if (!!args && ('object' === typeof args)) {
            Object.keys(args).forEach(key => {
                result = result.replace(new RegExp('\\{' + key + '\\}', 'g'), args[key]);
            });
        }
        return result;
    }

    static createDeepCopy(obj: any): any {
        return JSON.parse(JSON.stringify(obj));
    }

    static scrollToTop(): void {
        window.scrollTo(0, 0);
    }

    static getMobileOperatingSystem() {
        const userAgent = navigator.userAgent || navigator.vendor;
        const mobileOS = {
            windowsPhone: false,
            android: false,
            iOS: false
        };

        // Windows Phone must come first because its UA also contains "Android"
        if (/windows phone/i.test(userAgent)) {
            mobileOS.windowsPhone = true;
            return mobileOS;
        }

        if (/android/i.test(userAgent)) {
            mobileOS.android = true;
            return mobileOS;
        }

        // iOS detection from: http://stackoverflow.com/a/9039885/177710
        // Removed ' && !window.MSStream' to fix compiler error. Seems to work just fine, despite the warning in the issue above
        if (/iPad|iPhone|iPod/g.test(userAgent)) {
            mobileOS.iOS = true;
            return mobileOS;
        }

        return mobileOS;
    }

    static isMobileOperatingSystem() {
        return Object.values(this.getMobileOperatingSystem()).includes(true);
    }

    static isEmptyObject(obj) {
        if (!obj) {
            return true;
        }

        return Object.keys(obj).length === 0 && obj.constructor === Object;
    }

    // does not support object arrays currently
    static areArraysEqual(arr1, arr2) {
        if (!arr1) arr1 = [];
        if (!arr2) arr2 = [];
        if (arr1 && Array.isArray(arr1)) arr1 = arr1.sort();
        if (arr2 && Array.isArray(arr2)) arr2 = arr2.sort();

        return JSON.stringify(arr1) == JSON.stringify(arr2);
    }

    static getClearedObject(obj = {}) {
        const res = {};
        Object.keys(obj).forEach(key => {
            if (obj[key] !== undefined) {
                res[key] = obj[key];
            }
        });
        return res;
    }

    static sortObjectsByProperty(items: any[], prop: string) {
        if (!items) {
            return null;
        }

        return items.sort((a, b) => {
            const ap = a[prop];
            const bp = b[prop];

            return ap === bp ? 0 : (ap < bp ? -1 : 1);
        });
    }

    static isActive(from, to): boolean {
        const validFrom = from ? moment(from) : null;
        const validTo = to ? moment(to) : null;

        if (!validFrom && !validTo) {
            return true;
        } else if (!validFrom && validTo) {
            return validTo.isSameOrAfter(moment(), 'day');
        } else if (validFrom && !validTo) {
            return validFrom.isSameOrBefore(moment(), 'day');
        } else {
            return validTo.isSameOrAfter(moment(), 'day') && validFrom.isSameOrBefore(moment(), 'day');
        }
    }

}
