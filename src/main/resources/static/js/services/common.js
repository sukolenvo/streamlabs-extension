define(['./module',
        'toastr'], function (services) {
    'use strict';
    services.service('common', ['toastr', function (toastr) {
        return {
            successCallback: function (msg) {
                return function () {
                    toastr.success(msg);
                }
            },
            errorCallback: function (msg) {
                return function () {
                    toastr.error(msg);
                }
            }
        }
    }]);
});