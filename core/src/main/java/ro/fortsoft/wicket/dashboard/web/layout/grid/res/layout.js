var Dashboard = Dashboard || {};

Dashboard.GridLayout = {
    serialize: function () {
        var items = [];
        $("li.widget").each(function () {
            var widgetId = $(this).attr("id");
            var widget = widgetId.substring(7); // "widget-".length
            var column = $(this).data("col") - 1;
            var row = $(this).data("row") - 1;
            var columnSpan = $(this).data("sizex");
            var rowSpan = $(this).data("sizey");
            // create item object for current panel
            var item = {
                widget: widget,
                column: column,
                row: row,
                columnSpan: columnSpan,
                rowSpan: rowSpan
            };

            // push item object into items array
            items.push(item);
        });

        // pass items variable to server to save state
        var data = $.toJSON(items);
//        console.log("data = " + data);

        return data;
    }
};
