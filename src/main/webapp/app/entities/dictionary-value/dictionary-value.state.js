(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dictionary-value', {
            parent: 'entity',
            url: '/dictionary-value?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DictionaryValues'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dictionary-value/dictionary-values.html',
                    controller: 'DictionaryValueController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('dictionary-value-detail', {
            parent: 'dictionary-value',
            url: '/dictionary-value/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DictionaryValue'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dictionary-value/dictionary-value-detail.html',
                    controller: 'DictionaryValueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DictionaryValue', function($stateParams, DictionaryValue) {
                    return DictionaryValue.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dictionary-value',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('dictionary-value-detail.edit', {
            parent: 'dictionary-value-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dictionary-value/dictionary-value-dialog.html',
                    controller: 'DictionaryValueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DictionaryValue', function(DictionaryValue) {
                            return DictionaryValue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dictionary-value.new', {
            parent: 'dictionary-value',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dictionary-value/dictionary-value-dialog.html',
                    controller: 'DictionaryValueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                longName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dictionary-value', null, { reload: 'dictionary-value' });
                }, function() {
                    $state.go('dictionary-value');
                });
            }]
        })
        .state('dictionary-value.edit', {
            parent: 'dictionary-value',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dictionary-value/dictionary-value-dialog.html',
                    controller: 'DictionaryValueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DictionaryValue', function(DictionaryValue) {
                            return DictionaryValue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dictionary-value', null, { reload: 'dictionary-value' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dictionary-value.delete', {
            parent: 'dictionary-value',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dictionary-value/dictionary-value-delete-dialog.html',
                    controller: 'DictionaryValueDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DictionaryValue', function(DictionaryValue) {
                            return DictionaryValue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dictionary-value', null, { reload: 'dictionary-value' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
