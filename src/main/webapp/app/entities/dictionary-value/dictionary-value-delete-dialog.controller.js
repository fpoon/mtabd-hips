(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('DictionaryValueDeleteController',DictionaryValueDeleteController);

    DictionaryValueDeleteController.$inject = ['$uibModalInstance', 'entity', 'DictionaryValue'];

    function DictionaryValueDeleteController($uibModalInstance, entity, DictionaryValue) {
        var vm = this;

        vm.dictionaryValue = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DictionaryValue.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
