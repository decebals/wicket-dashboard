$('.gridster>ul').gridster({
    widget_selector: 'li.widget',
    widget_margins: [10, 10],
    widget_base_dimensions: [600, 330],
    min_cols: 2,
    draggable: {
        stop: function(event, ui) {
            ${stopBehavior};
        }
    },
    resize: { enabled: true }
}).data('gridster');
