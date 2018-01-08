(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('ChatDetailController', ChatDetailController);

    ChatDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Chat', 'Service', 'Message'];

    function ChatDetailController($scope, $rootScope, $stateParams, previousState, entity, Chat, Service, Message) {
        var vm = this;

        vm.chat = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mtabdApp:chatUpdate', function(event, result) {
            vm.chat = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
