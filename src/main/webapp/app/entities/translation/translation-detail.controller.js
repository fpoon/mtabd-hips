(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('TranslationDetailController', TranslationDetailController);

    TranslationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Translation'];

    function TranslationDetailController($scope, $rootScope, $stateParams, previousState, entity, Translation) {
        var vm = this;

        vm.translation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mtabdApp:translationUpdate', function(event, result) {
            vm.translation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
