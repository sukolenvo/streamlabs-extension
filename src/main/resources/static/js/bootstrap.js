window.name = "NG_DEFER_BOOTSTRAP!";

require([
            'require',
            'angular',
            'app',
            'routes'
], function (require, ng) {
    'use strict';

    require(['domReady!'], function (document) {
        ng.bootstrap(document, ['app']);
        ng.resumeBootstrap();
    });
});