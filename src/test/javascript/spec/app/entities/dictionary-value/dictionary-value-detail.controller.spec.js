'use strict';

describe('Controller Tests', function() {

    describe('DictionaryValue Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDictionaryValue, MockTranslation, MockDictionary;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDictionaryValue = jasmine.createSpy('MockDictionaryValue');
            MockTranslation = jasmine.createSpy('MockTranslation');
            MockDictionary = jasmine.createSpy('MockDictionary');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DictionaryValue': MockDictionaryValue,
                'Translation': MockTranslation,
                'Dictionary': MockDictionary
            };
            createController = function() {
                $injector.get('$controller')("DictionaryValueDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mtabdApp:dictionaryValueUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
