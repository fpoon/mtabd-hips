(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('PerformerDeleteController',PerformerDeleteController);

    PerformerDeleteController.$inject = ['$uibModalInstance', 'entity', 'Performer'];

    function PerformerDeleteController($uibModalInstance, entity, Performer) {
        var vm = this;

        vm.performer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Performer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
