define(function (require) {
    var Backbone = require('backbone');
    var User = require('app/js/admin/models/User');
    var Page = require('app/js/admin/collections/Page');

    return Backbone.Collection.extend(_.extend({
        model: User,
        url: function () {
            return 'api/v1/users';
        }
    }, Page));
});