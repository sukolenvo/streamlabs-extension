define(['./module'], function (controllers) {
    'use strict';

    controllers.controller('editAlertCtrl', ['$scope', '$http', 'common', '$stateParams',
      function ($scope, $http, common, $stateParams) {

          $scope.alertTypes = ["FOLLOW", "SUBSCRIPTION", "HOST", "DONATE"];

          function loadAlert() {
              $http.get('/api/alert/' + $stateParams.alertId).then(function (response) {
                  $scope.alert = response.data;
              }, common.errorCallback("Failed to load alert"));

          }
          $scope.save = function() {
              $http.post('/api/alert/save', $scope.alert)
                  .then(common.successCallback("Alert successfully saved"),
                      common.errorCallback("Failed to save alert"))

          };

          loadAlert();
      }])
});
