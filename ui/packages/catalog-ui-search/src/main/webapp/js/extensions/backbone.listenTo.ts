/**
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 **/
import * as Backbone from "backbone";

const listenTo = Backbone.View.prototype.listenTo;
/**
 * Overrides listenTo to make it behave as it should.  In other words, not fire after views are destroyed.
 * Call is preferred since it's performance is much faster than apply.
 */
Backbone.View.prototype.listenTo = function(obj: any, name: string, callback: Function) {
    return listenTo.call(this, obj, name, function() {
        if (callback === undefined) {
            console.warn(`Found no callback for listener in ${this.tagName}`);
            return;
        }
        if (this.isDestroyed !== true) {
            const a1 = arguments[0], a2 = arguments[1], a3 = arguments[2];
            switch (arguments.length) {
                case 0:
                    callback.call(this);
                    return;
                case 1:
                    callback.call(this, a1);
                    return;
                case 2:
                    callback.call(this, a1, a2);
                    return;
                case 3:
                    callback.call(this, a1, a2, a3);
                    return;
                default:
                    callback.apply(this, arguments);
                    return;
            }
        }
    });
};