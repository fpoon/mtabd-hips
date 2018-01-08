(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('DictionaryValueDialogController', DictionaryValueDialogController);

    DictionaryValueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'DictionaryValue', 'Translation', 'Dictionary'];

    function DictionaryValueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, DictionaryValue, Translation, Dictionary) {
        var vm = this;

        vm.dictionaryValue = entity;
        vm.clear = clear;
        vm.save = save;
        vm.translations = Translation.query({filter: 'dictionaryvalue-is-null'});
        $q.all([vm.dictionaryValue.$promise, vm.translations.$promise]).then(function() {
            if (!vm.dictionaryValue.translationId) {
                return $q.reject();
            }
            return Translation.get({id : vm.dictionaryValue.translationId}).$promise;
        }).then(function(translation) {
            vm.translations.push(translation);
        });
        vm.dictionaries = Dictionary.query({filter: 'dictionaryvalue-is-null'});
        $q.all([vm.dictionaryValue.$promise, vm.dictionaries.$promise]).then(function() {
            if (!vm.dictionaryValue.dictionaryId) {
                return $q.reject();
            }
            return Dictionary.get({id : vm.dictionaryValue.dictionaryId}).$promise;
        }).then(function(dictionary) {
            vm.dictionaries.push(dictionary);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dictionaryValue.id !== null) {
                DictionaryValue.update(vm.dictionaryValue, onSaveSuccess, onSaveError);
            } else {
                DictionaryValue.save(vm.dictionaryValue, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mtabdApp:dictionaryValueUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
