$('.gridster>ul').gridster({
    widget_selector: 'li.widget',
    widget_margins: [${widgetHorizontalMargin}, ${widgetVerticalMargin}],
    widget_base_dimensions: [${widgetBaseWidth}, ${widgetBaseHeight}],
    min_cols: 1,
    draggable: {
        stop: function(e, ui) {
            // save the new layout
            ${stopBehavior};
        }
    },
    resize: {
        enabled: true,
        stop: function(e, ui, widget) {
            // save the new layout
            ${stopBehavior};
            // force repaint the resized widget
            ${widgetResizeBehavior};
        }
    }
}).data('gridster');
