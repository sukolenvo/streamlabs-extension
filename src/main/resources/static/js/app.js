define([
           'angular',
           'angular-ui-router',
           './controllers/index',
           './services/index',
           'toastr',
           './services/common',
           'angular-ui-bootstrap',
           'bootstrap-js'
    ],
    function (ng) {
        'use strict';

        return ng.module('app', [
            'app.controllers',
            'app.services',
            'ui.router',
            'toastr',
            'ui.bootstrap'
        ]).config(['$provide', '$httpProvider', function ($provide, $httpProvider) {

        }]).run(['$transitions', function ($transitions) {
            $transitions.onStart({}, function (trans) {
                trans.promise.then(function (event) {
                    document.title = event.data.title ? event.data.title : "VOP Admin";
                })
            });
        }]);
    });
