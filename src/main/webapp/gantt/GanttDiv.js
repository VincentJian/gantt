zk.afterLoad('zul.wgt', function() {
	zul.wgt.GanttDiv = zk.$extends(zul.wgt.Div, {
		bind_: function() {
			this.$supers('bind_', arguments);
			this._resolveConfliction();
			this._attachGanttEvent();
		},
		unbind_: function() {
			this._detachGanttEvent();
			this.$supers('unbind_', arguments);
		},
		_init: function(task) {
			gantt.init(this.uuid);
			gantt.parse(task);
		},
		_attachGanttEvent: function() {
			var self = this;
			gantt.attachEvent('onAfterTaskAdd', function(id, item) {
				zAu.send(new zk.Event(self, "onTaskAdd", {'' : item}, {toServer:true}));
			});
			gantt.attachEvent('onAfterTaskUpdate', function(id, item) {
				zAu.send(new zk.Event(self, "onTaskUpdate", {'' : item}, {toServer:true}));
			});
			gantt.attachEvent('onAfterTaskDelete', function(id, item) {
				zAu.send(new zk.Event(self, "onTaskDelete", {'' : item}, {toServer:true}));
			});

			gantt.attachEvent('onAfterLinkAdd', function(id, item) {
				zAu.send(new zk.Event(self, 'onLinkAdd', {'' : item}, {toServer:true}));
		    });
			gantt.attachEvent('onAfterLinkDelete', function(id, item) {
				zAu.send(new zk.Event(self, 'onLinkDelete', {'' : item}, {toServer:true}));
		    });
		},
		_detachGanttEvent: function() {
			gantt.detachAllEvents();
		},
		_resolveConfliction: function() {
			Object.defineProperties(Array.prototype, {
				'$indexOf': {enumerable: false},
				'$contains': {enumerable: false},
				'$equals': {enumerable: false},
				'$remove': {enumerable: false},
				'$addAll': {enumerable: false},
				'$clone': {enumerable: false}
			});
		}
	});
});
