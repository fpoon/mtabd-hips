(function() {
    'use strict';
    angular
        .module('mtabdApp')
        .factory('Performer', Performer);

    Performer.$inject = ['$resource'];

    function Performer ($resource) {
        var resourceUrl =  'api/performers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
