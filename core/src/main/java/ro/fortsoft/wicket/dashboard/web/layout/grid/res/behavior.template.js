$('.gridster>ul').gridster({
    widget_selector: 'li.widget',
    widget_margins: [10, 10],
    widget_base_dimensions: [600, 330],
    min_cols: 2,
    draggable: {
        stop: function(e, ui) {
            ${stopBehavior};
        }
    },
    resize: {
        enabled: true,
        stop: function(e, ui, widget) {
            /*
            // retrieves the new dimensions (use these dimensions in resizeBehavior)
            var columnSpan = this.resize_coords.grid.size_x;
            var rowSpan = this.resize_coords.grid.size_y;

            ${resizeBehavior};
            */
            ${stopBehavior};
        }
    }
}).data('gridster');
