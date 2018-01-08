(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('MessageDetailController', MessageDetailController);

    MessageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Message', 'User', 'Chat'];

    function MessageDetailController($scope, $rootScope, $stateParams, previousState, entity, Message, User, Chat) {
        var vm = this;

        vm.message = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mtabdApp:messageUpdate', function(event, result) {
            vm.message = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
