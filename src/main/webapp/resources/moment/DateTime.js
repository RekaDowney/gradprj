/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/19 15:11
 * @description :
 */
DayOfWeek = {
    MONDAY: 1,
    TUESDAY: 2,
    WEDNESDAY: 3,
    THURSDAY: 4,
    FRIDAY: 5,
    SATURDAY: 6,
    SUNDAY: 7,
    parse: function (num) {
        switch (num) {
            case 1:
                return DayOfWeek.MONDAY;
            case 2:
                return DayOfWeek.TUESDAY;
            case 3:
                return DayOfWeek.WEDNESDAY;
            case 4:
                return DayOfWeek.THURSDAY;
            case 5:
                return DayOfWeek.FRIDAY;
            case 6:
                return DayOfWeek.SATURDAY;
            case 7:
                return DayOfWeek.SUNDAY;
        }
    }
};

function DateTime(date) {
    if (date && (moment.isDate(date) || moment.isMoment(date))) {
        this.moment = moment(date);
    } else {
        this.moment = moment();
    }
}

/*
 function isDate(date) {
 return Object.prototype.toString.call(date) === '[object Date]';
 }

 function isMoment(date) {
 return Object.prototype.toString.call(date) === '[object Object]' &&
 date.constructor.name === 'Moment';
 }
 */

function normalize(pattern) {
    return pattern.replace(/y/g, 'Y').replace(/d/g, 'D');
}

DateTime.now = function () {
    return new DateTime();
};
DateTime.parse = function (dateString, pattern) {
    return new DateTime(moment(dateString, normalize(pattern)));
};

DateTime.prototype = {
    constructor: DateTime,
    format: function (pattern) {
        return this.moment.format(normalize(pattern));
    },
    toString: function () {
        return this.moment.format('YYYY-MM-DD HH:mm:ss');
    },
    getYear: function () {
        return this.moment.year();
    },
    getMonth: function () {
        // moment.month() 方法返回 0【代表1月】 - 11【代表12月】
        return this.moment.month() + 1;
    },
    getDayOfMonth: function () {
        return this.moment.date();
    },
    getDayOfYear: function () {
        return this.moment.dayOfYear();
    },
    getWeek: function () {
        var week = this.moment.day();
        // 将周日从 0 变成 7
        return week === 0 ? 7 : week;
    },
    getHour: function () {
        return this.moment.hour();
    },
    getMinute: function () {
        return this.moment.minute();
    },
    getSecond: function () {
        return this.moment.second();
    },
    getMillSecond: function () {
        return this.moment.millisecond();
    },
    withYear: function (year) {
        return new DateTime(this.moment.clone().year(year));
    },
    withMonth: function (month) {
        month = month - 1;
        return new DateTime(this.moment.clone().month(month));
    },
    withDayOfMonth: function (day) {
        return new DateTime(this.moment.clone().date(day));
    },
    withHour: function (hour) {
        return new DateTime(this.moment.clone().hour(hour));
    },
    withMinute: function (minute) {
        return new DateTime(this.moment.clone().minute(minute));
    },
    withSecond: function (second) {
        return new DateTime(this.moment.clone().second(second));
    },
    withMillSecond: function (ms) {
        return new DateTime(this.moment.clone().millisecond(ms));
    },
    plusYears: function (year) {
        return new DateTime(this.moment.clone().add(year, 'years'));
    },
    plusMonths: function (month) {
        return new DateTime(this.moment.clone().add(month, 'months'));
    },
    plusDays: function (day) {
        return new DateTime(this.moment.clone().add(day, 'days'));
    },
    plusHours: function (hour) {
        return new DateTime(this.moment.clone().add(hour, 'hours'));
    },
    plusMinutes: function (minute) {
        return new DateTime(this.moment.clone().add(minute, 'minutes'));
    },
    plusSeconds: function (second) {
        return new DateTime(this.moment.clone().add(second, 'seconds'));
    },
    plusMillSeconds: function (ms) {
        return new DateTime(this.moment.clone().add(ms, 'milliseconds'));
    },
    minusYears: function (year) {
        return new DateTime(this.moment.clone().subtract(year, 'years'));
    },
    minusMonths: function (month) {
        return new DateTime(this.moment.clone().subtract(month, 'months'));
    },
    minusDays: function (day) {
        return new DateTime(this.moment.clone().subtract(day, 'days'));
    },
    minusHours: function (hour) {
        return new DateTime(this.moment.clone().subtract(hour, 'hours'));
    },
    minusMinutes: function (minute) {
        return new DateTime(this.moment.clone().subtract(minute, 'minutes'));
    },
    minusSeconds: function (second) {
        return new DateTime(this.moment.clone().subtract(second, 'seconds'));
    },
    minusMillSeconds: function (ms) {
        return new DateTime(this.moment.clone().subtract(ms, 'milliseconds'));
    },
    toDate: function () {
        // 转成 JS Date 对象
        return this.moment.toDate();
    },
    isBefore: function (dateTime) {
        return this.moment.isBefore(dateTime.moment);
    },
    isBeforeNow: function () {
        return this.moment.isBefore(moment());
    },
    isAfter: function (dateTime) {
        return this.moment.isAfter(dateTime.moment);
    },
    isAfterNow: function () {
        return this.moment.isAfter(moment());
    },
    isBetween: function (start, end) {
        return this.moment.isBetween(start, end);
    },
    isLeapYear: function () {
        return this.moment.isLeapYear();
    }
};