(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('DictionaryValueDetailController', DictionaryValueDetailController);

    DictionaryValueDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DictionaryValue', 'Translation', 'Dictionary'];

    function DictionaryValueDetailController($scope, $rootScope, $stateParams, previousState, entity, DictionaryValue, Translation, Dictionary) {
        var vm = this;

        vm.dictionaryValue = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mtabdApp:dictionaryValueUpdate', function(event, result) {
            vm.dictionaryValue = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
