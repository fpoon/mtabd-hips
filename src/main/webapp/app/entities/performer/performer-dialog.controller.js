(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('PerformerDialogController', PerformerDialogController);

    PerformerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Performer'];

    function PerformerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Performer) {
        var vm = this;

        vm.performer = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.performer.id !== null) {
                Performer.update(vm.performer, onSaveSuccess, onSaveError);
            } else {
                Performer.save(vm.performer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mtabdApp:performerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
