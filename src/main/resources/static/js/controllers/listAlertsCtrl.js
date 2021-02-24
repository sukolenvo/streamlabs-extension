define(['./module', 'jquery'], function (controllers, jquery) {
    'use strict';

    controllers.controller('listAlertsCtrl', ['$scope', '$http', 'common',
      function ($scope, $http, common) {

          function loadAlerts() {
              $http.get('/api/alert').then(function (response) {
                  $scope.alerts = response.data;
              }, common.errorCallback("Failed to load alerts list"));

          }

          $scope.testAlert = function(id) {
              $http.post('/api/alert/show/' + id).then(common.successCallback("Show alert request successfully submitted"),
                  common.errorCallback("Failed to show alert request, check server logs for more info"));
          };

          $scope.showDeleteConfirmation = function(alarm) {
              $scope.deleteCandidate = alarm;
              jquery('#deleteConfirmation').modal('show');
          };

          $scope.deleteSelectedAlert = function () {
              $http.delete('/api/alert/' + $scope.deleteCandidate.id).then(function () {
                      var index = $scope.alerts.indexOf($scope.deleteCandidate);
                      if (index >= 0) {
                          $scope.alerts.splice(index, 1);
                          common.successCallback("Alert has been deleted")();
                          jquery('#deleteConfirmation').modal('hide');
                      }
                  },
                  common.errorCallback("Failed to delete alert. Check server logs for more information"));
          };

          $scope.enableAlert = function(alert) {
              $http.post('/api/alert/enable/' + alert.id).then(function() {
                  alert.enabled = true;
                  },
                  common.errorCallback("Failed to update alert"));
          };

          $scope.disableAlert = function(alert) {
              $http.post('/api/alert/disable/' + alert.id).then(function() {
                      alert.enabled = false;
                  },
                  common.errorCallback("Failed to update alert"));
          };

          loadAlerts();
      }])
});
