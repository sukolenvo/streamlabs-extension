define(['./app'], function (app) {
    return app.config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

        $urlRouterProvider.otherwise("/");

        $stateProvider
            .state('createAlert',
                {
                    url: "/create-alert",
                    controller: "createAlertCtrl",
                    templateUrl: "/static/template/createAlert.html",
                    data: {
                        title: "Create Alert"
                    }
                })
            .state('editAlert',
                {
                    url: "/edit-alert/:alertId",
                    controller: "editAlertCtrl",
                    templateUrl: "/static/template/editAlert.html",
                    data: {
                        title: "Edit Alert"
                    }
                })
            .state('listAlerts',
                {
                    url: "/list-alert",
                    controller: "listAlertsCtrl",
                    templateUrl: "/static/template/listAlerts.html",
                    data: {
                        title: "List Alerts"
                    }
                })
            .state('home',
                {
                    url: "/",
                    controller: 'homeCtrl',
                    templateUrl: "/static/template/home.html",
                    data: {
                        title: "Home"
                    }
                });

    }]);
});