(function() {
    'use strict';

    angular
        .module('mtabdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('chat', {
            parent: 'entity',
            url: '/chat?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Chats'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chat/chats.html',
                    controller: 'ChatController',
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
        .state('chat-detail', {
            parent: 'chat',
            url: '/chat/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Chat'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chat/chat-detail.html',
                    controller: 'ChatDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Chat', function($stateParams, Chat) {
                    return Chat.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'chat',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('chat-detail.edit', {
            parent: 'chat-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chat/chat-dialog.html',
                    controller: 'ChatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Chat', function(Chat) {
                            return Chat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chat.new', {
            parent: 'chat',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chat/chat-dialog.html',
                    controller: 'ChatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('chat', null, { reload: 'chat' });
                }, function() {
                    $state.go('chat');
                });
            }]
        })
        .state('chat.edit', {
            parent: 'chat',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chat/chat-dialog.html',
                    controller: 'ChatDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Chat', function(Chat) {
                            return Chat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chat', null, { reload: 'chat' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chat.delete', {
            parent: 'chat',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chat/chat-delete-dialog.html',
                    controller: 'ChatDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Chat', function(Chat) {
                            return Chat.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chat', null, { reload: 'chat' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
