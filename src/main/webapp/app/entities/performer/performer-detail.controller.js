(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('PerformerDetailController', PerformerDetailController);

    PerformerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Performer'];

    function PerformerDetailController($scope, $rootScope, $stateParams, previousState, entity, Performer) {
        var vm = this;

        vm.performer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mtabdApp:performerUpdate', function(event, result) {
            vm.performer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
