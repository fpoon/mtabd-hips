(function() {
    'use strict';
    angular
        .module('mtabdApp')
        .factory('Service', Service);

    Service.$inject = ['$resource', 'DateUtils'];

    function Service ($resource, DateUtils) {
        var resourceUrl =  'api/services/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.finishDate = DateUtils.convertLocalDateFromServer(data.finishDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.finishDate = DateUtils.convertLocalDateToServer(copy.finishDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.finishDate = DateUtils.convertLocalDateToServer(copy.finishDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
