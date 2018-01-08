(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('ChatDialogController', ChatDialogController);

    ChatDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Chat', 'Service', 'Message'];

    function ChatDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Chat, Service, Message) {
        var vm = this;

        vm.chat = entity;
        vm.clear = clear;
        vm.save = save;
        vm.services = Service.query({filter: 'chat-is-null'});
        $q.all([vm.chat.$promise, vm.services.$promise]).then(function() {
            if (!vm.chat.serviceId) {
                return $q.reject();
            }
            return Service.get({id : vm.chat.serviceId}).$promise;
        }).then(function(service) {
            vm.services.push(service);
        });
        vm.messages = Message.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.chat.id !== null) {
                Chat.update(vm.chat, onSaveSuccess, onSaveError);
            } else {
                Chat.save(vm.chat, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mtabdApp:chatUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
