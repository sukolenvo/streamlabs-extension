require.config({
    urlArgs: "v=1",
    baseURL: '/static/js',
    paths: {
        'domReady': 'https://cdnjs.cloudflare.com/ajax/libs/require-domReady/2.0.1/domReady.min',
        'requireLib': 'https://cdnjs.cloudflare.com/ajax/libs/require.js/2.3.3/require.min',
        'angular': 'https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.6.1/angular.min',
        'angular-ui-router': 'https://cdnjs.cloudflare.com/ajax/libs/angular-ui-router/1.0.3/angular-ui-router.min',
        'toastr': 'https://unpkg.com/angular-toastr/dist/angular-toastr.tpls',
        'angular-ui-bootstrap': 'https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/2.5.0/ui-bootstrap-tpls.min',
        'jquery': '//cdn.jsdelivr.net/jquery/1/jquery.min',
        'moment': '//cdn.jsdelivr.net/momentjs/latest/moment.min',
        'bootstrap-js': 'https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.bundle.min'
    },
    include: ['domReady', 'requireLib'],
    shim: {
        'angular': {
            exports: 'angular'
        },
        'angular-ui-router': {
            deps: ['angular']
        },
        'toastr': {
            deps: ['angular']
        },
        'angular-ui-bootstrap': {
            deps: ['angular']
        },
        'moment': {
            deps: ['jquery']
        },
        'bootstrap-js': {
            deps: ['jquery']
        }
    },

    // запустить приложение
    deps: ['./bootstrap']
});