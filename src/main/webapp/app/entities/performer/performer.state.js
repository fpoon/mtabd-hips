(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('performer', {
            parent: 'entity',
            url: '/performer?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Performers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/performer/performers.html',
                    controller: 'PerformerController',
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
        .state('performer-detail', {
            parent: 'performer',
            url: '/performer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Performer'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/performer/performer-detail.html',
                    controller: 'PerformerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Performer', function($stateParams, Performer) {
                    return Performer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'performer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('performer-detail.edit', {
            parent: 'performer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/performer/performer-dialog.html',
                    controller: 'PerformerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Performer', function(Performer) {
                            return Performer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('performer.new', {
            parent: 'performer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/performer/performer-dialog.html',
                    controller: 'PerformerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                username: null,
                                firstName: null,
                                lastName: null,
                                email: null,
                                age: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('performer', null, { reload: 'performer' });
                }, function() {
                    $state.go('performer');
                });
            }]
        })
        .state('performer.edit', {
            parent: 'performer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/performer/performer-dialog.html',
                    controller: 'PerformerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Performer', function(Performer) {
                            return Performer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('performer', null, { reload: 'performer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('performer.delete', {
            parent: 'performer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/performer/performer-delete-dialog.html',
                    controller: 'PerformerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Performer', function(Performer) {
                            return Performer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('performer', null, { reload: 'performer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
