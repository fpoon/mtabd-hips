(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .controller('ServiceDialogController', ServiceDialogController);

    ServiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Service', 'Performer'];

    function ServiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Service, Performer) {
        var vm = this;

        vm.service = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.services = Performer.query({filter: 'service-is-null'});
        $q.all([vm.service.$promise, vm.services.$promise]).then(function() {
            if (!vm.service.serviceId) {
                return $q.reject();
            }
            return Performer.get({id : vm.service.serviceId}).$promise;
        }).then(function(service) {
            vm.services.push(service);
        });
        vm.performers = Performer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.service.id !== null) {
                Service.update(vm.service, onSaveSuccess, onSaveError);
            } else {
                Service.save(vm.service, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mtabdApp:serviceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.finishDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
