$('.gridster>ul').gridster({
    widget_selector: 'li.widget',
    widget_margins: [10, 10],
    widget_base_dimensions: [${widgetBaseWidth}, ${widgetBaseHeight}],
    min_cols: 1,
    draggable: {
        stop: function(e, ui) {
            ${stopBehavior};
        }
    },
    resize: {
        enabled: true,
        stop: function(e, ui, widget) {
            ${stopBehavior};
        }
    }
}).data('gridster');
