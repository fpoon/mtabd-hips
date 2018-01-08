(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('ServiceDetailController', ServiceDetailController);

    ServiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Service', 'Performer'];

    function ServiceDetailController($scope, $rootScope, $stateParams, previousState, entity, Service, Performer) {
        var vm = this;

        vm.service = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mtabdApp:serviceUpdate', function(event, result) {
            vm.service = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
