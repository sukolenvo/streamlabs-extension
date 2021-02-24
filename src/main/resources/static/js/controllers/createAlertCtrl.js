define(['./module'], function (controllers) {
    'use strict';

    controllers.controller('createAlertCtrl', ['$scope', '$http', 'common',
      function ($scope, $http, common) {

          $scope.alertTypes = ["FOLLOW", "SUBSCRIPTION", "HOST", "DONATE"];

          function loadAlert() {
              $http.get('/api/alert/default').then(function (response) {
                  $scope.alert = response.data;
              }, common.errorCallback("Failed to load default image alert configuration"));

          }
          $scope.save = function() {
              $http.post('/api/alert/save', $scope.alert)
                  .then(common.successCallback("Alert successfully created"),
                      common.errorCallback("Failed to save alert"))

          };

          loadAlert();
      }])
});
