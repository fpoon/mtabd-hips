(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('MessageDialogController', MessageDialogController);

    MessageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Message', 'User', 'Chat'];

    function MessageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Message, User, Chat) {
        var vm = this;

        vm.message = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.chats = Chat.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.message.id !== null) {
                Message.update(vm.message, onSaveSuccess, onSaveError);
            } else {
                Message.save(vm.message, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mtabdApp:messageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
