$('#${component}').sortable({
    connectWith: '.column',
    handle: '.dragbox-header',
    forcePlaceholderSize: true,
    placeholder: 'placeholder',
    cursor: 'move',
    opacity: 0.5,
    stop: function(e, ui) {
        ${stopBehavior}
    }
});