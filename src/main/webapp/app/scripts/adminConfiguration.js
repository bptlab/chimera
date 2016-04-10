'use strict';

(function () {
    var adminCon = angular.module('adminConfiguration', []);

    //introducing an custom filter for checking unique entries within an array
    
    adminCon.filter('unique', function () {
        return function (collection, keyname) {
            var output = [],
                keys = [];

            angular.forEach(collection, function (item) {
                var key = item[keyname];
                if (keys.indexOf(key) === -1) {
                    keys.push(key);
                    output.push(item);
                }
            });
            return output;
        };
    });
})();
