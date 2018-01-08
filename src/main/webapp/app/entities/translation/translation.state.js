(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('translation', {
            parent: 'entity',
            url: '/translation?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Translations'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/translation/translations.html',
                    controller: 'TranslationController',
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
        .state('translation-detail', {
            parent: 'translation',
            url: '/translation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Translation'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/translation/translation-detail.html',
                    controller: 'TranslationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Translation', function($stateParams, Translation) {
                    return Translation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'translation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('translation-detail.edit', {
            parent: 'translation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/translation/translation-dialog.html',
                    controller: 'TranslationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Translation', function(Translation) {
                            return Translation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('translation.new', {
            parent: 'translation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/translation/translation-dialog.html',
                    controller: 'TranslationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('translation', null, { reload: 'translation' });
                }, function() {
                    $state.go('translation');
                });
            }]
        })
        .state('translation.edit', {
            parent: 'translation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/translation/translation-dialog.html',
                    controller: 'TranslationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Translation', function(Translation) {
                            return Translation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('translation', null, { reload: 'translation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('translation.delete', {
            parent: 'translation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/translation/translation-delete-dialog.html',
                    controller: 'TranslationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Translation', function(Translation) {
                            return Translation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('translation', null, { reload: 'translation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
