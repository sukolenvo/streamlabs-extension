define(['./module'], function (controllers) {
    'use strict';

    controllers.controller('homeCtrl', ['$scope', '$http', 'common',
        function ($scope, $http, common) {

            $http.get('/api/alert/enabled').then(function (response) {
                $scope.alertsEnabled = response.data;
            }, common.errorCallback("Failed load alerts status from server"));

            $http.get('/api/connected-channel').then(function (response) {
                $scope.connectedChannel = response.data;
            }, common.errorCallback("Failed to load channel configuration from server"));

            $http.get('/streamlabs/auth/status').then(function (response) {
                $scope.streamlabsConnected = response.data;
            }, common.errorCallback("Failed to load Streamlabs connection status from server"));
        }])
});
